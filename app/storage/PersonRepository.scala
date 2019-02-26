package storage

import javax.inject.{Inject, Singleton}
import model.Person
import play.api.db.slick.DatabaseConfigProvider
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonRepository @Inject() (dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import profile.api._

  private class PersonTable(tag: Tag) extends Table[Person](tag, "person") {
    def id = column[Int]("id", O.PrimaryKey, O.AutoInc)

    def name = column[String]("name")

    def age = column[Int]("age")

    def * = (id.?, name, age) <> (Person.tupled, Person.unapply)
  }

  private val personQuery = TableQuery[PersonTable]

  def create(name: String, age: Int): Future[Person] =
    db.run {
      (
        personQuery.map(p => (p.name, p.age))
          returning personQuery.map(_.id)
          into ((nameAge, id) => Person(Some(id), nameAge._1, nameAge._2))
      ) += (name, age)
    }

  def getPerson(id: Int): Future[Option[Person]] =
    db.run {
      personQuery.filter(_.id === id).result.headOption
    }

  def getAll: Future[Seq[Person]] =
    db.run {
      personQuery.result
    }
}
