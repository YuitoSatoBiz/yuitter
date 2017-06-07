package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import models.Tables._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import play.api.mvc.{Action, Controller}
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._
import TweetController._
import models.TweetWithMemberView

/**
  * TWEETテーブルにCRUD処理を行うコントローラー
  *
  * @author yuito.sato
  */
class TweetController @Inject()(val dbConfigProvider: DatabaseConfigProvider, val messagesApi: MessagesApi) extends Controller with HasDatabaseConfigProvider[JdbcProfile] with I18nSupport {

  def list = Action.async { implicit rs =>

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
    db.run(action).map { tweets =>
      Ok(Json.toJson(tweets))
    }
    // レポジトリ
    //    db.run(Tweet joinLeft Member on (_.memberId).result)
    //    db.run((Tweet joinLeft Member on (_.memberId == _.memberId)).result).map {tweets => }
//    db.run(Tweet.sortBy(t => t.registerDatetime.desc).result).map { tweets =>
//      Ok(Json.obj("tweets" -> tweets))
//    }
  }
    // TODO services に切り出す。
    // TODO sqlをはけるようにする
}

object TweetController {
  implicit val tweetRowWrites: Writes[TweetRow] = (
    (__ \ "tweetId").write[Long] and
      (__ \ "memberId").write[Long] and
      (__ \ "tweetText").write[String] and
      (__ \ "registerDatetime").write[java.sql.Timestamp] and
      (__ \ "updateDatetime").write[java.sql.Timestamp] and
      (__ \ "versionNo").write[Long]
    ) (unlift(TweetRow.unapply))
}
