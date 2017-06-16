package services

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import javax.inject.Inject

import formats.{AccountView, KeywordCommand, TweetCommand}
import repositories.AccountRepositoryJDBC

import scala.concurrent.Future
import slick.driver.JdbcProfile

/**
  * Accountテーブルに対してのビジネスロジックを実装するクラス
  *
  * @author yuito.sato
  */
class AccountService @Inject()(val accountJdbc: AccountRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  def search(form: KeywordCommand): Future[Seq[AccountView]] = {
    db.run(accountJdbc.search(form.keyword))
  }

  def listFollowers(accountId: Long): Future[Seq[AccountView]] = {
    db.run(accountJdbc.listFollowers(accountId))
  }

  def listFollowees(accountId: Long): Future[Seq[AccountView]] = {
    db.run(accountJdbc.listFollowees(accountId))
  }
}

