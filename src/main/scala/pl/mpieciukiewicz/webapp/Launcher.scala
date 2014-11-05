package pl.mpieciukiewicz.webapp

import pl.mpieciukiewicz.webapp.webserver.{Bootstrap, WebServer}

object Launcher {

  def main(args: Array[String]) {

    val config = loadConfiguration

    val webServer = new WebServer(
      8080,
      classOf[Bootstrap],
      Array("/lib/*", "/app/*", "/favicon.ico"))

    webServer.start()

  }


  def loadConfiguration: Null = {
    null
  }





}
