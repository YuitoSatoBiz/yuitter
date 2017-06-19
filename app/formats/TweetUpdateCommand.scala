package formats

import play.api.libs.json.{Json, Reads}

/**
  * Tweetテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class TweetUpdateCommand(
  tweetText: String,
  versionNo: Long
)

object TweetUpdateCommand {

  implicit val TweetCommandReads: Reads[TweetUpdateCommand] = Json.reads[TweetUpdateCommand]
}
