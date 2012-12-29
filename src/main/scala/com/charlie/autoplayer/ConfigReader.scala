package com.charlie.autoplayer

import java.io.{FileInputStream, FileOutputStream, File}

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 12/27/12
 * Time: 3:09 PM
 * To change this template use File | Settings | File Templates.
 */
class ConfigReader() {
  /**
   * default construct read config in @USER_HOME/.autoplayer
   */
  val config = xml.XML.loadFile(initConfigs());
  val songs = config \ "playlist" \ "song"

  def initConfigs():File={
    val userHome: String = getUserHome();
    val configDirectory: File = getOrCreateConfigDirectory(userHome);
    return getOrCreateConfigfile(configDirectory)
  }
  /**
   * copy init config file from classpath
   * name rule =  filePath = "init" + configName
   * @param file
   */
  def copyInitConfigFile(file:File)={
      val initFile = ClassLoader.getSystemResource("init"+file.getName()).getFile();
      new FileOutputStream(file) getChannel() transferFrom(
         new FileInputStream(initFile) getChannel, 0, Long.MaxValue );
  }

  def getList(): List[String] = {
    val list = for (s <- songs) yield s.text.toString();
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
