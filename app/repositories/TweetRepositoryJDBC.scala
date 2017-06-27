package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject
import formats.{AccountView, TweetCreateCommand, TweetUpdateCommand, TweetView}
import models.Tables.{Account, AccountFollowing, AccountTweet, AccountTweetRow, Tweet, TweetRow}
import play.api.libs.json.JsValue
import play.api.mvc.AnyContent
import security.AuthenticatedRequest
import slick.driver.MySQLDriver.api._
import utils.Consts
import scala.concurrent.ExecutionContext

/**
  * TWEETテーブルに対するクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class TweetRepositoryJDBC @Inject()(implicit ec: ExecutionContext) {

  def list(accountId: Long): DBIO[Seq[TweetView]] = {
    val subAccount = Account.join(AccountFollowing).on { case (a, af) =>
      a.accountId === af.followeeId
    }
    val subAccountTweet = AccountTweet.join(subAccount).on { case (at, (a, _)) =>
      at.accountId === a.accountId
    }
    Tweet
      .join(subAccountTweet).on { case (t, (at, _)) => t.tweetId === at.tweetId }
      .filter { case (_, (_, (_, af))) =>
        af.followerId === accountId.bind
      }
      .take(Consts.ContentMaxCountPerPage)
      .sortBy { case (t, _) =>
        t.registerDatetime.desc
      }
      .result
      .map {
        rows =>
          rows.map { case (t, _) =>
            t.tweetId
          }.distinct.map { tweetId =>
            rows.groupBy { case (t, _) =>
              t.tweetId
            }.filter { case (id, _) =>
              id == tweetId
            }
          }.flatMap(_.map { case (_, tuples) =>
            val tweet = tuples.map(_._1).head
            val accounts = tuples.map(_._2).map(_._2).map(_._1).map(AccountView.from)
            TweetView.from(tweet, accounts)
          }.toSeq)
      }
  }

  def searchByAccountId(accountId: Long): DBIO[Seq[TweetView]] = {
    val subAccountTweet = AccountTweet.join(Account).on { case (at, a) =>
      at.accountId === a.accountId
    }
    Tweet.join(subAccountTweet).on { case (t, (at, _)) =>
      t.tweetId === at.tweetId
    }
      .filter { case (_, (_, a)) => a.accountId === accountId.bind }
      .sortBy { case (t, _) =>
        t.registerDatetime.desc
      }
      .result
      .map {
        rows =>
          rows.map { case (t, _) =>
            t.tweetId
          }.distinct.map { tweetId =>
            rows.groupBy { case (t, _) =>
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

  def create(form: TweetCreateCommand): DBIO[(Long, Option[Int])] = {
    (for {
      tweetId <- Tweet returning Tweet.map(_.tweetId) += TweetRow(
        tweetId = Consts.DefaultId,
        tweetText = form.tweetText,
        registerDatetime = Timestamp.valueOf(LocalDateTime.now),
        updateDatetime = Timestamp.valueOf(LocalDateTime.now),
        versionNo = Consts.DefaultVersionNo
      )
      accountTweetResult <- AccountTweet ++= form.accountIds.map { aid =>
        AccountTweetRow(
          accountTweetId = Consts.DefaultId,
          accountId = aid,
          tweetId = tweetId,
          registerDatetime = Timestamp.valueOf(LocalDateTime.now)
        )
      }
    } yield (tweetId, accountTweetResult)).transactionally
  }

  def update(tweetId: Long, form: TweetUpdateCommand)(implicit rs: AuthenticatedRequest[JsValue]): DBIO[Int] = {
    Tweet
      .filter(t =>
        (t.tweetId === tweetId.bind) &&
          (t.versionNo === form.versionNo.bind)
      )
      .map(t => (t.tweetText, t.updateDatetime, t.versionNo))
      .update(form.tweetText, Timestamp.valueOf(LocalDateTime.now), form.versionNo + Consts.AutoIncremental)
  }

  def delete(tweetId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): DBIO[(Int, Int)] = {
    (for {
      accountTweetResult <- AccountTweet
        .filter(_.tweetId === tweetId.bind)
        .delete
      tweetResult <- Tweet
        .filter(_.tweetId === tweetId.bind)
        .delete
    } yield (accountTweetResult, tweetResult)).transactionally
  }
}
