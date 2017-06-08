package repositories

import javax.inject.Inject

import formats.TweetWithMemberView
import models.Tables.{Member, Tweet}
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

/**
  * TWEETテーブルに対してのクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class TweetRepositoryJDBC @Inject()(val dbConfigProvider: DatabaseConfigProvider) extends HasDatabaseConfigProvider[JdbcProfile] {

  def listWithMember(): DBIOAction[Seq[TweetWithMemberView], NoStream, Effect.Read] = {
    Tweet
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
  }
}
