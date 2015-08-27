package es.upm.oeg.pnk.topics

import es.upm.oeg.epnoi.matching.metrics.domain.entity.{ConceptualResource, RegularResource}
import es.upm.oeg.epnoi.matching.metrics.domain.space.{ConceptsSpace, TopicsSpace}
import es.upm.oeg.epnoi.matching.metrics.topics.LDASettings
import org.apache.spark.rdd.RDD

/**
 * Created by cbadenes on 03/08/15.
 */
object CreateTopicModel {

  def apply (corpus: RDD[RegularResource], topics: Integer, alpha: Double, beta: Double, maxIt: Integer): (ConceptsSpace,TopicsSpace)= {

    // LDA Settings
    LDASettings.setTopics(topics);
    LDASettings.setAlpha(alpha);
    LDASettings.setBeta(beta);
    LDASettings.setMaxIterations(maxIt);

    // Conceptual Resources
    println("creating conceptual resources..")
    val conceptualResources = corpus.map(ConceptualResource(_))

    // Conceptual Space
    println("creating a conceptual space..")
    val conceptualSpace = new ConceptsSpace(conceptualResources)

    println("creating the topics space ..")
    val topicSpace: TopicsSpace = new TopicsSpace(conceptualSpace)

    return (conceptualSpace,topicSpace)

  }


}
