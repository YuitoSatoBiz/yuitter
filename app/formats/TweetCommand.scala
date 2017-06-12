package formats

import play.api.libs.json.{Json, Reads}

/**
  * Tweetテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class TweetCommand(
  tweetText: String,
  versionNo: Option[Long]
)

object TweetCommand {

  implicit val TweetCommandReads: Reads[TweetCommand] = Json.reads[TweetCommand]
}
