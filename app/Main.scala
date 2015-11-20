import slick.driver.{JdbcProfile, MySQLDriver}

import scala.concurrent.{Future, Await}
import scala.concurrent.duration.Duration

case class Tactic(id: Long, name: String)

trait TacticComponent {
  import Db.jdbcProfile.api._

  class TacticTable(tag: Tag) extends Table[Tactic](tag, "tactics") {
    def id = column[Long]("id", O.PrimaryKey)
    def name = column[String]("name")

    def * = (id, name) <> ((Tactic.apply _).tupled, Tactic.unapply)
  }

  val tacticTableQuery = TableQuery[TacticTable]
}

class Dal(val jdbcProfile: JdbcProfile)
  extends TacticComponent

object Main {

  def initDb() = {
    val profile: JdbcProfile = MySQLDriver
    DalModule.init(new DbConnection(profile))
  }

  def main(args: Array[String]) {

    initDb()
    val dal = DalModule.dal
    import dal._
    val db = DalModule.db
    import Db.jdbcProfile.api._

    val results = List.newBuilder[String]

    val query: Query[TacticTable, Tactic, Seq] = tacticTableQuery.take(1)
    val action = query.result
    val futureResult: Future[Option[Tactic]] = db.run(action.headOption)

    results ++= action.statements
    results += Await.result(futureResult, Duration(1, "seconds")).toString

    println(results.result().mkString("\n\n"))
  }
}
