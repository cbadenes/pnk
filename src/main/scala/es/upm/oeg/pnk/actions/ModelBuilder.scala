package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.transformations.Folder
import es.upm.oeg.pnk.validators.WordValidator
import org.apache.spark.SparkContext
import org.apache.spark.mllib.feature.Word2Vec

/**
 * Created by cbadenes on 05/08/15.
 */
object ModelBuilder {

  val OUTPUT_RAW_W2V = "output/model/w2v-raw"

  val OUTPUT_ENTITIES_W2V = "output/model/w2v-entities"

  def createRawW2V(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_RAW_W2V'..")
    Folder.moveIfExists(OUTPUT_RAW_W2V)

    println(s"reading raw corpus ..")
    // one document per line
    val documents = sc.
      wholeTextFiles(CorpusBuilder.OUTPUT_RAW).
      filter(_._1.contains("part-00")).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).map(_.replace("\n","")).filter(!_.isEmpty)

    println(s"creating word2vec model from raw corpus..")
    val w2vAux = new Word2Vec()
    w2vAux.setVectorSize(500) // vector dimension (default 100)
    val corpus = documents.map(line=>line.split(" ").filter(WordValidator.isRelevant).toSeq)
    val w2vSimModel = w2vAux.fit(corpus)

    // save output
    w2vSimModel.save(sc,OUTPUT_RAW_W2V)

  }

  def createEntitiesW2V(sc: SparkContext): Unit = {

    println(s"creating or moving output folder '$OUTPUT_ENTITIES_W2V'..")
    Folder.moveIfExists(OUTPUT_ENTITIES_W2V)

    println(s"reading corpus with entities..")
    // one document per line
    val documents = sc.
      wholeTextFiles(CorpusBuilder.OUTPUT_ENTITIES).
      filter(_._1.contains("part-00")).
      map(_._2).
      flatMap(line=>line.split("\\(file:.*/index.html,")).map(_.replace("\n","")).filter(!_.isEmpty)

    println(s"creating word2vec model from corpus with entities..")
    val w2vAux = new Word2Vec()
    w2vAux.setVectorSize(500) // vector dimension (default 100)
    val corpus = documents.map(line=>line.split(" ").filter(WordValidator.isRelevant).toSeq)
    val w2vSimModel = w2vAux.fit(corpus)

    // save output
    w2vSimModel.save(sc,OUTPUT_ENTITIES_W2V)

  }
  

}
