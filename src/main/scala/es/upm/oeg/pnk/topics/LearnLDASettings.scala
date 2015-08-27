package es.upm.oeg.pnk.topics

import es.upm.oeg.epnoi.matching.metrics.domain.entity.{ConceptualResource, RegularResource}
import es.upm.oeg.epnoi.matching.metrics.domain.space.ConceptsSpace
import es.upm.oeg.epnoi.matching.metrics.topics.LDASettings
import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Created by cbadenes on 03/08/15.
 */
object LearnLDASettings {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().
      setMaster("local[6]").
      setAppName("Local Spark Example")
    println("Spark Configuration: " + conf.getExecutorEnv)

    val sc = new SparkContext(conf)

    Logger.getRootLogger.setLevel(Level.WARN)

    val corpus = sc.objectFile[RegularResource]("src/test/resources/corpus/serialized")

    val maxIt = 11
    for( a <- 1 until maxIt){
      println("####>  Iteration " + a + " of " + maxIt)

      // Conceptual Resources
      println("creating conceptual resources..")
      val conceptualResources = corpus.map(ConceptualResource(_))

      // Conceptual Space
      println("creating a conceptual space..")
      val conceptualSpace = new ConceptsSpace(conceptualResources)

      // OPTIMIZATION: Search best parameters (topics, alpha and beta) for LDA process
      val maxEvaluations = 200
      val ldaIterations  = 100
      val solution = LDASettings.learn(conceptualSpace.featureVectors, maxEvaluations, ldaIterations)

      println(s"LDA Solution: Iteration: " + a + " of " + maxIt)
      println("- maxGeneticIterations: \t" + maxEvaluations)
      println("- maxLDAIterations: \t" + ldaIterations)
      println("- alpha: \t" + solution.getAlpha)
      println("- beta: \t" + solution.getBeta)
      println("- topics: \t" + solution.getTopics)
      println("- logLikelihood: \t" + solution.getLoglikelihood)
      println("- logPrior: \t" + solution.getLogPrior)
    }


  }


}
