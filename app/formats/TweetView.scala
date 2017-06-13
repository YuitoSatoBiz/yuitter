package formats

import java.time.LocalDateTime

import models.Tables.Tweet
import play.api.libs.json.{Json, Writes}

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

  implicit val tweetViewWrites: Writes[TweetView] = Json.writes[TweetView]

  def from(tweet: Tweet#TableElementType, accounts: Seq[AccountView]): TweetView = {
    TweetView(
      tweetId = tweet.tweetId,
      tweetText = tweet.tweetText,
      registerDatetime = tweet.registerDatetime.toLocalDateTime,
      versionNo = tweet.versionNo,
      accounts = accounts
    )
  }
}
