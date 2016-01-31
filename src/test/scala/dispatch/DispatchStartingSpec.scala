package dispatch

import java.util.concurrent.ExecutionException

import dispatch.Defaults._
import org.scalatest.FunSpec
import org.scalatest.Matchers._
import org.scalatest.concurrent.ScalaFutures

import scala.util.Failure


/**
  * http://dispatch.databinder.net/
  * http://d.hatena.ne.jp/Kazuhira/20140126/1390726214
  * Created by Admin on 2016/01/20.
  */
class DispatchStartingSpec extends FunSpec {
  describe("dispatch starting spec") {
    it("Access Google Top") {
      val request = url("https://www.google.co.jp/")
      val googleTop = Http(request OK as.String)
      googleTop() should include("<title>Google</title>")
    }

    it("Path Access Google Top") {
      val request = host("www.google.co.jp").secure
      val googleTop = Http(request OK as.String)
      googleTop() should include("<title>Google</title>")
    }

    it("Path Access niconico Top") {
      val request = host("www.nicovideo.jp")
      val nicoTop = Http(request OK as.String)
      nicoTop() should include("niconico")
    }

    it("Path Access niconico Ranking") {
      val request = host("www.nicovideo.jp") / "ranking"
      val nicoTop = Http(request OK as.String)
      nicoTop() should include("niconico")
    }

    it("Path Access not-exists 0") {
      val request = host("not-exists.com")
      val notExists = Http(request OK as.String)
      notExists onSuccess { case result ⇒ }
      notExists onFailure { case e ⇒ e.getCause }
      ScalaFutures.whenReady(notExists.failed) { e ⇒
        e shouldBe a[ExecutionException]
      }
    }

    it("Path Access not-exists 1") {
      val request = host("not-exists.com")
      val notExists = Http(request OK as.String)
      notExists onSuccess { case result ⇒ }
      notExists onFailure { case e ⇒ e.getCause }
      intercept[ExecutionException] {
        notExists() shouldBe a[Failure[_]]
      }
    }

  }
}
