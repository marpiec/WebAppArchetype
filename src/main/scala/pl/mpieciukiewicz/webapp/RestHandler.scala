package pl.mpieciukiewicz.webapp

import akka.actor.ActorSystem
import pl.mpieciukiewicz.webapp.actors.HelloActor
import pl.mpieciukiewicz.webapp.actors.HelloActor.SayHello
import pl.mpieciukiewicz.webapp.webserver.EnhancedScalatraServlet

class RestHandler(actorSystem: ActorSystem) extends EnhancedScalatraServlet(actorSystem) {

  implicit val as = actorSystem

  get("/ping") {
    "pong"
  }

  get("/say-hello/:name") {
    async {
      val actor = HelloActor.getActor
      val msg = SayHello(params("name"))
      actor.askForJson(msg)
    }
  }
}
