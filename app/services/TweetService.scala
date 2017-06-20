package services

import javax.inject.Inject
import formats.{TweetCreateCommand, TweetUpdateCommand, TweetView}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
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
class TweetService @Inject()(val tweetJdbc: TweetRepositoryJDBC, val accountService: AccountService, val memberService: MemberService, val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def list(accountId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): Future[Seq[TweetView]] = {
    for {
      accounts <- accountService.listByMemberId(rs.memberId)
      tweets <- {
        if (accounts.map(a => a.accountId).contains(accountId)) {
          db.run(tweetJdbc.list(accountId))
        } else {
          Future.failed(new IllegalArgumentException("不正なアカウントIDです。"))
        }
      }
    } yield tweets
  }

  def find(tweetId: Long): Future[Option[TweetView]] = {
    db.run(tweetJdbc.find(tweetId: Long))
  }

  def create(form: TweetCreateCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Unit] = {
    for {
      accounts <- accountService.listByMemberId(rs.memberId)
      _ <- {
        if (accounts.map(a => a.accountId).exists(id => form.accountIds.contains(id))) {
          db.run(tweetJdbc.create(form))
        } else {
          Future.failed(new IllegalArgumentException("不正なアカウントIDです。"))
        }
      }
    } yield ()
  }

  def update(tweetId: Long, form: TweetUpdateCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Int] = {
    for {
      member <- memberService.findByTweetId(tweetId)
      result <- {
        if (member.nonEmpty) {
          db.run(tweetJdbc.update(tweetId, form))
        } else {
          Future.failed(new IllegalArgumentException("ツイートを更新する権限がありません"))
        }
      }
    } yield result
  }

  def delete(tweetId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): Future[(Int, Int)] = {
    for {
      member <- memberService.findByTweetId(tweetId)
      result <- {
        if (member.nonEmpty) {
          db.run(tweetJdbc.delete(tweetId))
        } else {
          Future.failed(new IllegalArgumentException("ツイートを削除する権限がありません"))
        }
      }
    } yield result
  }
}
