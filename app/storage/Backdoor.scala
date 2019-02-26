package storage

import javax.inject.{Inject, Singleton}
import model.Person

import scala.concurrent.Future

@Singleton
class Backdoor @Inject()(personRepository: PersonRepository) {

  def create(name: String, age: Int): Future[Person] =
    personRepository.create(name, age)
}
