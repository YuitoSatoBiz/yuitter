package services

import javax.inject.Inject

import formats.{TweetCreateCommand, TweetUpdateCommand, TweetView}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json.JsValue
import play.api.mvc.AnyContent
import repositories.TweetRepositoryJDBC
import security.AuthenticatedRequest
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

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

  def searchByAccountId(accountId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): Future[Seq[TweetView]] = {
    db.run(tweetJdbc.searchByAccountId(accountId))
  }

  def find(tweetId: Long): Future[Option[TweetView]] = {
    db.run(tweetJdbc.find(tweetId: Long))
  }

  def create(form: TweetCreateCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Option[TweetView]] = {
    for {
      accounts <- accountService.listByMemberId(rs.memberId)
      tweetResult <- {
        if (form.accountIds.forall(id => accounts.map(_.accountId).contains(id))) {
          db.run(tweetJdbc.create(form))
        } else {
          Future.failed(new IllegalArgumentException("不正なアカウントIDです。"))
        }
      }
      tweet <- db.run(tweetJdbc.find(tweetResult._1))
    } yield tweet
  }

  def update(tweetId: Long, form: TweetUpdateCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Option[TweetView]] = {
    for {
      member <- memberService.findByTweetId(tweetId)
      _ <- {
        if (member.nonEmpty) {
          db.run(tweetJdbc.update(tweetId, form))
        } else {
          Future.failed(new IllegalArgumentException("ツイートを更新する権限がありません"))
        }
      }
      tweet <- db.run(tweetJdbc.find(tweetId))
    } yield tweet
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
