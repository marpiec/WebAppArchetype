package pl.mpieciukiewicz.webapp.webserver

import akka.actor.{ActorRef, ActorSystem}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import org.scalatra.{FutureSupport, ScalatraServlet, AsyncResult}
import pl.mpieciukiewicz.webapp.utils.json.JsonUtil

import scala.concurrent.{Future, ExecutionContext}

abstract class EnhancedScalatraServlet(actorSystem: ActorSystem) extends ScalatraServlet with FutureSupport {

  val jsonUtil = new JsonUtil
  import jsonUtil._

  implicit val defaultTimeout = Timeout(30 seconds)

  before() {
    response.setHeader("Cache-control", "no-cache")
  }

  implicit class JsonEnabledActor(actor:ActorRef) {
    def askForJson(msg: Any):Future[String] = {
      actor.ask(msg).map(toJson)
    }
  }

  def async(block: => Future[_]):AsyncResult = {
    new AsyncResult {
      val is = block
    }
  }

  override protected implicit def executor: ExecutionContext = actorSystem.dispatcher
}
