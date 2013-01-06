package com.charlie.autoplayer

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEachFunctions, BeforeAndAfter, FunSuite}
import java.util
import java.io.File
import xml.transform.{RuleTransformer, RewriteRule}
import xml.{NodeSeq, Text, Elem, Node}

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
     var config = getConfigFile();
     // delete configure file before init
     if(config.exists()){
       assert(config.delete());
     }
    reader.initConfigs();
    config = getConfigFile();
    assert(config.exists());
    config.deleteOnExit();
  }

  test("if the config file is existed,it won't rewrite it by default value") {
    reader.initConfigs();
    var config = getConfigFile();
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
    val lists = node \\ new ConfigReader().PATH_LIST
    lists.foreach(l =>{
      if ((l \ "@date").text == "2099-01-01 00:00:00") return false
    })
    return true;
  }

  private def getConfigFile():File = {
    val userHome = System.getProperty("user.home");
    if (userHome.isEmpty) {
      throw new IllegalStateException("user.home==null");
    }
    val configPath = userHome + File.separator + ".autoplay" + File.separator + "configs.xml";
    new File(configPath);
  }


}
