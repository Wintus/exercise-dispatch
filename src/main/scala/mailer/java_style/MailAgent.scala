package mailer.java_style

import java.util.{Date, Properties}
import javax.mail._
import javax.mail.internet._

/**
  * http://alvinalexander.com/source-code/scala/scala-send-email-class-uses-javamail-api
  * Created by Admin on 2016/01/20.
  */
class MailAgent(to: String,
                cc: String,
                bcc: String,
                from: String,
                subject: String,
                content: String,
                smtpHost: String) {
  var message: Message = null

  message = createMessage
  message.setFrom(new InternetAddress(from))
  setToCcBccRecipients()

  message.setSentDate(new Date())
  message.setSubject(subject)
  message.setText(content)

  // throws MessagingException
  def sendMessage() {
    Transport.send(message)
  }

  def createMessage: Message = {
    val properties = new Properties()
    properties.put("mail.smtp.host", smtpHost)
    val session = Session.getDefaultInstance(properties, null)
    new MimeMessage(session)
  }

  // throws AddressException, MessagingException
  def setToCcBccRecipients() {
    setMessageRecipients(to, Message.RecipientType.TO)
    Option(cc) foreach {
      setMessageRecipients(_, Message.RecipientType.CC)
    }
    Option(bcc) foreach {
      setMessageRecipients(_, Message.RecipientType.BCC)
    }
  }

  // throws AddressException, MessagingException
  def setMessageRecipients(recipient: String, recipientType: Message.RecipientType) {
    // had to do the asInstanceOf[...] call here to make scala happy
    val addressArray = buildInternetAddressArray(recipient).asInstanceOf[Array[Address]]
    if ((addressArray != null) && (addressArray.length > 0)) {
      message.setRecipients(recipientType, addressArray)
    }
  }

  // throws AddressException
  def buildInternetAddressArray(address: String): Array[InternetAddress] = {
    // could test for a null or blank String but I'm letting parse just throw an exception
    InternetAddress.parse(address)
  }

}
