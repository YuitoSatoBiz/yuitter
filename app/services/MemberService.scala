package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.MemberCommand
import play.api.libs.json.JsValue
import play.api.mvc.Request
import repositories.MemberRepositoryJDBC
import models.Tables.Member
import play.api.cache.CacheApi

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * Memberテーブルに対してDB接続をするクラス
  *
  * @author yuito.sato
  */
class MemberService @Inject()(val memberJdbc: MemberRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, cache: CacheApi)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(form: MemberCommand): Future[(Long, Int)] = {
    for {
      countByEmailAddress <- db.run(memberJdbc.countByEmailAddress(form.emailAddress))
      createMember <- db.run(memberJdbc.create(form)) if countByEmailAddress.equals(0)
    } yield createMember
  }

  def findCurrentUser(implicit rs: Request[JsValue]): Future[Option[Member#TableElementType]] = {
    val memberId = for {
      token <- rs.session.get("token")
      memberId <- cache.get[Int](token)
    } yield memberId
    db.run(memberJdbc.findById(memberId
      .getOrElse(throw new IllegalArgumentException("セッションが切れています")))
    )
  }
}
