package com.charlie.autoplayer

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 10:36 AM
 * To change this template use File | Settings | File Templates.
 */

import java.io.{FileNotFoundException, File}
import java.lang.InterruptedException

class PlayContorller {
  final val cmdPlayerPath = ClassLoader.getSystemResource("cmdmp3.exe").toURI();
  final def errorCantFound(path:String) = "Can't found mp3 " + path
  final def errorCantFoundPlayer(path:String) = "Can't found cmd mp3 player " + path

  def runCmdPlayerList(mp3List : List[String])={
    mp3List.foreach(mp3Path => {
      try{
       runCmdPlayerMp3(mp3Path);
      } catch {
        case ex:FileNotFoundException => println(ex.getMessage);
      }
    })
  }

  /**
   * run 3pp cmdmp3 file with exec.
   * details can be found in
   * http://www.mailsend-online.com/blog/a-command-line-mp3-player-for-windows.html
   * @param file
   */
  def runCmdPlayerMp3 (file :String){
    //check mp3 file exist
    val f = new File(file);
    if (!f.exists())
      throw new FileNotFoundException(errorCantFound(f.getAbsolutePath()));

    //check mp3 player file exist
    val cmdPlayer = new File(cmdPlayerPath);
    if (!cmdPlayer.exists())
      throw new FileNotFoundException(errorCantFound(cmdPlayer.getAbsolutePath()));

    // try to play this mp3
    val pid = Runtime.getRuntime().exec( cmdPlayer.getAbsolutePath() + " \"" + f.getAbsolutePath() + "\"");
    try {
      pid.waitFor();
    } catch {
      case ex:InterruptedException =>  //TODO
    }

    //TODO: need test exit code.
  }
}
