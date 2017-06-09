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
      .sortBy { case (t, _) =>
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
      tweetId = 0L,
      memberId = 1L,
      // TODO(yuito) ログイン中のメンバーのIDを取得する
      tweetText = form.tweetText,
      registerDatetime = Timestamp.valueOf(LocalDateTime.now),
      updateDatetime = Timestamp.valueOf(LocalDateTime.now),
      versionNo = 0L
    )
  }

  def update(tweetId: Long, form: TweetForm): DBIO[Int] = {
    Tweet
      .filter(t => (t.tweetId === tweetId.bind) && (t.versionNo === form.versionNo) )
      .map(t => (t.tweetText, t.updateDatetime))
      .update(form.tweetText, Timestamp.valueOf(LocalDateTime.now))
  }
}
