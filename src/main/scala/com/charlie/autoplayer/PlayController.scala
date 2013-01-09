package com.charlie.autoplayer

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */


import datamodel.Song
import java.io.{FileNotFoundException, File}
import java.lang.InterruptedException
import grizzled.slf4j.Logging

class PlayController extends Logging{
  final val cmdPlayerPath = ClassLoader.getSystemResource("cmdmp3.exe").toURI();

  final def errorCantFound(path: String) = "Can't found mp3 " + path

  final def errorCantFoundPlayer(path: String) = "Can't found cmd mp3 player " + path


  def runPlayer(mp3List: List[Song]): Unit = {
    mp3List.foreach(song => {
      try {
        playAudioFile(song.Path);
      } catch {
        case ex:FileNotFoundException => info("skip audio file " + song.Path);
      }
    })
  }

  /**
   * run 3pp cmdmp3 file with exec.
   * details can be found in
   * http://www.mailsend-online.com/blog/a-command-line-mp3-player-for-windows.html
   * @param file
   */
  def playAudioFile(file: String): Unit = {
    //check mp3 file exist
    info("Going to play audio file " + file)
    val f = new File(file);
    debug("audio file exists:" + f.exists());
    if (!f.exists()) {
      val errorMsg = errorCantFound(f.getAbsolutePath());
      warn(errorMsg)
      throw new FileNotFoundException(errorMsg);
    }

    //check if mp3 player file exist
    val cmdPlayer = new File(cmdPlayerPath);
    if (!cmdPlayer.exists()) {
      val errorMsg = errorCantFound(cmdPlayer.getAbsolutePath());
      throw new FileNotFoundException(errorMsg);
    }

    // try to play this mp3
    val pid = Runtime.getRuntime().exec(cmdPlayer.getAbsolutePath() + " \"" + f.getAbsolutePath() + "\"");
    try {
      pid.waitFor();
    } catch {
      case ex: InterruptedException => //TODO
    }
    pid.exitValue() match {
      case 0 => info("Successfully to play audio file " +  file);
      case _ => info("Exception found when playing audio file " +  file)
    }
    //TODO: need test exit code.
  }
}
