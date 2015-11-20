import com.typesafe.config.ConfigFactory
import play.api.Logger
import slick.driver.JdbcProfile

class DbConnection(val profile: JdbcProfile) {

  import profile.api._

  def dbObject(): Database = {
    val config = ConfigFactory.load("application")
    val url = config.getString(s"db.default.url")
    val username = config.getString(s"db.default.user")
    val password = config.getString(s"db.default.pass")
    val driver = config.getString(s"db.default.driver")

    Logger.info(s"Database configuration: url=$url driver=$driver")
    Database.forURL(url, username, password, null, driver)
  }
}
