package es.upm.oeg.pnk.validators

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class WordValidatorTest extends FunSuite {


  test("Normal Case") {

    val input   = "SAMPLE"
    val output  = "Sample"
    val result  = WordValidator.normalize(input)

    println("--------- Normal Case Test:")
    println(s"Input: " + input)
    println(s"Output: " + output)
    println(s"Result: " + result)

    assert(result == output)
  }

  test("Special Characters Case") {

    val input   = "REG.DOS"
    val output  = "Reg.Dos"
    val result  = WordValidator.normalize(input)

    println("--------- Special Characters Test:")
    println(s"Input: " + input)
    println(s"Output: " + output)
    println(s"Result: " + result)

    assert(result == output)
  }

  test("Special Characters 2 Case") {

    val input   = "REG.DOS."
    val output  = "Reg.Dos ."
    val result  = WordValidator.normalize(input)

    println("--------- Special Characters 2 Test:")
    println(s"Input: " + input)
    println(s"Output: " + output)
    println(s"Result: " + result)

    assert(result == output)
  }



  test("Capitalize Case") {

    val input   = "Example"
    val output  = "Example"
    val result  = WordValidator.normalize(input)

    println("--------- Capitalize Test:")
    println(s"Input: " + input)
    println(s"Output: " + output)
    println(s"Result: " + result)

    assert(result == output)
  }

  test("Any Upper Case") {

    val input   = "exAmplE"
    val output  = "example"
    val result  = WordValidator.normalize(input)

    println("--------- Any Upper Case Test:")
    println(s"Input: " + input)
    println(s"Output: " + output)
    println(s"Result: " + result)

    assert(result == output)
  }

  test("First Upper Case") {

    val input   = "ExAmplE"
    val output  = "Example"
    val result  = WordValidator.normalize(input)

    println("--------- First Upper Case Test:")
    println(s"Input: " + input)
    println(s"Output: " + output)
    println(s"Result: " + result)

    assert(result == output)
  }


}
