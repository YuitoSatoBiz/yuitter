package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject
import formats.{AccountView, MemberCommand, MemberView}
import models.Tables.{Account, AccountTweet, Member}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import slick.driver.MySQLDriver.api._
import models.Tables.{AccountRow, MemberRow}
import utils.Consts
import scala.concurrent.ExecutionContext

/**
  * MEMBERテーブルに対するクエリを生成したActionを返すクラス
  *
  * @author yuito.sato
  */
class MemberRepositoryJDBC @Inject()(val bCrypt: BCryptPasswordEncoder)(implicit ec: ExecutionContext) {

  def create(form: MemberCommand): DBIO[(Long, Int)] = {
    (for {
      memberId <- Member returning Member.map(_.memberId) += MemberRow(
        memberId = Consts.DefaultId,
        emailAddress = form.emailAddress,
        password = bCrypt.encode(form.password),
        registerDatetime = Timestamp.valueOf(LocalDateTime.now),
        updateDatetime = Timestamp.valueOf(LocalDateTime.now),
        versionNo = Consts.DefaultVersionNo
      )
      account <- Account += AccountRow(
        accountId = Consts.DefaultId,
        memberId = memberId,
        accountName = form.account.get.accountName,
        avatar = form.account.get.avatar,
        backgroundImage = form.account.get.backgroundImage,
        registerDatetime = Timestamp.valueOf(LocalDateTime.now),
        updateDatetime = Timestamp.valueOf(LocalDateTime.now),
        versionNo = Consts.DefaultVersionNo
      )
    } yield (memberId, account)).transactionally
  }

  def findByEmailAddress(emailAddress: String): DBIO[Option[MemberView]] = {
    Member.join(
      Account
    ).on { case (m, a) =>
      m.memberId === a.memberId
    }.filter { case (m, _) =>
      m.emailAddress === emailAddress.bind
    }
      .result
      .map { rows =>
        val member = rows.map(_._1).headOption
        val accounts = rows.map(_._2).map(AccountView.from)
        member.map { member =>
          MemberView.from(member, accounts)
        }
      }
  }

  def findById(memberId: Long): DBIO[Option[MemberView]] = {
    Member.join(
      Account
    ).on { case (m, a) =>
      m.memberId === a.memberId
    }.filter { case (m, _) =>
      m.memberId === memberId.bind
    }
      .result
      .map { rows =>
        val member = rows.map(_._1).headOption
        val accounts = rows.map(_._2).map(AccountView.from)
        member.map { member =>
          MemberView.from(member, accounts)
        }
      }
  }

  def findByTweetId(tweetId: Long, memberId: Long): DBIO[Option[Member#TableElementType]] = {
    val subAccount = Account.join(
      AccountTweet
    ).on { case (a, at) => a.accountId === at.accountId }
    Member.join(subAccount)
      .on { case (m, (a, _)) => m.memberId === a.memberId }
      .filter { case (m, (_, t)) =>
        (m.memberId === memberId.bind) && (t.tweetId === tweetId.bind)
      }
      .result
      .headOption
      .map(_.map { case (m, _) =>
        m
      })
  }
}
