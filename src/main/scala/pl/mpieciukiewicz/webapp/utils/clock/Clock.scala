package pl.mpieciukiewicz.webapp.utils.clock

import org.joda.time.{Instant, DateTime}

/**
 *
 */
trait Clock {

  def now:DateTime

  def instantNow:Instant

}
