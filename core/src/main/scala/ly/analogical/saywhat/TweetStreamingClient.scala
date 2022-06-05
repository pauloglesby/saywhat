package ly.analogical.saywhat

import ly.analogical.saywhat.model._

import cats.effect.kernel.Async
import fs2.Stream
import fs2.io.stdout
import fs2.text.lines
import fs2.text.utf8
import io.circe.Json
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.Method._
import org.http4s._
import org.http4s.circe._
import org.http4s.ember.client._
import org.http4s.headers._
import org.http4s.implicits._
import org.typelevel.jawn.Facade
import org.typelevel.jawn.fs2._

class TweetStreamingClient[F[_]: Async](private val token: ApiToken) {

  implicit val f: Facade[Json] = new io.circe.jawn.CirceSupportParser(None, false).facade

  lazy val checkRuleUri: Uri = uri"https://api.twitter.com/2/tweets/search/stream/rules?dry_run=true"
  lazy val postRuleUri: Uri = uri"https://api.twitter.com/2/tweets/search/stream/rules"
  lazy val getRulesUri: Uri = uri"https://api.twitter.com/2/tweets/search/stream/rules"
  lazy val streamTweetsUri: Uri =
    uri"https://api.twitter.com/2/tweets/search/stream?expansions=geo.place_id&place.fields=contained_within,country_code,full_name,place_type"

  lazy val authHeader: Authorization = Authorization(Credentials.Token(AuthScheme.Bearer, token.value))

  lazy val jubileeRuleRaw: RuleRaw =
    RuleRaw(value = "-is:retweet (#jubilee OR jubilee)", tag = "theme:jubilee original-only")
  lazy val addJubileeRule: AddRule = AddRule(List(jubileeRuleRaw))

  def ruleRequest(addRule: AddRule)(uri: Uri): Request[F] = Request[F](
    method = POST,
    uri = uri,
    headers = Headers(authHeader)
  ).withEntity(addRule.asJson)

  def checkRuleRequest(addRule: AddRule): Request[F] = ruleRequest(addRule)(checkRuleUri)

  def addRuleRequest(addRule: AddRule): Request[F] = ruleRequest(addRule)(postRuleUri)

  lazy val getRulesRequest: Request[F] = Request[F](
    method = Method.GET,
    uri = getRulesUri,
    headers = Headers(authHeader)
  )

  lazy val streamTweetsRequest: Request[F] = Request[F](
    method = Method.GET,
    uri = streamTweetsUri,
    headers = Headers(authHeader)
  )

  def jsonStream(req: Request[F]): Stream[F, Json] =
    for {
      client <- Stream.resource(EmberClientBuilder.default[F].build)
      res <- client.stream(req).flatMap(_.body.chunks.parseJsonStream)
    } yield res

  lazy val stream: Stream[F, Unit] = {
    val s = jsonStream(streamTweetsRequest)
    s.map(_.spaces2).through(lines).through(utf8.encode).through(stdout)
  }

  def run: F[Unit] =
    stream.compile.drain
}
