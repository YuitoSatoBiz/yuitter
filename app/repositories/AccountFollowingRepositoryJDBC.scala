package repositories

import java.sql.Timestamp
import java.time.LocalDateTime
import javax.inject.Inject

import models.Tables.{AccountFollowing, AccountFollowingRow}
import slick.driver.MySQLDriver.api._
import utils.Consts

import scala.concurrent.ExecutionContext

/**
  * @author yuito.sato
  */
class AccountFollowingRepositoryJDBC @Inject()(implicit ex: ExecutionContext) {

  def create(followerId: Long, followeeId: Long): DBIO[Long] = {
    AccountFollowing returning AccountFollowing.map(_.followeeId) += AccountFollowingRow(
      accountFollowingId = Consts.DefaultId,
      followerId = followerId,
      followeeId = followeeId,
      registerDatetime = Timestamp.valueOf(LocalDateTime.now)
    )
  }
}
