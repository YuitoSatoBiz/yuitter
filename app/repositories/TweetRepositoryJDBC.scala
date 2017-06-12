package repositories

import java.sql.Timestamp
import java.time.LocalDateTime

import formats.{AccountView, TweetCommand, TweetView}
import models.Tables.{Account, AccountTweet, Tweet}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import models.Tables.TweetRow
import play.api.Logger

/**
  * TWEETテーブルに対するクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class TweetRepositoryJDBC {

  def list(): DBIO[Seq[TweetView]] = {
    Tweet
      .join(
        AccountTweet.join(Account).on {
          case (at, a) => at.accountId === a.accountId
        }
      ).on { case (t, (at, a)) => t.tweetId === at.tweetId }
      .sortBy { case (t, (_, _)) =>
        t.registerDatetime.desc
      }
      .result
      .map(_.groupBy { case (t, (_, _)) =>
        Logger.logger.debug(t.tweetId.toString)
        t.tweetId
      })
      .map(_.map { case (key, rows) =>
        Logger.logger.debug(key.toString)
        val tweet = rows.map(_._1).head
//        Logger.logger.debug(tweet.tweetId.toString)
        val accounts = rows.map(_._2).map(_._2).map(AccountView.from)
        TweetView.from(tweet, accounts)
      }.toSeq)
  }

  // TODO(yuitoe)自分がフォローしているユーザーを含める

  def find(tweetId: Long): DBIO[Option[TweetView]] = {
    Tweet
      .join(
        AccountTweet.join(Account).on {
          case (at, a) => at.accountId === a.accountId
        }
      ).on { case (t, (at, a)) => t.tweetId === at.tweetId }
      .result
      .map(_.groupBy { case (t, (_, _)) => t.tweetId })
      .map(_.map { case (_, rows) =>
        val tweet = rows.map(_._1).head
        val accounts = rows.map(_._2).map(_._2).map(AccountView.from)
        TweetView.from(tweet, accounts)
      }.headOption)
  }

  def create(form: TweetCommand): DBIO[Int] = {
    Tweet += TweetRow(
      tweetId = 0L,
      // TODO(yuito) ログイン中のメンバーのIDを取得する
      tweetText = form.tweetText,
      registerDatetime = Timestamp.valueOf(LocalDateTime.now),
      updateDatetime = Timestamp.valueOf(LocalDateTime.now),
      versionNo = 0L
    )
  }

  def update(tweetId: Long, form: TweetCommand): DBIO[Int] = {
    Tweet
      .filter(t => (t.tweetId === tweetId.bind) && (t.versionNo === form.versionNo))
      .map(t => (t.tweetText, t.updateDatetime))
      .update(form.tweetText, Timestamp.valueOf(LocalDateTime.now))
  }

  def delete(tweetId: Long): DBIO[Int] = {
    Tweet
      .filter(_.tweetId === tweetId)
      .delete
  }
}
