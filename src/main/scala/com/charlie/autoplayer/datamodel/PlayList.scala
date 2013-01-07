package com.charlie.autoplayer.com.charlie.autoplay.datamodel

import java.util.Date
import java.sql.Time

/**
 * Created with IntelliJ IDEA.
 * User: Charlie
 * Date: 13-1-5
 * Time: 下午9:11
 * To change this template use File | Settings | File Templates.
 * This class represents for Playlist
 */
class PlayList(date: Date, songs: List[Song]) {
  def Date = date;
  def Songs = songs;
}
