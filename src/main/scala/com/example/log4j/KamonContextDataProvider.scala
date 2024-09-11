package com.example.log4j

import kamon.Kamon
import org.apache.logging.log4j.core.util.ContextDataProvider
import java.util.Map as JMap

class KamonContextDataProvider extends ContextDataProvider:
  extension (s: String)
    private def withDefault: String = if (s.nonEmpty) s else "undefined"

  override def supplyContextData(): JMap[String, String] =
    val span = Kamon.currentSpan()
    JMap.of(
      "trace.id", span.trace.id.string.withDefault,
      "span.id", span.id.string.withDefault,
    )
  end supplyContextData
end KamonContextDataProvider
