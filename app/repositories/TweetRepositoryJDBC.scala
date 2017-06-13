package repositories

import java.sql.Timestamp
import java.time.LocalDateTime

import formats.{AccountView, TweetCommand, TweetView}
import models.Tables.{Account, AccountTweet, Tweet}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import models.Tables.{AccountTweetRow, TweetRow}
import utils.Consts

/**
  * TWEETテーブルに対するクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class TweetRepositoryJDBC {

  def list(): DBIO[Seq[TweetView]] = {
    val subAccountTweet = AccountTweet.join(Account).on {
      case (at, a) => at.accountId === a.accountId
    }

    Tweet
      .join(subAccountTweet).on { case (t, (at, _)) => t.tweetId === at.tweetId }
      .sortBy { case (t, (_, _)) =>
        t.registerDatetime.desc
      }
      .result
      .map {
        rows =>
          rows.map { case (t, (_, _)) =>
            t.tweetId
          }.distinct.map { tweetId =>
            rows.groupBy { case (t, (_, _)) =>
              t.tweetId
            }.filter { case (id, _) =>
              id == tweetId
            }
          }.flatMap(_.map { case (_, tuples) =>
            val tweet = tuples.map(_._1).head
            val accounts = tuples.map(_._2).map(_._2).map(AccountView.from)
            TweetView.from(tweet, accounts)
          }.toSeq)
      }
  }

  // TODO(yuito)自分がフォローしているユーザーを含める

  def find(tweetId: Long): DBIO[Option[TweetView]] = {
    val subAccountTweet = AccountTweet.join(Account).on {
      case (at, a) => at.accountId === a.accountId
    }
    Tweet
      .join(subAccountTweet).on { case (t, (at, _)) => t.tweetId === at.tweetId }
      .filter { case (t, (_, _)) => t.tweetId === tweetId }
      .result
      .map { rows =>
        val tweet = rows.map(_._1).head
        val accounts = rows.map(_._2).map(_._2).map(AccountView.from)
        Option.apply(TweetView.from(tweet, accounts))
      }
  }

  def create(form: TweetCommand): DBIO[(Long, Option[Int])] = {
    (for {
      tweet <- Tweet returning Tweet.map(_.tweetId) += TweetRow(
        tweetId = Consts.DefaultId,
        // TODO(yuito) ログイン中のメンバーのAccountIdしかおくれないようにする
        tweetText = form.tweetText,
        registerDatetime = Timestamp.valueOf(LocalDateTime.now),
        updateDatetime = Timestamp.valueOf(LocalDateTime.now),
        versionNo = Consts.DefaultVersionNo
      )
      accountTweet <- AccountTweet ++= form.accountIds.get.map { aid =>
        AccountTweetRow(
          accountTweetId = Consts.DefaultId,
          accountId = aid,
          tweetId = tweet,
          registerDatetime = Timestamp.valueOf(LocalDateTime.now),
          updateDatetime = Timestamp.valueOf(LocalDateTime.now),
          versionNo = Consts.DefaultVersionNo)
      }
    } yield (tweet, accountTweet)).transactionally
  }

  def update(tweetId: Long, form: TweetCommand): DBIO[Int] = {
    Tweet
      .filter(t => (t.tweetId === tweetId.bind) && (t.versionNo === form.versionNo))
      .map(t => (t.tweetText, t.updateDatetime))
      .update(form.tweetText, Timestamp.valueOf(LocalDateTime.now))
  }

  def delete(tweetId: Long): DBIO[(Int, Int)] =
    (for {
      accountTweet <- AccountTweet
        .filter(_.tweetId === tweetId)
        .delete
      tweet <- Tweet
        .filter(_.tweetId === tweetId)
        .delete
    } yield (accountTweet, tweet)).transactionally
}
