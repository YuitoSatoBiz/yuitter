package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import slick.driver.JdbcProfile
import services.TweetService

/**
  * TWEETテーブルにCRUD処理を行うコントローラー
  *
  * @author yuito.sato
  */
class TweetController @Inject()(val dbConfigProvider: DatabaseConfigProvider, val tweetService: TweetService) extends Controller with HasDatabaseConfigProvider[JdbcProfile] {

  def list: Action[AnyContent] = Action.async { implicit rs =>
    tweetService.list().map { tweets =>
      Ok(Json.toJson(tweets))
    }
  }
}
