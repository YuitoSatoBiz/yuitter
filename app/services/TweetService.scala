package services

import javax.inject.Inject

import formats.{TweetForm, TweetWithMemberView}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import repositories.TweetRepositoryJDBC

import scala.concurrent.Future
import slick.driver.JdbcProfile

/**
  * TWEETテーブルに対してDB接続を行うクラス
  *
  * @author yuito.sato
  */
class TweetService @Inject()(val tweetJdbc: TweetRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  def list(): Future[Seq[TweetWithMemberView]] = {
    db.run(tweetJdbc.listWithMember())
  }

  def find(tweetId: Long): Future[Option[TweetWithMemberView]] = {
    db.run(tweetJdbc.find(tweetId: Long))
  }

  def create(form: TweetForm): Future[Int] = {
    db.run(tweetJdbc.create(form))
  }

  def update(tweetId: Long, form: TweetForm): Future[Int] = {
    db.run(tweetJdbc.update(tweetId, form))
  }
}
