package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.transformations.Folder
import es.upm.oeg.pnk.validators.WordValidator
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.Word2Vec

/**
 * Created by cbadenes on 05/08/15.
 */
object ModelBuilder {

  def createW2V(sc: SparkContext, input: String, output: String): Unit = {

    println(s"creating or moving output folder '$output'..")
    Folder.moveIfExists(output)

    println(s"reading corpus from $input..")
    // one document per line
    val documents = sc.
      wholeTextFiles(input).
      filter(_._1.contains("part-00")).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).map(_.replace("\n","")).filter(!_.isEmpty)

    println(s"creating word2vec model in: $output..")
    val w2vAux = new Word2Vec()
    w2vAux.setVectorSize(500) // vector dimension (default 100)
    val corpus = documents.map(line=>line.split(" ").filter(WordValidator.isRelevant).toSeq)
    val w2vSimModel = w2vAux.fit(corpus)

    // save output
    w2vSimModel.save(sc,output)

  }

}
