package services

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import javax.inject.Inject

import formats.{AccountCommand, AccountView, KeywordCommand}
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

  def search(form: KeywordCommand): Future[Seq[AccountView]] = {
    db.run(accountJdbc.search(form.keyword))
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

  def create(form: AccountCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Int] = {
    db.run(accountJdbc.create(form, rs.memberId))
  }

  def update(accountId: Long, form: AccountCommand)(implicit rs: AuthenticatedRequest[JsValue]): Future[Unit] = {
    for {
      versionNo <- {
        if (form.versionNo.nonEmpty) {
          Future.successful(form.versionNo.get)
        } else {
          Future.failed(new IllegalArgumentException("バージョン番号がありません。"))
        }
      }
      _ <- db.run(accountJdbc.update(accountId, rs.memberId, versionNo, form))
    } yield ()
  }

  def delete(accountId: Long)(implicit rs: AuthenticatedRequest[AnyContent]): Future[Int] = {
    db.run(accountJdbc.delete(accountId, rs.memberId))
  }
}
