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
import java.util.TimerTask

class PlayContorller(mp3List: List[Song]) extends TimerTask{
  final val cmdPlayerPath = ClassLoader.getSystemResource("cmdmp3.exe").toURI();

  final def errorCantFound(path: String) = "Can't found mp3 " + path

  final def errorCantFoundPlayer(path: String) = "Can't found cmd mp3 player " + path

  def MP3List = mp3List;

  private def runPlayer(mp3List: List[Song]): Unit = {
    mp3List.foreach(song => {
      println(song.Path)
      try {
        runPlayer(song.Path);
      } catch {
        case ex: FileNotFoundException => println(ex.getMessage);
      }
    })
  }

  def run(){
    runPlayer(MP3List);
    this.cancel();
  }

  /**
   * run 3pp cmdmp3 file with exec.
   * details can be found in
   * http://www.mailsend-online.com/blog/a-command-line-mp3-player-for-windows.html
   * @param file
   */
  def runPlayer(file: String): Unit = {
    //check mp3 file exist
    val f = new File(file);
    if (!f.exists())
      throw new FileNotFoundException(errorCantFound(f.getAbsolutePath()));

    //check mp3 player file exist
    val cmdPlayer = new File(cmdPlayerPath);
    if (!cmdPlayer.exists())
      throw new FileNotFoundException(errorCantFound(cmdPlayer.getAbsolutePath()));

    // try to play this mp3
    val pid = Runtime.getRuntime().exec(cmdPlayer.getAbsolutePath() + " \"" + f.getAbsolutePath() + "\"");
    try {
      pid.waitFor();
    } catch {
      case ex: InterruptedException => println("interrupted") //TODO
    }
    println(pid.exitValue());
    //TODO: need test exit code.
  }
}
