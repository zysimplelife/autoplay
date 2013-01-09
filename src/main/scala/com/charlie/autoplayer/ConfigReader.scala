package com.charlie.autoplayer

import datamodel.{PlayList, Song}
import java.io.{FileNotFoundException, FileOutputStream, File}
import java.text.SimpleDateFormat
import xml.NodeSeq
import java.util.Date
import grizzled.slf4j.Logging



object Const {
  def PATH_SONG = "song"
  def PATH_LIST = "playlist"
  def PATH_ROOT = "autoplay"
  def ATTR_LIST_DATE = "@date"

  def CONFIG_FILE_NAME = "Configs.xml"
  def CONFIG_FILE_DIR = ".autoplay"
}

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
class ConfigReader() extends Logging {
  /**
   * default construct read config in @USER_HOME/.autoplayer
   */
  var config: scala.xml.Node = _;
  var lists: List[PlayList] = _;
  /**
   * Get a list that should be played today.
   */
  def getTodayPlayList(): List[PlayList] = {
    val sf = new SimpleDateFormat("MM-dd");
    val now = sf.format(new Date());
    val result = lists.filter(l=>{
      val listTime = sf.format(l.Date)
      now.equals(listTime);
    })
    result
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
  def reloadConfig(file: File): ConfigReader = {
    info("reload config file : " + file.getAbsolutePath);
    lists = List();
    config = xml.XML.loadFile(file);
    val playLists = config \ Const.PATH_LIST;
    playLists.foreach(item => {
      val dateTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").
              parse((item \ Const.ATTR_LIST_DATE).toString());
      val songs = getSongList(item)
      lists ::= new PlayList(dateTime, songs)
    })
    return this;
  }

  /**
   * reload config with default config file
   */
  def reloadConfig(): ConfigReader = {
    reloadConfig(initConfigs())
    return this;
  }

  /**
   * copy init config file from classpath
   * name rule =  filePath = "init" + configName
   * @param file
   */
  private def copyInitConfigFile(file: File) = {
    info("Generate config file : " + file.getAbsolutePath);
    try {
      val in = ClassLoader.getSystemResourceAsStream("init" + file.getName());
      val out = new FileOutputStream(file);

      val buffer = new Array[Byte](1024)
      Iterator.continually(in.read(buffer))
        .takeWhile(_ != -1)
        .foreach { out.write(buffer, 0 , _) }

      out.close();
      in.close();
    } catch {
      case ex: FileNotFoundException => //TODO
      case ex: Exception =>  //TODO
    }

  }

  private def getSongList(listTag: NodeSeq): List[Song] = {
    val songs = listTag \ Const.PATH_SONG;
    val list = for (s <- songs) yield (new Song(s.text.toString()));
    list.toList;
  }

  private def getUserHome(): String = {
    val userHome = System.getProperty("user.home");
    if (userHome.isEmpty) {
      info("user.home==null");
      throw new IllegalStateException("user.home==null");
    }
    userHome
  }

  private def getOrCreateConfigDirectory(userHome: String): File = {
    //Create config directory if not existing
    val configDirectory = new File(new File(userHome), Const.CONFIG_FILE_DIR);
    if (!configDirectory.exists()) {
      if (!configDirectory.mkdir()) {
        info("Failed to create config directory");
        throw new IllegalStateException(configDirectory.toString());
      }
    }
    configDirectory
  }

  private def getOrCreateConfigfile(configDirectory: File): File = {
    //Create config file if not existing
    val configFile = new File(configDirectory, Const.CONFIG_FILE_NAME);
    if (!configFile.exists()) {
      if (!configFile.createNewFile()) {
        info("Failed to create config file");
        throw new IllegalStateException(configFile.toString());
      }
      info("Create config file : " + configFile.getAbsolutePath);
      copyInitConfigFile(configFile);
    }
    return configFile.getAbsoluteFile;
  }

}
