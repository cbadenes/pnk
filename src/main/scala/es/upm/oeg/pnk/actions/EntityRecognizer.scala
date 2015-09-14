package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.transformations.{Classifier, Folder}
import es.upm.oeg.pnk.validators.WordValidator
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.Word2Vec

/**
 * Created by cbadenes on 05/08/15.
 */
object EntityRecognizer {


  def identify(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"identifying entities from corpus: $input ..")
    // one document per line
    sc.
      wholeTextFiles(input).
      filter(_._1.contains("part-00")).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).
      map(_.replace("\n","")).
      filter(!_.isEmpty).
      flatMap(line => Classifier(line)).
      saveAsTextFile(output)

  }

  def distinct(sc: SparkContext, input: String, output: String, byType: String): Unit = {

    val outputFolder = output+"-"+byType

    println(s"creating or moving output folder '$outputFolder'..")
    Folder.moveIfExists(outputFolder)

    println(s"reading entities from: $input ..")
    // one document per line
    sc.
      textFile(input+"/part-00000").
      map(x=>x.split(",")).
      filter(x=>x(0).contains(byType)).
      map(x=>(x(1),1)).
      reduceByKey((x,y)=>x+y).
      filter(x=>x._1.length >3 && x._2>10).
      sortByKey(true).
      map(_._1).
      saveAsTextFile(outputFolder)

  }


  

}
