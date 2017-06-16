package services

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import javax.inject.Inject

import formats.{AccountCommand, AccountView, KeywordCommand, TweetCommand}
import repositories.AccountRepositoryJDBC

import scala.concurrent.Future
import slick.driver.JdbcProfile

/**
  * Accountテーブルに対してのビジネスロジックを実装するクラス
  *
  * @author yuito.sato
  */
class AccountService @Inject()(val accountJdbc: AccountRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val memberService: MemberService) extends HasDatabaseConfigProvider[JdbcProfile] {

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

  def create(form: AccountCommand): Future[Unit] = {
    for {
      memberId <- Future.successful(memberService.findCurrentMemberId.getOrElse(throw new IllegalArgumentException("アカウントを作成するにはログインが必要です。")))
      _ <- db.run(accountJdbc.create(form, memberId))
    } yield ()
  }

  def update(accountId: Long, form: AccountCommand): Future[Unit] = {
    for {
      _ <- Future.successful(memberService.findCurrentMemberId.getOrElse(throw new IllegalArgumentException("アカウントを作成するにはログインが必要です。")))
      versionNo <- Future.successful(form.versionNo.getOrElse(throw new IllegalArgumentException("バージョン番号がありません。")))
      _ <- db.run(accountJdbc.update(accountId, versionNo, form))
    } yield ()
  }
}
