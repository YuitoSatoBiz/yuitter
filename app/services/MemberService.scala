package services

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.{MemberCommand, MemberView}
import play.api.mvc.Request
import repositories.MemberRepositoryJDBC
import models.Tables.Member
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import play.api.cache.CacheApi
import security.AuthenticatedRequest
import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * Memberテーブルに関するビジネスロジックを実装するクラス
  *
  * @author yuito.sato
  */
class MemberService @Inject()(val memberJdbc: MemberRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val cache: CacheApi, val bCrypt: BCryptPasswordEncoder)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(form: MemberCommand): Future[Unit] = {
    for {
      emailAddress <- db.run(memberJdbc.findByEmailAddress(form.emailAddress))
      _ <- if (emailAddress.isEmpty) db.run(memberJdbc.create(form)) else Future.failed(new IllegalArgumentException("このメールアドレスはすでに使用されています。"))
    } yield ()
  }

  /**
    * サインイン中の会員のIDを取得するメソッド。
    * この返り値がSomeかどうかでサインインステータスを確認することも可能
    *
    * @param rs クライアントからのリクエスト
    * @return キャッシュから取得したMemberテーブルのmemberId
    */
  def findCurrentMemberId(implicit rs: Request[_]): Option[Long] = {
    for {
      token <- rs.session.get("memberToken")
      memberId <- cache.get[Long](token)
    } yield memberId
  }

  def findByTweetId(tweetId: Long)(implicit rs: AuthenticatedRequest[_]): Future[Option[Member#TableElementType]] = {
    db.run(memberJdbc.findByTweetId(tweetId, rs.memberId))
  }

  def findCurrentWithAccounts(implicit rs: Request[_]): Future[Option[MemberView]] = {
    findCurrentMemberId.map(memberJdbc.findById) match {
      case Some(dbio) => db.run(dbio)
      case None => Future.failed(new IllegalArgumentException("セッションが切れています"))
    }
  }

  def authenticate(form: MemberCommand): Future[MemberView] = {
    for {
      optMember <- db.run(memberJdbc.findByEmailAddress(form.emailAddress))
      member <- Future.successful(optMember.getOrElse(throw new IllegalArgumentException("このメールアドレスは登録されていません。")))
      member <- if (bCrypt.matches(form.password, member.password)) Future.successful(member) else Future.failed(new IllegalArgumentException("メールアドレスまたはパスワードが間違えています。"))
    } yield member
  }
}
