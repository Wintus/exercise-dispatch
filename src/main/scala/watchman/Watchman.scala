package watchman

import dispatch.Future

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by Admin on 2016/01/22.
  */
class Watchman {
  private val reporter : Reporter       = new Reporter
  private var addresses: Vector[String] = Vector.empty

  def register(addressBook: AddressBook): Unit = {
    addresses = addressBook()
  }

  def check(): Vector[(Address, Source)] = {
    val futures: Vector[Future[(Address, Source)]] = Retriever(addresses)

    // set callbacks on complete
    futures foreach { future ⇒
      future onSuccess { case (address, source) ⇒
        // show in Markdown-like style
        println(
          s"""#Success: ${address.url} is *alive*!
              |
              |---
              |##Source:
              |${source.html}
              |""".stripMargin)
      }
      future onFailure { case error ⇒ reporter(error) }
    }

    // combine them inside-out
    val sequence: Future[Vector[(Address, Source)]] = Future sequence futures

    // open sesame
    Await.result(sequence, Duration.Inf)
  }
}

/**
  * The Entry-point. To be renamed as Main on use.
  */
object WatchmanMain extends App {
  val watchman: Watchman = new Watchman()
  watchman.register(new AddressBook)
  val results: Vector[(Address, Source)] = watchman.check()
  println(results)
}