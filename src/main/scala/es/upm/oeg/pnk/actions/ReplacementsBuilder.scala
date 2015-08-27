package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.data.Replacement
import es.upm.oeg.pnk.transformations.Folder
import es.upm.oeg.pnk.validators.{SpellValidator, WordValidator}
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.Word2VecModel

/**
 * Created by cbadenes on 05/08/15.
 */
object ReplacementsBuilder {

  val INPUT_FILES             = "part-00"

  val OUTPUT_IDENTIFICATION   = "output/replacements/identification"

  val OUTPUT_ANALYSIS         = "output/replacements/analysis"

  val OUTPUT_FIXED            = "output/replacements/fixed"


  def identify(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_IDENTIFICATION'..")
    Folder.moveIfExists(OUTPUT_IDENTIFICATION)

    println(s"reading raw corpus ..")
    // one document per line
    val documents = sc.
      wholeTextFiles(CorpusBuilder.OUTPUT_RAW).
      filter(_._1.contains("part-00")).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).map(_.replace("\n","")).filter(!_.isEmpty)

    println(s"loading word2vec model to detect similar words..")
    val w2vModel    = Word2VecModel.load(sc, ModelBuilder.OUTPUT_RAW_W2V)

    println(s"filtering relevant words ..")
    val words         = documents.flatMap(line => line.split(" ")).filter(WordValidator.isRelevant)

    println(s"counting frequencies ..")
    val wordsCounter  = words.map(word => (word, 1)).reduceByKey((a, b) => a + b).cache
    val counter       = wordsCounter.collectAsMap

    var analyzed : List[String] = List.empty[String]

    println(s"detecting similar words ..")
    wordsCounter.flatMap { case (word,counts) =>
      if (!analyzed.contains(word)) {
        analyzed = word :: analyzed
        val synonyms : List[(String,(Double,Double,Double))] = SimilarityFinder.recursive(w2vModel,word)
        if (!synonyms.isEmpty) {
          synonyms.map{case x=>
            new Replacement(word,x._1,x._2._1,x._2._2,x._2._3,SpellValidator.isValid(x._1),counter.getOrElse(word,0).toInt,counter.getOrElse(x._1,0).toInt).toString
          }
        }else{List.empty}
      }else{List.empty}
    }.saveAsTextFile(OUTPUT_IDENTIFICATION)
  }



  def analyze(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_ANALYSIS'..")
    Folder.moveIfExists(OUTPUT_ANALYSIS)

    println(s"loading replacement text..")
    val input  = sc.wholeTextFiles(ReplacementsBuilder.OUTPUT_IDENTIFICATION).filter(_._1.contains(ReplacementsBuilder.INPUT_FILES)).
      map(_._2).
      flatMap(_.split("\n")).
      map(new Replacement(_)).
      filter(!_.sameWord)

    println(s"detecting duplicated replacements ..")
    input.
      map(x=>(x.token,x)).
      groupByKey.
      filter(x=>x._2.size>1).
      flatMap(_._2).
      map(_.toString).
      saveAsTextFile(OUTPUT_ANALYSIS)

  }


  def fix(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_FIXED'..")
    Folder.moveIfExists(OUTPUT_FIXED)

    println(s"loading replacement text..")
    val input  = sc.wholeTextFiles(ReplacementsBuilder.OUTPUT_IDENTIFICATION).filter(_._1.contains(ReplacementsBuilder.INPUT_FILES)).
      map(_._2).
      flatMap(_.split("\n")).
      map(new Replacement(_)).
      filter(!_.sameWord).
      filter(x=> WordValidator.isValid(x.token) && WordValidator.isValid(x.similar)).
      filter(x=> !SpellValidator.isValid(x.token)).
      map{case x=>
      try{
        val increment     = if (x.spell) 100 else 0
        val fitness       = x.w2vMeasure + x.smithWatermanMeasure + x.jaroWinklerMeasure + increment

        (x.token,(x.similar,fitness))
      }catch {
        case e:Exception =>
          println("ERROR: " + x.toString)
          (x.token,(x.similar.toString,0.0))
      }
    }

    println(s"fixing replacements ..")
    input.
      reduceByKey{case (x,y) => if (x._2 > y._2) x else y}.
      map(x=> new Replacement(x._1,x._2._1).toString).
      saveAsTextFile(OUTPUT_FIXED)

  }


}
