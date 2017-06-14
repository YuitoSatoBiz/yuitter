package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject

import formats.MemberCommand
import models.Tables.{Account, Member}
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import models.Tables.{AccountRow, MemberRow}
import utils.Consts

/**
  * MEMBERテーブルに対するクエリを生成したActionを返すクラス
  *
  * @author yuito.sato
  */
class MemberRepositoryJDBC @Inject()(val bCrypt: BCryptPasswordEncoder) {

  def countByEmailAddress(emailAddress: String): DBIO[Int] = {
    Member.filter(_.emailAddress === emailAddress).length.result
  }

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
        accountName = form.account.accountName,
        avatar = form.account.avatar,
        backgroundImage = form.account.backgroundImage,
        registerDatetime = Timestamp.valueOf(LocalDateTime.now),
        updateDatetime = Timestamp.valueOf(LocalDateTime.now),
        versionNo = Consts.DefaultVersionNo
      )
    } yield (memberId, account)).transactionally
  }
}
