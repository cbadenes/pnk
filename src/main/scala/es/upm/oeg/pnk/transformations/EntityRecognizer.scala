package es.upm.oeg.pnk.transformations

import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.util.Triple

import scala.collection.{JavaConversions, mutable}

/**
 * Created by cbadenes on 08/08/15.
 */
object EntityRecognizer {

  val serializedClassifier = "classifier/spanish.ancora.distsim.s512.crf.ser.gz";
  val classifier = CRFClassifier.getClassifier(serializedClassifier);

  def apply (text: String): Map[String,List[(String,Integer, Integer)]] = {

    val annotatedContent  : mutable.Buffer[Triple[String, Integer, Integer]] = JavaConversions.asScalaBuffer(classifier.classifyToCharacterOffsets(text))

    var result : mutable.Map[String,List[(String,Integer, Integer)]] = mutable.Map.empty[String,List[(String,Integer, Integer)]]

    annotatedContent.foreach{case triple =>
      val entityType  : String                            = triple.first()
      val entity      : String                            = text.substring(triple.second(),triple.third())
      val item        : (String,Integer,Integer)          = (entity,triple.second(),triple.third())
      val entities    : List[(String,Integer, Integer)]   = result.getOrElse(entityType, List.empty[(String,Integer, Integer)])

      if (!entities.contains(entity)) result += (entityType -> (item :: entities))
    }
    return result.toMap
  }


}
