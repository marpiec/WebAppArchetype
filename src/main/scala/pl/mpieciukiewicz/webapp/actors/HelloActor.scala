package pl.mpieciukiewicz.webapp.actors

import akka.actor.{Props, ActorSystem, Actor}
import pl.mpieciukiewicz.webapp.actors.HelloActor.{HelloSaid, SayHello}

object HelloActor {
  case class SayHello(name: String)
  case class HelloSaid(name: String)

  def getActor(implicit actorSystem: ActorSystem) = actorSystem.actorOf(Props(classOf[HelloActor]))
}

class HelloActor extends Actor {
  override def receive: Receive = {
    case SayHello(name) => sender ! HelloSaid(name)
    case _ => ???
  }
}
