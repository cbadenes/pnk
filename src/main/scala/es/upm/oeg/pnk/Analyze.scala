package es.upm.oeg.pnk

import es.upm.oeg.pnk.actions._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 05/08/15.
 */
object Analyze {


  def main(args: Array[String]): Unit = {

    // Spark Configuration
    val conf = new SparkConf().
      setMaster("local[4]").
      setAppName("Local Spark Example").
      set("spark.executor.memory", "48g").
      set("spark.driver.maxResultSize","8g")
    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    // Extract txt from html
    CorpusBuilder.createRaw(sc)

    // Create an initial w2v model from raw text
    ModelBuilder.createRawW2V(sc)

    // Detect similar words
    ReplacementsBuilder.identify(sc)

    // Detect duplicated replacements
    ReplacementsBuilder.analyze(sc)

    // Fix replacements
    ReplacementsBuilder.fix(sc)

    // Create a new corpus with replacements
    CorpusBuilder.makeReplacements(sc)

    // Identify entities
    EntityRecognizer.identifyEntities(sc)


  }

}
