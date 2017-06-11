package formats

import java.time.LocalDateTime
import models.Tables.{Member, Tweet}

import play.api.libs.json._

/**
  * MEMBERテーブルの情報を付加させたTWEETテーブルのケースクラス
  *
  * @author yuito.sato
  */
case class TweetWithMemberView(
  tweetId: Long,
  memberId: Long,
  tweetText: String,
  registerDatetime: LocalDateTime,
  versionNo: Long,
  memberName: String
)

object TweetWithMemberView {

  implicit val tweetWithMemberViewWrites: OWrites[TweetWithMemberView] = Json.writes[TweetWithMemberView]

  def from(t: Tweet#TableElementType, m: Member#TableElementType): TweetWithMemberView =
    TweetWithMemberView(
      tweetId = t.tweetId,
      memberId = t.memberId,
      tweetText = t.tweetText,
      registerDatetime = t.registerDatetime.toLocalDateTime,
      versionNo = t.versionNo,
      memberName = m.memberName
    )
}
