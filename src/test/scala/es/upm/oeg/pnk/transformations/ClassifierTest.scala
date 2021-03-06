package es.upm.oeg.pnk.transformations

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class ClassifierTest extends FunSuite {


  test("Entity Recognize text") {
    val input   : String  = "w . Juzgado Central Instruccion . Fecha . : . Of.Reg! Rep Dos.Cent. Instruccion . Hora . Madrid , Madrid , . Usuario Pet: . ju7500 . Penal . Usuarlo Reg: . ju7500"
    val expected  : String  = ""
    val obtained  : List[(String, String, Integer, Integer)]  = Classifier(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: ")
    obtained.foreach { case entity =>
      println(s"$entity")
    }
  }

  test("Entity Recognize and Replace text") {
    val input   : String  = "w . Juzgado Central Instruccion . Fecha . : . Of.Reg! Rep Dos.Cent. Instruccion . Hora . Madrid , Madrid , . Usuario Pet: . ju7500 . Penal . Usuarlo Reg: . ju7500"
    val expected  : String  = "w . Juzgado Central_Instruccion . Fecha . : . Of.Reg! Rep_Dos.Cent. Instruccion . Hora . Madrid , Madrid , . Usuario Pet: . ju7500 . Penal . Usuarlo_Reg: . ju7500"
    val obtained  : String  = Classifier.findAndReplace(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: " + obtained)

    assert(expected.equals(obtained))
  }
}
