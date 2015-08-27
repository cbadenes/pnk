package es.upm.oeg.pnk.transformations

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class EntityRecognizerTest extends FunSuite {


  test("Entity Recognize text") {


    val input   : String  = "w . Juzgado Central Instruccion . Fecha . : . Of.Reg! Rep Dos.Cent. Instruccion . Hora . Madrid , Madrid , . Usuario Pet: . ju7500 . Penal . Usuarlo Reg: . ju7500"
    val expected  : String  = ""
    val obtained  : Map[String, List[(String,Integer,Integer)]]  = EntityRecognizer(input)


    println(s"Input: " + input)
    println(s"Expected: " + expected)
    println(s"Obtained: ")
    obtained.keys.foreach { case key =>
      println(s"$key=>")
      val entities = obtained.getOrElse(key, List.empty)
      entities.foreach(entity=>println(s"- [$entity]"))
    }
  }
}
