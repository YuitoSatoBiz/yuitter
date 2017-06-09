package repositories

import java.sql.Timestamp
import java.time.LocalDateTime

import formats.{TweetForm, TweetWithMemberView}
import models.Tables.{Member, Tweet}

import scala.concurrent.ExecutionContext.Implicits.global
import slick.driver.MySQLDriver.api._
import models.Tables.TweetRow

/**
  * TWEETテーブルに対するクエリを生成しActionを返すクラス
  *
  * @author yuito.sato
  */
class TweetRepositoryJDBC {

  def listWithMember(): DBIO[Seq[TweetWithMemberView]] = {
    Tweet
      .join(Member).on { case (t, m) => t.memberId === m.memberId }
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
            registerDatetime = t.registerDatetime.toLocalDateTime,
            memberName = m.memberName
          )
      })
  }
  // TODO(yuito) 自分がフォローしているユーザーを含める

  def create(form: TweetForm): DBIO[Int] = {
    Tweet += TweetRow(
      0,
      1,
      // TODO(yuito) ログイン中のメンバーのIDを取得する
      form.tweetText,
      Timestamp.valueOf(LocalDateTime.now),
      Timestamp.valueOf(LocalDateTime.now),
      0
    )
  }
}
