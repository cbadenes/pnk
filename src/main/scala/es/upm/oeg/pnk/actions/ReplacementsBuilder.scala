package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.data.Replacement
import es.upm.oeg.pnk.transformations.{Folder, MapReducer}
import es.upm.oeg.pnk.validators.{SpellValidator, WordValidator}
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.Word2VecModel

/**
 * Created by cbadenes on 05/08/15.
 */
object ReplacementsBuilder {

  val INPUT_FILES             = "part-00"

  def identify(sc: SparkContext, corpusInput: String, w2vInput: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"reading corpus from: $corpusInput..")
    // one document per line
    val documents = sc.
      wholeTextFiles(corpusInput).
      filter(_._1.contains(INPUT_FILES)).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).map(_.replace("\n","")).filter(!_.isEmpty)

    println(s"loading word2vec model to detect similar words from: $w2vInput..")
    val w2vModel    = Word2VecModel.load(sc, w2vInput)

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
    }.saveAsTextFile(output)
  }



  def fix(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"analyzing and fixing replacements from: $input to create: $output..")
    sc.wholeTextFiles(input).filter(_._1.contains(INPUT_FILES)).
      map(_._2).
      flatMap(_.split("\n")).
      map(new Replacement(_)).
      filter(!_.sameWord).
      filter(_.areBothValids).
      filter(!_.areBothCorrectlySpelled).
      map{case r=>
        // based on spelling
        if (r.isTokenCorrectlySpelled && !r.isSimilarWordCorrectlySpelled){
          r.change
        }else if (!r.isTokenCorrectlySpelled && r.isSimilarWordCorrectlySpelled){
          r
        }else{
          // based on frequencies
          if (r.tokenFreq > r.similarFreq){
            r.change
          }else{
            r
          }
        }
      }.
      saveAsTextFile(output)

  }


  def selection(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    val documents = sc.wholeTextFiles(input).filter(_._1.contains(INPUT_FILES)).
      map(_._2).
      flatMap(_.split("\n")).
      map(new Replacement(_)).
      filter(_.accuracy>0.7).           // has minimum accuracy?
      map(x=>(x.token,x)).
      reduceByKey((r1,r2) => if (r1.accuracy > r2.accuracy) r1 else r2).map(_._2).cache

    println(s"resolving replacement cycles from: $input..")
    val replacements = MapReducer(documents.map(x=>(x.token,x.similar)).collectAsMap)


    println(s"selecting the most adequate replacements in: $output ..")
    documents.
      map(x=>x.newSimilar(replacements.getOrElse(x.similar,x.similar))).
      saveAsTextFile(output)

  }


}
