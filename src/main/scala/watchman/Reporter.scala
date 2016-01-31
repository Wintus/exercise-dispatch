package watchman

/**
  * Created by Admin on 2016/01/22.
  */
class Reporter {
  private val email         : String = "reporter@no.such.domain.com"
  private val reportingEmail: String = "supervisor@no.such.domain.com"

  def apply(error: Throwable): Unit = {
    report(error)
  }

  import mail.{Mail, send}

  def report(error: Throwable): Unit = {
    send a new Mail(
      from = email → this.toString,
      to = Seq(reportingEmail),
      subject = "Website no response",
      message =
        s"""No response with Error:
            |${error.getCause}
            |
            |Massage:
            |${error.getMessage}
            |
            |Stacktrace:
            |${error.getStackTrace}
              """.stripMargin
    )
  }

}
