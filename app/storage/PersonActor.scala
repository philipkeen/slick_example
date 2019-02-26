package storage

import akka.actor.{Actor, Props}
import javax.inject.Inject
import model.Person
import play.api.Logger

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

class PersonActor @Inject() (personRepository: PersonRepository)(implicit executionContext: ExecutionContext) extends Actor {
  import PersonActor._

  private val logger = Logger("application")

  var store: scala.collection.mutable.Map[Int, Person] = scala.collection.mutable.HashMap.empty

  override def receive: Receive = {
    case FetchPerson(id) =>
      logger.debug(s"Received request for person with ID $id, returning ${store.get(id)}")
      sender() ! store.get(id)
    case FetchAll =>
      logger.debug(s"Returning all ${store.size} people")
      sender() ! store.values.toSeq
    case ReloadFromDatabase =>
      logger.debug("Refreshing person store from database")
      personRepository.getAll
          .onComplete {
            case Success(personSeq) =>
              store = scala.collection.mutable.HashMap.empty
              personSeq foreach { person =>
                person.id match {
                  case Some(i) => store += i -> person
                  case None => ()
                }
              }
            case Failure(error) =>
              logger.error(s"Refresh of person cache failed with $error")
          }
    case _ => ()
  }

}

object PersonActor {

  def props = Props[PersonActor]

  sealed trait Command
  final case class FetchPerson(id: Int) extends Command
  case object FetchAll extends Command
  case object ReloadFromDatabase extends Command

}
