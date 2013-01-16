package com.charlie.autoplayer

import java.util.Date
import grizzled.slf4j.Logging

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
object App extends Logging{
  def main(args: Array[String]) {
    info("Application Start#########################")
    val reader = new ConfigReader().reloadConfig();
    val timerc = new TimerController(reader);
    timerc.startSchedule();
  }
}
