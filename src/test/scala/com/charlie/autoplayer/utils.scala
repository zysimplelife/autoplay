package com.charlie.autoplayer

import java.text.SimpleDateFormat
import java.util.Date
import java.io.File

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 1/8/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
object Utils {
  def getTodayString():String={
    val sf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    val now = sf.format(new Date());
    return  now
  }

  def getConfigFile():File = {
    val userHome = System.getProperty("user.home");
    if (userHome.isEmpty) {
      throw new IllegalStateException("user.home==null");
    }
    val configPath = userHome + File.separator + ".autoplay" + File.separator + "configs.xml";
    new File(configPath);
  }
}
