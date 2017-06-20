package formats

import play.api.libs.json.Reads.maxLength
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}
import utils.Consts

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

  implicit val TweetCommandReads: Reads[TweetUpdateCommand] = (
    (JsPath \ "tweetText").read[String](maxLength[String](Consts.TweetTextMaxLength)) and
    (JsPath \ "versionNo").read[Long]
  ) (TweetUpdateCommand.apply _)
}
