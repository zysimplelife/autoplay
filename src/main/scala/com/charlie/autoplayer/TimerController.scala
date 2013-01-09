package com.charlie.autoplayer

import datamodel.Song
import java.util.{TimerTask, Date, Timer}

/**
 * Created with IntelliJ IDEA.
 * User: Charlie
 * Date: 13-1-4
 * Time: 下午10:30
 * To change this template use File | Settings | File Templates.
 */
class TimerController(reader:ConfigReader) {
  def startSchedule():Unit = {
    reader.getTodayPlayList().foreach(list=>{
      val timer = new Timer();
      class PlayTask extends TimerTask{
        val player = new PlayController();
        def run(){
          player.runPlayer(list.Songs);
          timer.cancel();
        }
      }
      timer.schedule(new PlayTask() ,list.Date)
    })
  }

}
