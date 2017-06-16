package services

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import javax.inject.Inject

import formats.{AccountCommand, AccountView, KeywordCommand}
import play.api.libs.json.JsValue
import play.api.mvc.{AnyContent, Request}
import repositories.AccountRepositoryJDBC

import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * Accountテーブルに対してのビジネスロジックを実装するクラス
  *
  * @author yuito.sato
  */
class AccountService @Inject()(val accountJdbc: AccountRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val memberService: MemberService)(implicit ec: ExecutionContext)  extends HasDatabaseConfigProvider[JdbcProfile] {

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

  def create(form: AccountCommand)(implicit rs: Request[JsValue]): Future[Unit] = {
    for {
      memberId <- Future.successful(memberService.findCurrentMemberId.getOrElse(throw new IllegalArgumentException("アカウントを作成するにはログインが必要です。")))
      _ <- db.run(accountJdbc.create(form, memberId))
    } yield ()
  }

  def update(accountId: Long, form: AccountCommand)(implicit rs: Request[JsValue]): Future[Unit] = {
    for {
      _ <- Future.successful(memberService.findCurrentMemberId.getOrElse(throw new IllegalArgumentException("アカウントを作成するにはログインが必要です。")))
      versionNo <- Future.successful(form.versionNo.getOrElse(throw new IllegalArgumentException("バージョン番号がありません。")))
      _ <- db.run(accountJdbc.update(accountId, versionNo, form))
    } yield ()
  }

  def delete(accountId: Long)(implicit rs: Request[AnyContent]): Future[Unit] = {
    for {
      memberId <- Future.successful(memberService.findCurrentMemberId.getOrElse(throw new IllegalArgumentException("アカウントを作成するにはログインが必要です。")))
      _ <- db.run(accountJdbc.delete(accountId, memberId))
    } yield ()
  }
}
