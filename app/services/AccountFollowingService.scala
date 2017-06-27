package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.{AccountFollowingCommand}
import repositories.{AccountFollowingRepositoryJDBC}
import security.AuthenticatedRequest
import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile


/**
  * Created by yuito.sato on 2017/06/26.
  */
class AccountFollowingService @Inject()(val accountFollowingJdbc: AccountFollowingRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(followerId: Long, followeeId: Long): Future[Int] = {
    db.run(accountFollowingJdbc.create(followerId, followeeId))
  }

  def delete(followerId: Long, followeeId: Long): Future[Int] = {
    db.run(accountFollowingJdbc.delete(followerId, followeeId))
  }

  def find(followerId: Long, followeeId: Long): Future[Option[Long]] = {
    db.run(accountFollowingJdbc.find(followerId, followeeId))
  }
}
