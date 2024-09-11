package com.example

import cats.Applicative
import cats.effect.*
import cats.syntax.all.*
import com.comcast.ip4s.*
import fs2.io.net.Network
import io.circe.{Encoder, Json}
import kamon.Kamon
import kamon.http4s.middleware.server.KamonSupport
import org.http4s.circe.jsonEncoderOf
import org.http4s.dsl.Http4sDsl
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.middleware.Logger
import org.http4s.{EntityEncoder, HttpRoutes}
import org.slf4j.LoggerFactory

object Main extends IOApp.Simple:
  Kamon.init()

  val run: IO[Nothing] = runServer[IO]

  private def runServer[F[_] : Async : Network]: F[Nothing] = {
    val host = ipv4"0.0.0.0"
    val port = port"8080"
    val routes = HelloWorld.routes[F](HelloWorld.impl[F])
    val httpApp = KamonSupport(routes, host.toUriString, port.value).orNotFound

    EmberServerBuilder
      .default[F]
      .withHost(host)
      .withPort(port)
      .withHttpApp(httpApp)
      .build
      .useForever
  }
end Main

trait HelloWorld[F[_]]:
  def hello(n: HelloWorld.Name): F[HelloWorld.Greeting]

object HelloWorld:
  private val logger = LoggerFactory.getLogger(HelloWorld.getClass)

  def routes[F[_] : Sync](H: HelloWorld[F]): HttpRoutes[F] =
    val dsl = new Http4sDsl[F] {}
    import dsl.*
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name => H.hello(HelloWorld.Name(name)).flatMap(Ok(_))
    }

  def impl[F[_] : Applicative]: HelloWorld[F] = (n: HelloWorld.Name) =>
    logger.info(s"Saying hi to $n").pure[F] *> Greeting(s"Hello, ${n.name}").pure[F]

  final case class Name(name: String) extends AnyVal

  final case class Greeting(greeting: String) extends AnyVal

  given Encoder[Greeting] = (a: Greeting) => Json.obj(("message", Json.fromString(a.greeting)))

  given [F[_]]: EntityEncoder[F, Greeting] = jsonEncoderOf[F, Greeting]

