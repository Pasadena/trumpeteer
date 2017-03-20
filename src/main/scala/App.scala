
import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.pattern.ask
import akka.util.Timeout
import spray.json.DefaultJsonProtocol._

import scala.concurrent.duration._
import scala.concurrent.Future

object App {

  final val ACTOR_SYSTEM_IDENTIFIER =  "trumpeteer"
  final val TWITTER_ACTOR_IDENTIFIER = "twitteractor"
  final val ROUTE_IDENTIFIER = "trumpeteer"

  def main(args: Array[String]): Unit = {

    implicit val actorSystem = ActorSystem(ACTOR_SYSTEM_IDENTIFIER)
    implicit val actorMaterializer = ActorMaterializer()
    implicit val context = actorSystem.dispatcher

    implicit val tweetDataFormat = jsonFormat4(TweetData)
    implicit val responseFormat = jsonFormat1(TweetResponse)

    val routes =
      path(ROUTE_IDENTIFIER) {
        get {
          implicit val timeout: Timeout = 5.seconds

          val twitterActor = actorSystem.actorOf(Props[TwitterActor], TWITTER_ACTOR_IDENTIFIER)

          val response: Future[List[TweetData]] = (twitterActor ? RequestedTweets.FOR_TODAY).mapTo[List[TweetData]]
          complete(response.map(tweets => TweetResponse(tweets)))
        }
      }

    Http().bindAndHandle(routes, "localhost", 8080)
  }
}

final case class TweetData(name: String, userName: String, content: String, tweetTime: String)

final case class TweetResponse(tweets: List[TweetData])

object RequestedTweets extends Enumeration {
  val FOR_TODAY = Value
}

class TwitterActor extends Actor {

  private val tweetService = new TweetService

  def receive = {
    case _ => sender ! tweetService.getTweetsForTheDay
  }
}
