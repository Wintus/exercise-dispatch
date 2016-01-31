package watchman

import dispatch.Defaults._
import dispatch._

/**
  * Created by Admin on 2016/01/22.
  */
object Retriever {
  def apply(addresses: Vector[String], isSecure: Boolean = false): Vector[Future[(Address, Source)]] = {
    retrieve(addresses map Address)
  }

  def retrieve(addresses: Vector[Address], isSecure: Boolean = false): Vector[Future[(Address, Source)]] = {
    addresses map { address ⇒
      val request = if (isSecure) url(address.url).secure else host(address.url)
      val html: Future[String] = Http(request OK as.String)
      // not yet set callbacks on complete
      html map { source ⇒ (address, Source(source)) }
    }
  }
}

case class Address(url: String)

case class Source(html: String)