package ly.analogical.saywhat

import cats.effect.IO
import org.http4s.ember.client._

object Stub {
  lazy val client: IO[Unit] = EmberClientBuilder.default[IO].build.use { _ =>
    // use `client` here and return an `IO`.
    // the client will be acquired and shut down
    // automatically each time the `IO` is run.
    IO.unit
  }
}
