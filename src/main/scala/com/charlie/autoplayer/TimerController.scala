package com.charlie.autoplayer

import java.util.{Date, Timer}

/**
 * Created with IntelliJ IDEA.
 * User: Charlie
 * Date: 13-1-4
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
class TimerController(reader:ConfigReader) {
  val timer = new Timer();
  def startSchedule():Unit = {
    reader.getTodayPlayList().foreach(list=>{
      val schedule = list.Date
      timer.schedule(new PlayContorller(list.Songs),schedule)
    })
  }

}
