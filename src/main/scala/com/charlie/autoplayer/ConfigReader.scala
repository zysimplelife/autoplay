package com.charlie.autoplayer

import com.charlie.autoplay.datamodel.{PlayList, Song}
import java.io.{FileInputStream, FileOutputStream, File}
import java.text.SimpleDateFormat
import xml.NodeSeq
import java.net.URL
import java.util.Date


/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
class ConfigReader() {

  def  PATH_SONG = "song"
  def  PATH_LIST = "playlist"
  def  ATTR_LIST_DATE = "@date"

  /**
   * default construct read config in @USER_HOME/.autoplayer
   */
  var config: scala.xml.Node = _;
  var lists: List[PlayList] = _;

  /**
   * Get a list that should be played today
   */
  def getTodayPlayList() = {
    val result = for (playlist <- lists) yield {
      val sf = new SimpleDateFormat("MM-dd");
      val now = new Date();
      val listTime = sf.format(playlist.Data)
      if (now.equals(listTime)) playlist
    }
    result.toList
  }

  def initConfigs(): File = {
    val userHome: String = getUserHome();
    val configDirectory: File = getOrCreateConfigDirectory(userHome);
    return getOrCreateConfigfile(configDirectory)
  }

  /**
   * reload config context
   * @param file
   */
  def reloadConfig(file: File): Unit = {
    lists = List();
    config = xml.XML.loadFile(file);
    val playLists = config \ PATH_LIST;
    playLists.foreach(item => {
      val dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse((item \ ATTR_LIST_DATE).toString());
      val songs = getSongList(item)
      lists ::= new PlayList(dateTime, songs)
    })
  }

  /**
   * reload config with default config file
   */
  def reloadConfig(): Unit = {
    reloadConfig(initConfigs())
  }

  /**
   * copy init config file from classpath
   * name rule =  filePath = "init" + configName
   * @param file
   */
  private def copyInitConfigFile(file: File) = {
    val initFile = ClassLoader.getSystemResource("init" + file.getName()).getFile();
    val output = new FileOutputStream(file);
    val input = new FileInputStream(initFile)
    output getChannel() transferFrom(input getChannel, 0, Long.MaxValue);
    output.close();
    input.close();
  }

  private def getSongList(listTag: NodeSeq): List[Song] = {
    val songs = listTag \ PATH_SONG;
    val list = for (s <- songs) yield (new Song(s.text.toString()));
    list.toList;
  }

  private def getUserHome(): String = {
    val userHome = System.getProperty("user.home");
    if (userHome.isEmpty) {
      throw new IllegalStateException("user.home==null");
    }
    userHome
  }

  private def getOrCreateConfigDirectory(userHome: String): File = {
    //Create config directory if not existing
    val configDirectory = new File(new File(userHome), ".autoplay");
    if (!configDirectory.exists()) {
      if (!configDirectory.mkdir()) {
        throw new IllegalStateException(configDirectory.toString());
      }
    }
    configDirectory
  }

  private def getOrCreateConfigfile(configDirectory: File): File = {
    //Create config file if not existing
    val configFile = new File(configDirectory, "configs.xml");
    if (!configFile.exists()) {
      if (!configFile.createNewFile()) {
        throw new IllegalStateException(configFile.toString());
      }
      copyInitConfigFile(configFile);
    }
    return configFile.getAbsoluteFile;
  }

}
