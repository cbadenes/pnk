package es.upm.oeg.pnk.transformations

import java.nio.file.{FileSystems, Files}

/**
 * Created by cbadenes on 08/08/15.
 */
object Folder {

  def moveIfExists( folder: String) ={
    val source = FileSystems.getDefault().getPath(folder)
    println("checking folder " + source.toFile.getAbsolutePath)
    if (Files.exists(source)){
      println("moving existing folder...")
      val target = FileSystems.getDefault().getPath(folder+System.currentTimeMillis())
      Files.move(source,target)
    }
  }


  def moveAndCreate( folder: String) ={
    val source = FileSystems.getDefault().getPath(folder)
    println("checking folder " + source.toFile.getAbsolutePath)
    if (Files.exists(source)){
      println("moving existing folder...")
      val target = FileSystems.getDefault().getPath(folder+System.currentTimeMillis())
      Files.move(source,target)
    }
    Files.createDirectory(source)
  }

}
