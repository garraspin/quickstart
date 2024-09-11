package com.example.log4j

import kamon.Kamon
import kamon.metric.Metric
import kamon.tag.TagSet
import org.apache.logging.log4j.core.appender.AbstractAppender
import org.apache.logging.log4j.core.config.Property
import org.apache.logging.log4j.core.config.plugins.{Plugin, PluginFactory}
import org.apache.logging.log4j.core.{Appender, Core, LogEvent}

@Plugin(
  name = "KamonAppender",
  category = Core.CATEGORY_NAME,
  elementType = Appender.ELEMENT_TYPE,
  printObject = false
)
class KamonAppender(name: String) extends AbstractAppender(name, null, null, true, Property.EMPTY_ARRAY):
  override def append(event: LogEvent): Unit = {
    val tags = TagSet.builder().add("component", "log4j2").add("level", event.getLevel.name()).build()
    KamonAppender.LogEvents.withTags(tags).increment()
    ()
  }
end KamonAppender

object KamonAppender:
  private lazy val LogEvents: Metric.Counter = Kamon.counter(
    name = "log.events",
    description = "Counts the number of log events per level"
  )

  @PluginFactory
  final def createAppender(): KamonAppender = new KamonAppender("kamonAppender")
end KamonAppender

