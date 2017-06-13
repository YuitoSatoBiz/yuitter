package services

import javax.inject.Inject

import formats.{TweetCommand, TweetView}
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

  def list(): Future[Seq[TweetView]] = {
    db.run(tweetJdbc.list())
  }

  def find(tweetId: Long): Future[Option[TweetView]] = {
    db.run(tweetJdbc.find(tweetId: Long))
  }

  def create(form: TweetCommand): Future[(Long, Option[Int])] = {
    db.run(tweetJdbc.create(form))
  }

  def update(tweetId: Long, form: TweetCommand): Future[Int] = {
    db.run(tweetJdbc.update(tweetId, form))
  }

  def delete(tweetId: Long): Future[(Int, Int)] = {
    db.run(tweetJdbc.delete(tweetId))
  }
}
