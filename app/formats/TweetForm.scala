package formats

import play.api.libs.json.{Json, Reads}

/**
  * Tweetテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class TweetForm(
  tweetText: String,
  versionNo: Option[Long]
)

object TweetForm {

  implicit val TweetFormReads: Reads[TweetForm] = Json.reads[TweetForm]
}
