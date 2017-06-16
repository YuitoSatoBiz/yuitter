package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject

import formats.{AccountCommand, AccountView, TweetCommand, TweetView}
import models.Tables.{Account, AccountFollowing, AccountRow, AccountTweet, AccountTweetRow, Tweet, TweetRow}
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
      .map(_.map{ a =>
        AccountView.from(a)
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

  def listFollowees(accountId: Long): DBIO[Seq[AccountView]] = {
    Account
      .join(AccountFollowing)
      .on{ case (a, af) =>
        a.accountId === af.followerId
      }
      .filter{ case (_, af) =>
        af.followerId === accountId
      }
      .result
      .map(_.map{ case (a, _) =>
        AccountView.from(a)
      })
  }

  def find(accountId: Long): DBIO[Option[AccountView]] = {
    Account
      .filter(_.accountId === accountId)
      .result
      .headOption
      .map(_.map{ a =>
        AccountView.from(a)
      })
  }

  def create(form: AccountCommand, memberId: Long): DBIO[Int] = {
    Account += AccountRow(
      accountId = Consts.DefaultId,
      memberId = memberId,
      accountName = form.accountName,
      avatar = form.avatar,
      backgroundImage = form.backgroundImage,
      registerDatetime = Timestamp.valueOf(LocalDateTime.now),
      updateDatetime = Timestamp.valueOf(LocalDateTime.now),
      versionNo = Consts.DefaultVersionNo
    )
  }
}
