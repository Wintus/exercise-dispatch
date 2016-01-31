package dispatch

import dispatch.Defaults._
import org.scalatest.FunSpec
import org.scalatest.Matchers._

/**
  * Created by Admin on 2016/01/20.
  */
class DispatchHttpSpec extends FunSpec {

  val sites = Seq(
    "wired.jp",
    "www.google.co.jp",
    "www.nicovideo.jp"
  )

  describe("Check'em all alive by sending HTTP Requests, and Send error report on failure 1") {
    import mail._

    sites foreach { address ⇒
      it(s"Check $address alive") {
        val request: Req = host(address)
        val html: Future[String] = Http(request OK as.String)

        // set callbacks on complete
        html onSuccess { case source ⇒
          println(
            s"""Success: $address is alive!
                |
                |Source:
                |$source""".stripMargin
          )
        }
        val sendANewMail: PartialFunction[Throwable, Unit] = {
          case e ⇒
            send a new Mail(
              from = "dummy@email.com" → "Watcher",
              to = "localhost",
              subject = s"Website $address no response",
              message =
                s"""No response with Error:
                    |${e.getCause}
                    |
                    |Massage:
                    |${e.getMessage}
                    |
                    |Stacktrace:
                    |${e.getStackTrace}
              """.stripMargin
            )
        }
        html onFailure sendANewMail

        html() should include regex "html|HTML".r // on success
      }
    }
  }

  describe("Check'em all alive by sending HTTP Requests, and Send error report on failure 2") {
    import mailer._

    sites foreach { address ⇒
      it(s"Check $address alive") {
        val request: Req = host(address)
        val http: Future[String] = Http(request OK as.String)

        // set callbacks on complete
        http onSuccess { case html ⇒
          println(
            s"""Success: $address is alive!
                |
                |Source:
                |$html""".stripMargin
          )
        }
        val sendByAMailer: PartialFunction[Throwable, Unit] = {
          case e ⇒
            val HOST_NAME = "mail.example.com"
            val mailSender = new Mailer(HOST_NAME)
            val subject = s"Website $address no response"
            val body =
              s"""No response with Error:
                  |${e.getCause}
                  |
                  |Massage:
                  |${e.getMessage}
                  |
                  |Stacktrace:
                  |${e.getStackTrace}
              """.stripMargin
            mailSender sendMail SendMailInfo("from@service.com", "to@service.com", subject, body)
        }
        http onFailure sendByAMailer

        http() should include regex "DOCTYPE (html|HTML)".r // on success
      }
    }
  }
}
