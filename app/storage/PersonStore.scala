package storage

import akka.actor.{ActorRef, ActorSystem}
import akka.pattern.ask
import akka.util.Timeout
import javax.inject.{Inject, Named, Singleton}
import model.Person
import play.api.Configuration
import storage.PersonActor.{FetchAll, FetchPerson, ReloadFromDatabase}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class PersonStore @Inject() (
  actorSystem: ActorSystem,
  @Named("person-actor") personActor: ActorRef,
  configuration: Configuration
)(
  implicit executionContext: ExecutionContext
) {

  implicit val timeout: Timeout = 5 seconds

  actorSystem.scheduler.schedule(
    initialDelay = 0 seconds,
    interval = configuration.get[Int]("app.personStore.refresh") seconds,
    receiver = personActor,
    message = ReloadFromDatabase
  )

  def getPerson(id: Int): Future[Option[Person]] =
    (personActor ? FetchPerson(id)).mapTo[Option[Person]]

  def getAllPeople(): Future[Seq[Person]] =
    (personActor ? FetchAll).mapTo[Seq[Person]]
}
