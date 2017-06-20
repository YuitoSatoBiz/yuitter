package formats

import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}
import utils.Consts

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

  implicit val TweetCommandReads: Reads[TweetCreateCommand] = (
    (JsPath \ "tweetText").read[String](maxLength[String](Consts.TweetTextMaxLength)) and
    (JsPath \ "accountIds").read[Seq[Long]]
    ) (TweetCreateCommand.apply _)
}
