package es.upm.oeg.pnk.actions

import es.upm.oeg.pnk.Analyze
import org.apache.log4j.{Level, Logger}
import org.apache.spark.mllib.feature.Word2VecModel
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 04/08/15.
 */
object SimilarWordsExample {

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
    val w2vModel    = Word2VecModel.load(sc, Analyze.w2v_raw)

    val input = List("Marjaliza","Imarjaliza","Iimarjaliza","Mapjaliza","Mar]aliza","Marjaiiza")

    println("##### Direct Similarity Finder")
    input.foreach{case word=>
      println("["+word+"] -> " + SimilarityFinder(w2vModel,word))
    }

    println("##### Recursive Similarity Finder")
    input.foreach{case word=>
        println("["+word+"] -> " + SimilarityFinder.recursive(w2vModel,word))
    }


    println("##Synonyms of Mapjaliza")
    w2vModel.findSynonyms("Mapjaliza",20).foreach(println(_))

    println("##Synonyms of legitimamente")
    w2vModel.findSynonyms("legitimamente",50).foreach(println(_))

  }


}
