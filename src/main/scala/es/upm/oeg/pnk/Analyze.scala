package es.upm.oeg.pnk

import es.upm.oeg.pnk.actions._
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 05/08/15.
 */
object Analyze {

  val corpus              = "corpus/filtrala.org/punica/tomo*/"

  val corpus_txt          = "output/corpus/txt"
  val corpus_fixed        = "output/corpus/fixed"
  val corpus_tagged       = "output/corpus/tagged"
  val corpus_optimized    = "output/corpus/optimized"
  
  val w2v_raw             = "output/model/w2v/raw"
  val w2v_ent             = "output/model/w2v/entities"
  val w2v_optimized       = "output/model/w2v/optimized"

  val repl_raw            = "output/replacements/raw/identification"
  val repl_raw_fix        = "output/replacements/raw/fix"
  val repl_raw_sel        = "output/replacements/raw/selection"

  val repl_ent            = "output/replacements/entities/identification"
  val repl_ent_fix        = "output/replacements/entities/fix"
  val repl_ent_sel        = "output/replacements/entities/selection"

  val entities_raw        = "output/entities/raw"
  val entities_optimized  = "output/entities/optimized"


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
    CorpusBuilder.create(sc,corpus,corpus_txt)

    // Create an initial w2v model from raw text
    ModelBuilder.createW2V(sc,corpus_txt,w2v_raw)

    // Detect similar words
    ReplacementsBuilder.identify(sc,corpus_txt, w2v_raw, repl_raw)

    // Fix duplicated and invalid replacements
    ReplacementsBuilder.fix(sc,repl_raw,repl_raw_fix)

    // Fix replacements
    ReplacementsBuilder.selection(sc,repl_raw_fix,repl_raw_sel)

    // Create a new corpus with replacements
    CorpusBuilder.replace(sc,corpus_txt,repl_raw_sel,corpus_fixed)

    // Identify entities
    EntityRecognizer.identify(sc,corpus_fixed,entities_raw)

    // Update corpus with entities
    CorpusBuilder.tag(sc,corpus_fixed,corpus_tagged)

    // Create a w2v model using this corpus
    ModelBuilder.createW2V(sc,corpus_tagged,w2v_ent)

    // -> Second Iteration
    //Detect similar words using entities
    ReplacementsBuilder.identify(sc, corpus_tagged, w2v_ent, repl_ent)

    // Fix duplicated and invalid replacements
    ReplacementsBuilder.fix(sc,repl_ent,repl_ent_fix)

    // Fix replacements
    ReplacementsBuilder.selection(sc,repl_ent_fix,repl_ent_sel)

    // Create a new corpus with these replacements
    CorpusBuilder.replace(sc,corpus_fixed,repl_ent_sel,corpus_optimized)

    // Identify entities
    EntityRecognizer.identify(sc,corpus_optimized,entities_optimized)

    // Create a w2v model using this corpus
    ModelBuilder.createW2V(sc,corpus_optimized,w2v_optimized)

  }

}
