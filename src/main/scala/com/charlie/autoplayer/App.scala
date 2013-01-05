package com.charlie.autoplayer

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
    val configReader = new ConfigReader();
    configReader.reloadConfig();
  }
}
