package es.upm.oeg.pnk.transformations

import scala.collection.Map
import util.control.Breaks._

/**
 * Created by cbadenes on 08/08/15.
 */
object MapReducer {

  def apply (input: Map[String, String]): Map[String,String] = {
    var linked = true
    var table = input
    while(linked){
      linked = false
      val keys = table.keys
      keys.foreach{ key =>
        breakable {
          if (!table.contains(key)) break
          val value = table.get(key).get
          if (table.contains(value)){
            val linkedValue = table.get(value).get
            if (value.equals(linkedValue)){
              table = table - value
            } else{
              linked = true
              table += key -> table.get(value).get
            }
          }
        }
      }
    }
    return table
  }

}
