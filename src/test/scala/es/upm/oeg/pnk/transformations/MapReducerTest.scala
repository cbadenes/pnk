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


  test("Cycle") {

    val input     : Map[String,String] = Map("A" -> "B", "B" -> "C","C" -> "D", "D" -> "A")
    val expected  : Map[String,String] = Map("B" -> "A", "C" -> "A","D" -> "A")
    val obtained = MapReducer(input)

    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }

  test("Cycle and others") {

    val input     : Map[String,String] = Map("A" -> "B", "B" -> "C","C" -> "D", "D" -> "A", "G" -> "H", "I" -> "J")
    val expected  : Map[String,String] = Map("B" -> "A", "C" -> "A","D" -> "A", "G" -> "H", "I" -> "J")
    val obtained = MapReducer(input)

    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }

  test("Two Cycle") {

    val input     : Map[String,String] = Map("A" -> "B", "B" -> "C","C" -> "D", "D" -> "A", "E" -> "F", "F" -> "G", "G" -> "E")
    val expected  : Map[String,String] = Map("B" -> "A", "C" -> "A","D" -> "A", "E" -> "G", "F" -> "G" )
    val obtained = MapReducer(input)

    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }


}
