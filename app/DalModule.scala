import slick.driver.JdbcProfile

import scala.concurrent.duration.Duration

object Db {
  val jdbcProfile = DalModule.profile
}

object DalModule {

  private object InitializedDal
  private val waitDuration = Duration(3, "seconds")

  @volatile private var _dal: Dal = _
  def dal = {
    if (_dal == null) {
      InitializedDal.synchronized {
        while(_dal == null) {
          play.api.Logger.warn(s"DAL has not been initialized. Waiting $waitDuration...")
          InitializedDal.wait(waitDuration.toMillis)
        }
      }
    }
    _dal
  }

  @volatile private var _db: JdbcProfile#Backend#Database = _
  def db = {
    if (_db == null) {
      InitializedDal.synchronized {
        while(_db == null) {
          play.api.Logger.warn(s"DB has not been initialized. Waiting $waitDuration...")
          InitializedDal.wait(waitDuration.toMillis)
        }
      }
    }
    _db
  }

  @volatile private var _profile: JdbcProfile = _
  def profile = {
    if (_profile == null) {
      InitializedDal.synchronized {
        while(_profile == null) {
          play.api.Logger.warn(s"JDBC Profile has not been initialized. Waiting $waitDuration...")
          InitializedDal.wait(waitDuration.toMillis)
        }
      }
    }
    _profile
  }

  def init(databaseConfig: DbConnection) = {
    if (_dal == null) {
      play.api.Logger.info("Obtaining locks for DalModule initialization...")
      InitializedDal.synchronized {

        play.api.Logger.info("Initializing DalModule...")
        _profile = databaseConfig.profile
        _dal = new Dal(databaseConfig.profile)
        _db = databaseConfig.dbObject()

        play.api.Logger.info("Initialized DalModule.")
        InitializedDal.notifyAll()
      }
    }
    else {
      play.api.Logger.error("DalModule is already initialized.")
      throw new RuntimeException("DalModule is already initialized.")
    }
  }
}
