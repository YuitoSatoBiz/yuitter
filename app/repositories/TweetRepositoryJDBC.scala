package repositories

import javax.inject.Inject

import formats.TweetWithMemberView
import models.Tables.{Member, Tweet}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

/**
  * @author yuito.sato
  */
class TweetRepositoryJDBC @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  def listWithMember(): Future[Seq[TweetWithMemberView]] = {
    val action = Tweet
      .join(Member).on(_.memberId === _.memberId)
      .sortBy { case (t, m) =>
        t.registerDatetime.desc
      }
      .result
      .map(_.map {
        case (t, m) =>
          TweetWithMemberView(
            tweetId = t.tweetId,
            memberId = t.memberId,
            tweetText = t.tweetText,
            registerDatetime = t.registerDatetime,
            memberName = m.memberName
          )
      }
      )
    db.run(action)
  }
}
