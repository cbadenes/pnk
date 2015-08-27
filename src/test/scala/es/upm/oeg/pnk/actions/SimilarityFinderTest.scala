package es.upm.oeg.pnk.actions

import java.io.FileInputStream

import opennlp.tools.postag.{POSModel, POSTaggerME}
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.simmetrics.StringMetrics


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class SimilarityFinderTest extends FunSuite {



  test("Similar words") {

    val input = List(("Ibercaja","Iberca]a"),("Ibercaja","Ibercpja"),("Ibercaja","Iberia"))

    input.foreach{case x=>
        println(s"SmithWaterman: ["+x._1+","+x._2+"] = " + StringMetrics.smithWaterman.compare(x._1, x._2))
        println(s"JaroWinkler: ["+x._1+","+x._2+"] = " + StringMetrics.jaroWinkler.compare(x._1, x._2))
    }

  }

}
