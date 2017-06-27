package services

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import javax.inject.Inject
import formats.{AccountCreateCommand, AccountUpdateCommand, AccountView, KeywordCommand}
import play.api.libs.json.JsValue
import play.api.mvc.AnyContent
import repositories.AccountRepositoryJDBC
import security.AuthenticatedRequest
import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * Accountテーブルに対してのビジネスロジックを実装するクラス
  *
  * @author yuito.sato
  */
class AccountService @Inject()(val accountJdbc: AccountRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val memberService: MemberService)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def list: Future[Seq[AccountView]] = {
    db.run(accountJdbc.list)
  }

  def search(form: KeywordCommand): Future[Seq[AccountView]] = {
    db.run(accountJdbc.search(form.keyword))
  }

  def listByMemberId(memberId: Long): Future[Seq[AccountView]] = {
    db.run(accountJdbc.listByMemberId(memberId))
  }

  def listFollowers(accountId: Long): Future[Seq[AccountView]] = {
    db.run(accountJdbc.listFollowers(accountId))
  }

  def listFollowees(accountId: Long): Future[Seq[AccountView]] = {
    db.run(accountJdbc.listFollowees(accountId))
  }

  def find(accountId: Long): Future[Option[AccountView]] = {
    db.run(accountJdbc.find(accountId))
  }

  def create(form: AccountCreateCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Option[AccountView]] = {
    for {
      accountId <- db.run(accountJdbc.create(form, rs.memberId))
      account <- db.run(accountJdbc.find(accountId))
    } yield account
  }

  def update(accountId: Long, form: AccountUpdateCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Int] = {
    db.run(accountJdbc.update(accountId, rs.memberId, form))
  }

  def delete(accountId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): Future[(Int, Int)] = {
    db.run(accountJdbc.delete(accountId, rs.memberId))
  }
}
