package formats

import play.api.libs.json.{Json, Reads}

/**
  * Tweetテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class TweetCreateCommand(
  tweetText: String,
  accountIds: Seq[Long]
)

object TweetCreateCommand {

  implicit val TweetCommandReads: Reads[TweetCreateCommand] = Json.reads[TweetCreateCommand]
}
