package formats

import play.api.libs.json.{Json, Reads}

/**
  * Created by yuito.sato on 2017/06/09.
  */
case class TweetForm(
  tweetText: String,
  versionNo: Option[Long]
)

object TweetForm {

  implicit val TweetFormReads: Reads[TweetForm] = Json.reads[TweetForm]
}
