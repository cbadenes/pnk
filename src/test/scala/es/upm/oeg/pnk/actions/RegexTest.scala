package es.upm.oeg.pnk.actions

import org.apache.commons.lang.StringUtils
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner


/**
 * Created by cbadenes on 08/08/15.
 */
@RunWith(classOf[JUnitRunner])
class RegexTest extends FunSuite {



  test("Match Basic Text") {

//    val input = "movies near 80301"
    val input = "movies 80301"
//    val input = "80301 movies"
//    val input = "movie: 80301"
//    val input = "movies: 80301"
//    val input = "movies near boulder, co"
//    val input = "movies near boulder, colorado"


    // match "movies 80301"
    val moviesZipRE = "movies (\\d{5})".r

    // match "movies near boulder, co"
    val moviesNearCityStateRE = "movies near ([a-z]+), ([a-z]{2})".r

    input match {
      case moviesZipRE(zip) => println(zip)
      case moviesNearCityStateRE(city, state) => println(city, state)
      case _ => println("did not match a regex")
    }

  }

  test("Match Text File") {

    val input = "(file:/Users/cbadenes/Projects/lda-pnk/src/main/resources/corpus/filtrala.org/punica/tomo1/index.html,- . . Juzgado Central De Instruccion No Madrid . , Numero . Rgrai.N . . . 22.0 . 1 . A L , . Dil)"

    // match "movies 80301"
    val filePath = "(.*file:.*/index.html),(.*)".r

    input match {
      case filePath(file,text) => {
        println("File: " + file)
        println("Text: " + text)
      }
      case _ => println("did not match a regex")
    }

  }

  test("Complex Text File") {
    val input = "(file:/Users/cbadenes/Projects/lda-pnk/src/main/resources/corpus/filtrala.org/punica/tomo1/index.html,- . . Juzgado Central De Instruccion No Madrid . , Numero . Rgrai.N . . . 22.0 . 1 . A L , . Diligencias Previas Ocuradores Apoyo de la Fiscalia contra la Corru de fecha 28/02/2014 , con r.e . y se encabezar6 , cqe t imonio de la presente diligencia . Day . ccia1 Cc,$t 3 . .)\n(file:/Users/cbadenes/Projects/lda-pnk/src/main/resources/corpus/filtrala.org/punica/tomo11/index.html,ano Xi . , . 6 . Juzgado Central De Instruccion Madrid . Número u.~5 u Rgtro . . . Gral \\ \\ I I / , -)"

    val files = StringUtils.substringsBetween(input,"(file:","/index.html,")

    files.foreach(x=>println("FILE: "+ x))

  }

}
