package es.upm.oeg.pnk.transformations

import scala.collection.Map

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
        val value = table.get(key).get
        if (table.contains(value)){
          linked = true
          table += key -> table.get(value).get
        }
      }
    }
    return table
  }

}
