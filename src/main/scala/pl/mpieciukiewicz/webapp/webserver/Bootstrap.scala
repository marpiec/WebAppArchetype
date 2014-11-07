package pl.mpieciukiewicz.webapp.webserver

import javax.servlet.ServletContext

import akka.actor.ActorSystem
import org.scalatra.LifeCycle
import pl.mpieciukiewicz.webapp.RestHandler

class Bootstrap extends LifeCycle {

  val actorSystem = ActorSystem("webapp")

  override def init(servletContext: ServletContext) {
    servletContext.mount(new RestHandler(actorSystem), "/")
  }

  override def destroy(context: ServletContext) {
    actorSystem.shutdown()
  }

}
