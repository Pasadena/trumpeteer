import java.text.SimpleDateFormat
import java.time.{LocalDate, ZoneId}
import java.util.{Properties, TimeZone}

import twitter4j.conf.ConfigurationBuilder
import twitter4j.{Status, TwitterFactory}

class TweetService {

  import TweetService.twitterFactory

  import scala.collection.JavaConverters.asScalaBuffer

  private val twitterInstance = twitterFactory.getInstance

  def getTweetsForTheDay: List[TweetData] = {
    val timelineForUser = asScalaBuffer(twitterInstance.getUserTimeline("@realDonaldTrump"))

    timelineForUser.filter(status => isTweetedToday(status))
      .map(status => statusToData(status))
      .sortBy(_.tweetTime)
      .toList
  }

  def statusToData(status: Status): TweetData =
    TweetData(status.getUser.getName, status.getUser.getScreenName, status.getText, status.getCreatedAt.toString)

  def isTweetedToday(status: Status): Boolean = {
    val userTimezone = TimeZone.getTimeZone("UTC+02:00")
    val now = LocalDate.now(ZoneId.of(userTimezone.getID))
    val dateFormat = new SimpleDateFormat("yyy-MM-dd")
    val tweetData = LocalDate.parse(dateFormat.format(status.getCreatedAt))
    System.out.println(now)
    System.out.println(now.minusDays(1))
    tweetData.isAfter(now.minusDays(1))
    //now.getDayOfYear == tweetData.getDayOfYear && now.getYear == tweetData.getYear
  }

}

object TweetService {

  import scala.io.Source

  private val twitterFactory = createInstanceFactory

  private def readProperties = {
    val properties = new Properties()
    val source = Source.fromResource("twitter-conf.properties")
    properties.load(source.bufferedReader())
    source.close()
    properties
  }

  private def createInstanceFactory = {
    val properties = readProperties
    val configurationBuilder = new ConfigurationBuilder()
    configurationBuilder.setDebugEnabled(true)
      .setOAuthConsumerKey(properties.getProperty("consumerKey"))
      .setOAuthConsumerSecret(properties.getProperty("consumerSecret"))
      .setOAuthAccessToken(properties.getProperty("accessToken"))
      .setOAuthAccessTokenSecret(properties.getProperty("accessSecret"))
    new TwitterFactory(configurationBuilder.build)
  }

}
