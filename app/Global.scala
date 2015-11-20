import play.api._
import slick.driver.MySQLDriver

object Global extends GlobalSettings {

  override def onStart(app: Application) {

    DalModule.init(new DbConnection(MySQLDriver))

    Logger.info("Application has started.")
  }
}
