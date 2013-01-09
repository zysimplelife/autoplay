package com.charlie.autoplayer

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import org.scalatest.{BeforeAndAfterEachFunctions, FunSuite}
import java.io.FileNotFoundException

/**
 * Created with IntelliJ IDEA.
 * User: ezhayog
 * Date: 1/9/13
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 */
@RunWith(classOf[JUnitRunner])
class TestPlayController extends FunSuite with BeforeAndAfterEachFunctions{
  var player:PlayController = _;

  beforeEach {
    player = new PlayController();
  }

  test("Try to play music immediately") {
    val nomp3 = "C:\\Users\\Public\\Music\\Sample Music\\notfound.mp3"
    val thrown = intercept[FileNotFoundException] {
    player.playAudioFile(nomp3);
    }
    assert(thrown.getMessage == "Can't found mp3 "+nomp3);
  }
}
