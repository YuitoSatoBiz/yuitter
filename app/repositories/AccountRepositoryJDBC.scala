package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject

import formats.{AccountView, TweetCommand, TweetView}
import models.Tables.{Account, AccountFollowing, AccountTweet, AccountTweetRow, Tweet, TweetRow}
import slick.driver.MySQLDriver.api._
import utils.Consts

import scala.concurrent.ExecutionContext

/**
  * Accountテーブルに対するクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class AccountRepositoryJDBC @Inject()(implicit ec: ExecutionContext) {

  def search(keyword: String): DBIO[Seq[AccountView]] = {
    Account
      .filter(_.accountName like "%" + "%")
      .result
      .map(_.map{ account =>
        AccountView.from(account)
      })
  }

  def listFollowers(accountId: Long): DBIO[Seq[AccountView]] = {
    Account
      .join(AccountFollowing)
      .on{ case (a, af) =>
        a.accountId === af.followeeId
      }
      .filter{ case (_, af) =>
          af.followeeId === accountId
      }
      .result
      .map(_.map{ case (a, _) =>
        AccountView.from(a)
      })
  }
}
