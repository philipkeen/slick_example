package modules

import com.google.inject.AbstractModule
import play.api.libs.concurrent.AkkaGuiceSupport

import storage.PersonActor

class AppModule extends AbstractModule with AkkaGuiceSupport {
  override def configure = {
    bindActor[PersonActor]("person-actor")
  }
}
