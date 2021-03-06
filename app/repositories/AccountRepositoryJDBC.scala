package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject

import formats.{AccountCreateCommand, AccountUpdateCommand, AccountView}
import models.Tables.{Account, AccountFollowing, AccountFollowingRow, AccountRow, AccountTweet}
import slick.driver.MySQLDriver.api._
import utils.Consts

import scala.concurrent.ExecutionContext

/**
  * Accountテーブルに対するクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class AccountRepositoryJDBC @Inject()(implicit ec: ExecutionContext) {

  def list: DBIO[Seq[AccountView]] = {
    Account
      .sortBy(_.registerDatetime.desc)
      .take(Consts.ContentMaxCountPerPage)
      .result
      .map(_.map { a =>
        AccountView.from(a)
      })
  }

  def search(keyword: String): DBIO[Seq[AccountView]] = {
    Account
      .filter(_.accountName like "%" + keyword + "%")
      .take(Consts.ContentMaxCountPerPage)
      .result
      .map(_.map { a =>
        AccountView.from(a)
      })
  }

  def listByMemberId(memberId: Long): DBIO[Seq[AccountView]] = {
    Account
      .filter(_.memberId === memberId)
      .result
      .map(_.map { a =>
        AccountView.from(a)
      })
  }

  def listFollowers(accountId: Long): DBIO[Seq[AccountView]] = {
    Account
      .join(AccountFollowing)
      .on { case (a, af) =>
        a.accountId === af.followerId
      }
      .filter { case (_, af) =>
        af.followeeId === accountId.bind
      }
      .result
      .map(_.map { case (a, _) =>
        AccountView.from(a)
      })
  }

  def listFollowees(accountId: Long): DBIO[Seq[AccountView]] = {
    Account
      .join(AccountFollowing)
      .on { case (a, af) =>
        a.accountId === af.followeeId
      }
      .filter { case (_, af) =>
        af.followerId === accountId.bind
      }
      .result
      .map(_.map { case (a, _) =>
        AccountView.from(a)
      })
  }

  def find(accountId: Long): DBIO[Option[AccountView]] = {
    Account
      .filter(_.accountId === accountId.bind)
      .result
      .headOption
      .map(_.map { a =>
        AccountView.from(a)
      })
  }

  def create(form: AccountCreateCommand, memberId: Long): DBIO[Long] = {
    (for {
      accountId <- Account returning Account.map(_.accountId) += AccountRow(
        accountId = Consts.DefaultId,
        memberId = memberId,
        accountName = form.accountName,
        avatar = form.avatar,
        backgroundImage = form.backgroundImage,
        registerDatetime = Timestamp.valueOf(LocalDateTime.now),
        updateDatetime = Timestamp.valueOf(LocalDateTime.now),
        versionNo = Consts.DefaultVersionNo
      )
      _ <- AccountFollowing += AccountFollowingRow(
        accountFollowingId = Consts.DefaultId,
        followerId = accountId,
        followeeId = accountId,
        registerDatetime = Timestamp.valueOf(LocalDateTime.now)
      )
    } yield accountId).transactionally
  }

  def update(accountId: Long, memberId: Long, form: AccountUpdateCommand): DBIO[Int] = {
    Account
      .filter(a => (a.accountId === accountId.bind) && (a.memberId === memberId.bind) && (a.versionNo === form.versionNo.bind))
      .map(a => (a.accountName, a.avatar, a.backgroundImage, a.versionNo, a.updateDatetime))
      .update(form.accountName.get, form.avatar, form.backgroundImage, form.versionNo + Consts.AutoIncremental, Timestamp.valueOf(LocalDateTime.now))
  }

  def delete(accountId: Long, memberId: Long): DBIO[(Int, Int)] = {
    (for {
      accountTweetResult <- AccountTweet
        .filter(at =>
          (at.accountId === accountId.bind) &&
          (at.accountId in Account
            .filter(_.memberId === memberId.bind)
            .map(_.accountId))
        ).delete
      accountResult <- Account
        .filter(a => a.accountId === accountId.bind && a.memberId === memberId.bind)
        .delete
    } yield (accountTweetResult, accountResult)).transactionally
  }
}
