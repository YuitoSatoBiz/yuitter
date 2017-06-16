package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.{MemberCommand, MemberView}
import play.api.libs.json.JsValue
import play.api.mvc.{AnyContent, Request}
import repositories.MemberRepositoryJDBC
import models.Tables.Member
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import play.api.cache.CacheApi

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * Memberテーブルに対してDB接続をするクラス
  *
  * @author yuito.sato
  */
class MemberService @Inject()(val memberJdbc: MemberRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val cache: CacheApi, val bCrypt: BCryptPasswordEncoder)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(form: MemberCommand): Future[(Long, Int)] = {
    for {
      countByEmailAddress <- db.run(memberJdbc.countByEmailAddress(form.emailAddress))
      createMember <- db.run(memberJdbc.create(form)) if countByEmailAddress.equals(0)
    } yield createMember
  }

  def findCurrentMember(implicit rs: Request[JsValue]): Future[Option[Member#TableElementType]] = {
    val memberId = for {
      token <- rs.session.get("token")
      memberId <- cache.get[Int](token)
    } yield memberId
    db.run(memberJdbc.findById(
      memberId.getOrElse(throw new IllegalArgumentException("セッションが切れています"))
    ))
  }

  def findCurrentMemberWithAccounts(implicit rs: Request[AnyContent]): Future[Option[MemberView]] = {
    val memberId = for {
      token <- rs.session.get("token")
      memberId <- cache.get[Long](token)
    } yield memberId

    memberId.map(memberJdbc.findByIdWithAccounts) match {
      case Some(dbio) => db.run(dbio)
      case None => Future.failed(new IllegalArgumentException("セッションが切れています"))
    }
  }

  def authenticate(form: MemberCommand): Future[Member#TableElementType] = {
    for {
      optMember <- db.run(memberJdbc.findByEmailAddress(form.emailAddress))
      member <- Future.successful(optMember.getOrElse(throw new IllegalArgumentException("このメールアドレスは登録されていません。")))
      member <- if (bCrypt.matches(form.password, member.password)) Future.successful(member) else Future.failed(new IllegalArgumentException("メールアドレスまたはパスワードが間違えています。"))
    } yield member
  }
}
