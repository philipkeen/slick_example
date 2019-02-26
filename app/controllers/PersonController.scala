package controllers

import javax.inject.{Inject, Singleton}
import model.Person
import play.api.libs.json.{Format, Json}
import play.api.mvc.{AbstractController, ControllerComponents}
import storage.PersonStore

import scala.concurrent.ExecutionContext

@Singleton
class PersonController @Inject()(
  personStore: PersonStore,
  cc: ControllerComponents
)(
  implicit executionContext: ExecutionContext) extends AbstractController(cc) {

  implicit val FORMAT: Format[Person] = Json.format[Person]

  def getPerson(id: Int) = Action.async { request =>
    personStore.getPerson(id) map {
      case Some(person) => Ok(Json.toJson(person))
      case None => NotFound
    }
  }

  def getAllPeople() = Action.async { request =>
    personStore.getAllPeople
      .map { peopleList =>
        Ok(Json.toJson(peopleList))
      }
  }
}
