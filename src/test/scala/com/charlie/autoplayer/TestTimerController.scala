package com.charlie.autoplayer

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEachFunctions, FunSuite}
import xml.{Elem, Node}
import xml.transform.{RuleTransformer, RewriteRule}


/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 1/6/13
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class TestTimerController extends FunSuite with BeforeAndAfterEachFunctions  {
  val reader = new ConfigReader()

  beforeEach {

  }

  test("Try to play music immediately") {
    reader.initConfigs();

    val config = Utils.getConfigFile();
    val configXml =  xml.XML.loadFile(config);
    val newConfigXml = modifytoTodayConfig(configXml);
    scala.xml.XML.save(config.getAbsolutePath(),newConfigXml.last);

    reader.reloadConfig();
    val timerc = new TimerController(reader);
    println(reader.getTodayPlayList().last.Date);
    timerc.startSchedule();

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
