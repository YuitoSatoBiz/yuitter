package services

import javax.inject.Inject

import formats.{TweetCommand, TweetView}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.mvc.AnyContent
import repositories.TweetRepositoryJDBC
import security.AuthenticatedRequest

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * TWEETテーブルに対してDB接続を行うクラス
  *
  * @author yuito.sato
  */
class TweetService @Inject()(val tweetJdbc: TweetRepositoryJDBC, val accountService: AccountService, val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def list(accountId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): Future[Seq[TweetView]] = {
    for {
      accounts <- accountService.listByMemberId(rs.memberId)
      tweets <- {
        if (accounts.map(a => a.accountId).contains(accountId)) {
          db.run(tweetJdbc.list(accountId, rs.memberId))
        } else {
          Future.failed(new IllegalArgumentException("不正なアカウントIDです。"))
        }
      }
    } yield tweets
  }

  def find(tweetId: Long): Future[Option[TweetView]] = {
    db.run(tweetJdbc.find(tweetId: Long))
  }

  def create(form: TweetCommand): Future[(Long, Option[Int])] = {
    db.run(tweetJdbc.create(form))
  }

  def update(tweetId: Long, form: TweetCommand): Future[Int] = {
    db.run(tweetJdbc.update(tweetId, form))
  }

  def delete(tweetId: Long): Future[(Int, Int)] = {
    db.run(tweetJdbc.delete(tweetId))
  }
}
