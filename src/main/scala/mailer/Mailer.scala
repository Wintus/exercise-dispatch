package mailer

import org.apache.commons.mail.SimpleEmail

/**
  * http://masahito.hatenablog.com/entry/2012/11/24/180456
  * Created by Admin on 2016/01/20.
  */
class Mailer(sendHostName: String, charSet: String = "UTF-8") {

  def sendMail(mailInfo: SendMailInfo) {

    new SimpleEmail {
      setCharset(charSet)
      setHostName(sendHostName)
      setFrom(mailInfo.from)
      addTo(mailInfo.to)
      setSubject(mailInfo.subject)
      setMsg(mailInfo.msg)
    }.send
  }
}

case class SendMailInfo(to: String, from: String, subject: String, msg: String)
