package com.charlie.autoplayer.datamodel

import java.util.Date
import grizzled.slf4j.Logging
/**
 * Created with IntelliJ IDEA.
 * User: Charlie
 * Date: 13-1-5
 * Time: 下午9:11
 * To change this template use File | Settings | File Templates.
 * This class represents for Playlist
 */
class PlayList(date: Date, songs: List[Song]) extends Logging{
  def Date = date;
  def Songs = songs;
  info("Create Play list which will played at :" +  date ) ;
}
