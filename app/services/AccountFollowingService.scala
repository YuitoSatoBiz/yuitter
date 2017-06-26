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

  def create(form: AccountFollowingCommand)(implicit rs: AuthenticatedRequest[_]): Future[Long] = {
    for {
      followerId <- {
        val optFollowerId = rs.cookies.get("accountId").map(_.value.toLong)
        if (optFollowerId.nonEmpty) {
          Future.successful(optFollowerId.get)
        } else {
          Future.failed(new IllegalArgumentException("セッションが切れています。"))
        }
      }
      followerId <- db.run(accountFollowingJdbc.create(followerId, form.followeeId))
    } yield followerId
  }
}
