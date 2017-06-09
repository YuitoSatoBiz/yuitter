package formats

import play.api.libs.json._
import play.api.libs.functional.syntax._

/**
  * MEMBERテーブルの情報を付加させたTWEETテーブルのケースクラス
  *
  * @author yuito.sato
  */
case class TweetWithMemberView(
  tweetId: Long,
  memberId: Long,
  tweetText: String,
  registerDatetime: java.sql.Timestamp,
  memberName: String
)

object TweetWithMemberView {

  implicit val tweetWithMemberViewWrites: Writes[TweetWithMemberView] = (
    (__ \ "tweetId").write[Long] and
    (__ \ "memberId").write[Long] and
    (__ \ "tweetText").write[String] and
    (__ \ "registerDatetime").write[java.sql.Timestamp] and
    (__ \ "memberName").write[String]
    ) (unlift(TweetWithMemberView.unapply))
}
