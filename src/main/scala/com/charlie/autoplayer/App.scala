package com.charlie.autoplayer

import java.util.Date

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
object App {
  def main(args: Array[String]) {
    val reader = new ConfigReader().reloadConfig();
    val timerc = new TimerController(reader);
    reader.getTodayPlayList().filter(l=>{l.isInstanceOf[datamodel.PlayList]}).foreach(l=>{
      println("Now=" + new Date() + " Schedule=" + l.Date);
    })
    timerc.startSchedule();
    println("Done")
  }
}
