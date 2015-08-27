package es.upm.oeg.pnk.actions

import java.io.{FileInputStream, File}

import opennlp.tools.postag.{POSModel, POSTaggerME}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class POSTaggingTest extends FunSuite {



  test("POS Tagging text") {

    //val inputStream = new FileInputStream("src/main/resources/pos/opennlp-es-maxent-pos-es.bin")
//    val inputStream = new FileInputStream("src/main/resources/pos/opennlp-es-maxent-pos-universal.bin")
//    val inputStream = new FileInputStream("src/main/resources/pos/opennlp-es-perceptron-pos-es.bin")
    val inputStream = new FileInputStream("src/main/resources/pos/opennlp-es-perceptron-pos-universal.bin")

    val model   = new POSModel(inputStream)
    val tagger  = new POSTaggerME(model)

    val input   = "Inmuebles,Muebles,Inmuobles,Inmuebtes,Inmuebie,Inmuebies,Inmueblo,Inmuoble,inmuebles:,Inmueblos,inmuebtes,Inmuebte,Inmueble"
    val words   = input.split(",").toArray[String]

    val tags    = tagger.tag(words)
    val probs   = tagger.probs()

    for (i <- 0 to words.length-1){
      println("Word: " + words(i) + "\t| tag: " + tags(i) + "\t| prob: " + probs(i))
    }



  }

}
