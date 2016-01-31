package mailer

import org.scalatest.FunSuite
import org.scalatest.BeforeAndAfter
import org.jvnet.mock_javamail.Mailbox

/**
  * Sending mails fluently in Scala
  * http://masahito.hatenablog.com/entry/2012/11/24/180456
  * Created by Admin on 2016/01/20.
  */
class MailerTest extends FunSuite with BeforeAndAfter {

  val HOST_NAME = "mail.example.com"
  var mailSender: Mailer = null

  before {
    mailSender = new Mailer(HOST_NAME)
    //clear Mock JavaMail box
    Mailbox.clearAll()
  }

  test("testSendInMockWay") {

    val subject = "Test2"
    val body = "Test Message2"
    mailSender.sendMail(SendMailInfo("to@example.com", "from@example.com", subject, body))

    // check JavaMail box
    val inbox = Mailbox.get("to@example.com")
    assert(inbox.isEmpty === false)
    assert(inbox.size() === 1)
    assert(subject === inbox.get(0).getSubject)
    assert(body === inbox.get(0).getContent)

  }
}
