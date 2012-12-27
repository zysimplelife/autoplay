package com.charlie.autoplayer

import com.charlie.autoplayer.PlayContorller

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
object App {
  def main(args: Array[String]) {
    val player = new PlayContorller();
    player.runCmdPlayerMp3(testmp3);

  }
}
