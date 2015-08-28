package es.upm.oeg.pnk.transformations

import edu.stanford.nlp.ie.crf.CRFClassifier
import edu.stanford.nlp.util.Triple

import scala.collection.{JavaConversions, mutable}

/**
 * Created by cbadenes on 08/08/15.
 */
object Classifier {

  val serializedClassifier  = "classifier/spanish.ancora.distsim.s512.crf.ser.gz";
  val classifier            = CRFClassifier.getClassifier(serializedClassifier);

  def apply (text: String): List[(String,String,Integer, Integer)] = {

    val annotatedContent  : mutable.Buffer[Triple[String, Integer, Integer]]  = JavaConversions.asScalaBuffer(classifier.classifyToCharacterOffsets(text))

    annotatedContent.map(triple=>(triple.first,text.substring(triple.second(),triple.third()),triple.second,triple.third)).toList
  }

  def findAndReplace (text: String): String = {

    val annotatedContent  : mutable.Buffer[Triple[String, Integer, Integer]]  = JavaConversions.asScalaBuffer(classifier.classifyToCharacterOffsets(text))

    annotatedContent.map(triple=>(triple.first,text.substring(triple.second(),triple.third()),triple.second,triple.third)).toList

    var modifiedText = new StringBuffer(text)

    annotatedContent.foreach{ case triple=>
        val start   = triple.second
        val end     = triple.third
        val entity  = text.substring(start,end).replace(" ","_")
        modifiedText = modifiedText.replace(start,end,entity)
    }
    return modifiedText.toString
  }

}
