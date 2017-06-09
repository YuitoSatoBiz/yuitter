package formats

import play.api.libs.json.{Json, Reads}

/**
  * Created by yuito.sato on 2017/06/09.
  */
case class TweetForm(
  tweetId: Option[Long],
  tweetText: String
)

object TweetForm {

  implicit val TweetFormReads: Reads[TweetForm] = Json.reads[TweetForm]
}
