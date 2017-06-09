package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.TweetService

/**
  * TWEETテーブルにCRUD処理を行うコントローラー
  *
  * @author yuito.sato
  */
class TweetController @Inject()(val tweetService: TweetService) extends Controller {

  def list: Action[AnyContent] = Action.async { implicit rs =>
    tweetService.list().map { tweets =>
      Ok(Json.toJson(tweets))
    }
  }
}
