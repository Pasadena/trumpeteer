A small microservice for loading daily tweets from @realDonaldTrump. The service returns all the tweets tweeted during the last 24 hours (at GMT+2). 

This actual implementation is built using [AKKA HTTP](http://doc.akka.io/docs/akka-http/current/index.html)

## USAGE

The following command compiles sources and starts dev server: ```sbt clean compile run```

The Twitter-integration is done with [Twitter4J](http://twitter4j.org/en/). For that one needs a twitter-app (which can be done [here](https://apps.twitter.com/)). After that is done the customes/access -tokens have to be provided to the app. This can be done by creating a conf-file ```twitter-conf.properties``` and placing it to the "src/main/resources" -folder. The conf-file needs to have the following properties: "consumerKey", "consumerSecret", "accessToken" and "accessSecret"
