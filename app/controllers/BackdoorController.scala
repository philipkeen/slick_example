package controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.mvc.{AbstractController, ControllerComponents}
import storage.Backdoor

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class BackdoorController @Inject() (
  backdoor: Backdoor,
  cc: ControllerComponents
)(
  implicit executionContext: ExecutionContext
) extends AbstractController(cc) {

  case class PersonData(name: String, age: Int)

  private implicit val format: Format[PersonData] = Json.format[PersonData]

  private def validateJson[A: Reads] = parse.json.validate(
    _.validate[A].asEither.left.map(e => BadRequest(JsError.toJson(e)))
  )

  def createPerson = Action.async(validateJson[PersonData]) { request =>
    backdoor.create(request.body.name, request.body.age)
      .map { person =>
        person.id match {
          case Some(i) => Ok(s"Created person with ID $i")
          case None => Ok
        }
      }
  }
}
