package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.data.Replacement
import es.upm.oeg.pnk.transformations.Folder
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * Created by cbadenes on 05/08/15.
 */
object CorpusBuilder {

  val INPUT_FOLDER    = "corpus/filtrala.org/punica/tomo*/"

  val OUTPUT_RAW      = "output/corpus/raw"

  val OUTPUT_REPLACED = "output/corpus/replaced"

  def createRaw(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_RAW'..")
    Folder.moveIfExists(OUTPUT_RAW)

    println(s"reading documents..")
    val input   : RDD[(String, String)]  = sc.
      wholeTextFiles(INPUT_FOLDER).
      filter((x) => x._1.endsWith("index.html"))

    println(s"extracting text from documents..")
    val texts  : RDD[(String, String)]  = Parser(input)

    // save output
    texts.saveAsTextFile(OUTPUT_RAW)

  }

  def makeReplacements(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_REPLACED'..")
    Folder.moveIfExists(OUTPUT_REPLACED)

    println(s"reading replacements ..")
    val replacements  = sc.
      wholeTextFiles(ReplacementsBuilder.OUTPUT_FIXED).
      filter(_._1.contains(ReplacementsBuilder.INPUT_FILES)).map(x=>x._2).
      flatMap(line=>line.split("\n")).
      map(new Replacement(_)).
      map(r=>(r.token,r.similar)).
      collectAsMap

    println(s"reading raw corpus ..")
    // one document per line
    val input : RDD[String] = sc.
      wholeTextFiles(CorpusBuilder.OUTPUT_RAW).
      filter(_._1.contains("part-00")).
      map(_._2).cache

    val files = input.map{case line=>
      val r = """\\(file:.*/index.html,""".r
      line match {
        case r(group) => group
        case _ => ""
      }
    }
    val words = input.
      flatMap(line=>line.split("\\(file:.*/index.html,")).
      map(_.replace("\n","")).
      filter(!_.isEmpty).
      map(line => line.split(" ").map(word=>replacements.getOrElse(word,word))).
      map(x=>x mkString(" "))

    println(s"replacing raw corpus ..")
    files zip words saveAsTextFile(OUTPUT_REPLACED)

  }

}