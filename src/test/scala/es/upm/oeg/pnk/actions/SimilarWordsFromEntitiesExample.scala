package es.upm.oeg.pnk.actions

import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 04/08/15.
 */
object SimilarWordsFromEntitiesExample {

  def main(args: Array[String]): Unit = {

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "8g").
      set("spark.driver.maxResultSize", "4g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    println(s"loading word2vec model to detect similar words..")
    val w2vModel    = Word2VecModel.load(sc, ModelBuilder.OUTPUT_ENTITIES_W2V)

//    val input = List("Marjaliza","David")
//
//    println("##### Recursive Similarity Finder")
//    input.foreach{case word=>
//        println("["+word+"] -> " + SimilarityFinder.recursive(w2vModel,word))
//    }

    w2vModel.findSynonyms("Marjaliza",50).foreach(println(_))

  }


}
