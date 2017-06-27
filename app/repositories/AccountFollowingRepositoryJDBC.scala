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

  def create(followerId: Long, followeeId: Long): DBIO[Int] = {
    AccountFollowing += AccountFollowingRow(
      accountFollowingId = Consts.DefaultId,
      followerId = followerId,
      followeeId = followeeId,
      registerDatetime = Timestamp.valueOf(LocalDateTime.now)
    )
  }

  def delete(followerId: Long, followeeId: Long): DBIO[Int] = {
    AccountFollowing
      .filter(af => af.followerId === followerId.bind && af.followeeId === followeeId.bind)
      .delete
  }

  def find(followerId: Long, followeeId: Long): DBIO[Option[Long]] = {
    AccountFollowing
      .filter(af => af.followerId === followerId.bind && af.followeeId === followeeId.bind)
      .result
      .headOption
      .map(_.map(_.followeeId))
  }
}
