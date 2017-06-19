package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject
import formats.{AccountCreateCommand, AccountUpdateCommand, AccountView}
import models.Tables.{Account, AccountFollowing, AccountRow}
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
      .filter(_.accountName like "%" + keyword + "%")
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

  def create(form: AccountCreateCommand, memberId: Long): DBIO[Int] = {
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

  def update(accountId: Long, memberId: Long, form: AccountUpdateCommand): DBIO[Int] = {
    Account
      .filter(a => (a.accountId === accountId.bind) && (a.memberId === memberId.bind) && (a.versionNo === form.versionNo.bind))
      .map(a => (a.accountName, a.avatar, a.backgroundImage, a.versionNo, a.updateDatetime))
      .update(form.accountName.get, form.avatar, form.backgroundImage, form.versionNo + 1L, Timestamp.valueOf(LocalDateTime.now))
  }

  def delete(accountId: Long, memberId: Long): DBIO[Int] = {
    Account
      .filter(a => a.accountId === accountId.bind && a.memberId === memberId.bind)
      .delete
  }
}
