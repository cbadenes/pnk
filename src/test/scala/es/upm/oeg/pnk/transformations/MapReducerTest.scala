package es.upm.oeg.pnk.transformations

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class MapReducerTest extends FunSuite {



  test("Map Basic") {

    val input     : Map[String,String] = Map("A" -> "B", "B" -> "C","C" -> "D", "E" -> "F")
    val expected  : Map[String,String] = Map("A" -> "D", "B" -> "D","C" -> "D", "E" -> "F")
    val obtained = MapReducer(input)

    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }



}
