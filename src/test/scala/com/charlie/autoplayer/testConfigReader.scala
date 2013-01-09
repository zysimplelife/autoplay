package com.charlie.autoplayer

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEachFunctions, BeforeAndAfter, FunSuite}
import java.util
import java.io.File
import util.Date
import xml.transform.{RuleTransformer, RewriteRule}
import xml.{NodeSeq, Text, Elem, Node}
import java.text.SimpleDateFormat

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 1/6/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class TestConfigReader extends FunSuite with BeforeAndAfterEachFunctions  {
  var reader:ConfigReader = _;

  beforeEach {
    reader = new ConfigReader();
  }

  test("Create Initial xml file if it is not existed") {
     var config = Utils.getConfigFile();
     // delete configure file before init
     if(config.exists()){
       assert(config.delete());
     }
    reader.initConfigs();
    config = Utils.getConfigFile();
    assert(config.exists());
    config.deleteOnExit();
  }

  test("if the config file is existed,it won't rewrite it by default value") {
    reader.initConfigs();
    var config = Utils.getConfigFile();
    if(!config.exists()){
      assert(false);
    }

    //modify config xml
    var configXml =  xml.XML.loadFile(config);
    val newConfigXml = modifyConfig(configXml);
    scala.xml.XML.save(config.getAbsolutePath(),newConfigXml.asInstanceOf[Node]);

    //compare
    reader.initConfigs();
    if(!config.exists()){
      assert(false);
    }
    configXml =  xml.XML.loadFile(config);
    assert(isModifySuccessfully(configXml));
    config.deleteOnExit();
  }


  test("Get today play list,if the config is correct,then getTodayPlaylist will get at list one element") {
    reader.initConfigs();
    var config = Utils.getConfigFile();
    var configXml =  xml.XML.loadFile(config);
    val newConfigXml = modifytoTodayConfig(configXml);
    scala.xml.XML.save(config.getAbsolutePath(),newConfigXml.last);
    reader.reloadConfig();
    val todaylist = reader.getTodayPlayList();
    println(todaylist.last.Date)
    assert(!todaylist.isEmpty);
    assert(todaylist.last.Songs.length == 2)
    config.delete();
  }

  test("Get emplty today play list,if the config is correct,then getTodayPlaylist will emplty playlist") {
    reader.reloadConfig();
    var config = Utils.getConfigFile();
    val todaylist = reader.getTodayPlayList();
    assert(todaylist.isEmpty);
    config.delete();
  }


  /**************************************************************/
  /*   Utils Functions                                          */
  /**************************************************************/
  private def modifyConfig(node:Node) = {
    object t extends RewriteRule {
      override def transform(n: Node): Seq[Node] = n match {
        case e:Elem if ((e \ "@date").text == "2099-01-01 00:00:00") => NodeSeq.Empty
        case other => other
      }
    }
    new RuleTransformer(t).transform(node)
  }


  private def isModifySuccessfully(node:Node):Boolean = {
    val lists = node \\ Const.PATH_LIST
    lists.foreach(l =>{
      if ((l \ "@date").text == "2099-01-01 00:00:00") return false
    })
    return true;
  }

  private def modifytoTodayConfig(node:Node) = {
    object t extends RewriteRule {
      override def transform(n: Node): Seq[Node] = n match {
        case e : Elem if (e.label == Const.PATH_ROOT) => {
          val node = <playlist date={Utils.getTodayString()}>
                        <song>C:\Users\Public\Music\Sample Music\Amanda.wma</song>
                        <song>C:\Users\Public\Music\Sample Music\Amanda.wma</song>
                      </playlist>;
          val newChild = node map { case e:Elem => e.copy(label="playlist") }
          e.copy(child = newChild)
        }
        case _ => n
      }
    }
    new RuleTransformer(t).transform(node)
  }

}
