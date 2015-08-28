package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.data.Replacement
import es.upm.oeg.pnk.transformations.{Classifier, Folder}
import es.upm.oeg.pnk.validators.WordValidator
import org.apache.commons.lang.StringUtils
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD

/**
 * Created by cbadenes on 05/08/15.
 */
object CorpusBuilder {

  def create(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"extracting text from documents..")
    val texts  : RDD[(String, String)]  = Parser(sc.
      wholeTextFiles(input).
      filter((x) => x._1.endsWith("index.html")))

    texts.saveAsTextFile(output)

  }

  def replace(sc: SparkContext, corpusInput: String, replacementsInput: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"reading replacements from: $replacementsInput..")
    val replacements  = sc.
      wholeTextFiles(replacementsInput).
      filter(_._1.contains(ReplacementsBuilder.INPUT_FILES)).
      map(_._2).
      flatMap(line=>line.split("\n")).
      map(new Replacement(_)).
      map(r=>(r.token,r.similar)).
      collectAsMap

    println(s"reading corpus from: $corpusInput..")
    // one document per line
    val input : RDD[String] = sc.
      wholeTextFiles(corpusInput).
      filter(_._1.contains(ReplacementsBuilder.INPUT_FILES)).
      map(_._2).cache

    val files = input.
      flatMap(line=>StringUtils.substringsBetween(line,"(file:","/index.html,")).map(path=>s"file:$path/index.html")

    val words = input.
      flatMap(line=>line.split("\\(file:.*/index.html,")).
      map(_.replace("\n","")).
      filter(!_.isEmpty).
      map(line => line.split(" ").filter(WordValidator.isMinimal).map(word=>replacements.getOrElse(word,word))).
      map(x=>x mkString(" "))

    println(s"replacing corpus to: $output..")
    files zip words saveAsTextFile(output)

  }

  def tag (sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"identifying and setting entities from corpus: $input to create the new corpus: $output..")
    // one document per line
    sc.
      wholeTextFiles(input).
      filter(_._1.contains(ReplacementsBuilder.INPUT_FILES)).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).
      map(_.replace("\n","")).
      filter(!_.isEmpty).
      flatMap(line => Classifier.findAndReplace(line)).
      saveAsTextFile(output)




  }


}
