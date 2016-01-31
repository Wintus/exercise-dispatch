package watchman

/**
  * Created by Admin on 2016/01/22.
  */
class AddressBook {
  private val sites = Seq(
    "http://www.google.co.jp",
    "http://wired.com",
    "http://www.nicovideo.jp"
  )
  private var addresses: Set[String] = Set(sites: _*)

  def apply(): Vector[String] = addresses.toVector.sorted

  def validate(address: String): Boolean = true

  def add(address: String) {
    require(validate(address))
    addresses += address
  }

  def remove(address: String) {
    addresses -= address
  }
}
