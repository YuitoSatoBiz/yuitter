package formats

import java.time.LocalDateTime
import models.Tables.{Member, Tweet}

import play.api.libs.json._

/**
  * MEMBERテーブルの情報を付加させたTWEETテーブルのケースクラス
  *
  * @author yuito.sato
  */
case class TweetView(
  tweetId: Long,
  memberId: Long,
  tweetText: String,
  registerDatetime: LocalDateTime,
  versionNo: Long,
  memberName: String
)

object TweetView {

  implicit val tweetViewWrites: OWrites[TweetView] = Json.writes[TweetView]

  def from(t: Tweet#TableElementType, m: Member#TableElementType): TweetView =
    TweetView(
      tweetId = t.tweetId,
      memberId = t.memberId,
      tweetText = t.tweetText,
      registerDatetime = t.registerDatetime.toLocalDateTime,
      versionNo = t.versionNo,
      memberName = m.memberName
    )
}
