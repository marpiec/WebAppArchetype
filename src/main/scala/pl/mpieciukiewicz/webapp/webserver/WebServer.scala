package pl.mpieciukiewicz.webapp.webserver

import java.io.File

import org.eclipse.jetty.server._
import org.eclipse.jetty.server.handler.{RequestLogHandler, ResourceHandler, HandlerList, ContextHandler}
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.util.component.AbstractLifeCycle
import org.eclipse.jetty.util.resource.Resource
import org.scalatra.servlet.ScalatraListener
import org.slf4j.{LoggerFactory, Logger}

class WebServer(port: Int,
                scalatraBootstrapClass: Class[_],
                requestLogIgnorePaths:Array[String]) {

  def start() {

    val server = new Server(port)

    val servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS)
    servletContext.setInitParameter(ScalatraListener.LifeCycleKey, scalatraBootstrapClass.getName)
    servletContext.addEventListener(new ScalatraListener)

    val staticFilesContext: ContextHandler = createStaticFilesContext("/", "/static", "index.html")

    val handlers = new HandlerList()
    handlers.setHandlers(Array(createRequestLogHandler(), servletContext, staticFilesContext))
    server.setHandler(handlers)

    server.start()
    server.join()

  }

  private def createStaticFilesContext(path: String, classpathPath: String, welcomeFile: String): ContextHandler = {
    val resourceHandler = new ResourceHandler()
    if (System.getProperty("webAppDevelopmentMode") == "true") {
      resourceHandler.setBaseResource(Resource.newResource(new File("src/main/resources/static")))
    } else {
      resourceHandler.setBaseResource(Resource.newClassPathResource(classpathPath))
    }

    resourceHandler.setDirectoriesListed(false)
    resourceHandler.setWelcomeFiles(Array(welcomeFile))
    val staticFilesContext = new ContextHandler(path)
    staticFilesContext.setHandler(resourceHandler)
    staticFilesContext
  }

  private def createRequestLogHandler(): RequestLogHandler = {

    val requestLog = new RestRequestLog("restAccessLogger")

//    requestLog.setLoggerName("accessLogger")
//    requestLog.setLogDateFormat("MM/dd HH:mm:ss:SSS")
//    requestLog.setExtended(false)
//    requestLog.setLogLatency(true)
//    requestLog.setIgnorePaths(requestLogIgnorePaths)

    val requestLogHandler = new RequestLogHandler()
    requestLogHandler.setRequestLog(requestLog)
    requestLogHandler
  }

}


class RestRequestLog(loggerName: String) extends AbstractLifeCycle with RequestLog {
  private def logger = LoggerFactory.getLogger(loggerName)

  override def log(request: Request, response: Response) {

    val contentLength = response.getLongContentLength;

    val line = StringBuilder.newBuilder
      .append(request.getRemoteAddr) // remote address
      .append(' ')
      .append(request.getProtocol) // protocol
      .append(' ')
      .append(request.getMethod) // http method
      .append(' ').append('"')
      .append(request.getUri.toString) // request uri
      .append('"').append(' ')
      .append(response.getStatus.toString) // response status
      .append(' ')
      .append((System.currentTimeMillis() - request.getTimeStamp).toString) // response time
      .append('m')
      .append('s')
      .append(' ')

    if(contentLength>=0) { // response size
      line.append(contentLength.toString).append('b')
    } else {
      line.append('-')
    }

    logger.info(line.toString())

  }
}
