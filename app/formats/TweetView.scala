package formats

import java.time.LocalDateTime
import models.Tables.Tweet

import play.api.libs.json._

/**
  * クライアントで表示用のTWEETテーブルのケースクラス
  *
  * @author yuito.sato
  */
case class TweetView(
  tweetId: Long,
  tweetText: String,
  registerDatetime: LocalDateTime,
  versionNo: Long,
  accounts: Seq[AccountView]
)

object TweetView {

  implicit val tweetViewWrites: OWrites[TweetView] = Json.writes[TweetView]

  def from(t: Tweet#TableElementType, aList: Seq[AccountView]): TweetView =
    TweetView(
      tweetId = t.tweetId,
      tweetText = t.tweetText,
      registerDatetime = t.registerDatetime.toLocalDateTime,
      versionNo = t.versionNo,
      accounts = aList
    )
}
