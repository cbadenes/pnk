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


  def main(args: Array[String]): Unit = {
    val input = "Nombre Ifrancisco Jose Illrena de to dedaracion L!i1 Ca Importante: Si Ia cantidad consignar on to casilla ha sido deierrninada come consecuancia de Ia curnpiimoniacion del apartado Ode Ia página do Ia declaración Solicitud do suspension del ingreso do on c6nyuga!renuncia del otro cónyuge at cobro de Is devolución indique marcando con una X asia casilla Esie apartado so cumplimentaro exciusivamenie an caso do declaración complementaria del ejercicio de la que se derive una cantidad a ingresar , is Do Resuitado do to declaración complementarla I• I Importante: on las declaraciones complcntenlarias no podra fraccionarse at pago on dos piazo . o V o at Is a o E Si at Importe consignar on Ia cosilia as una cantldad positiva , indique a continuaoon , marcando con una •x Ia casulla correspondiente , Si desea o no lraccionar ci paqo on dos piazo y consignar on Ia casilla I ci importe quo vaya a ungresar: Ia lotatidad , vi no fracciona ci page ."
    println(Classifier.findAndReplace(input))
  }


}
