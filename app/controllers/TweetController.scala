package controllers

import javax.inject.Inject

import formats.TweetCommand
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.TweetService

import scala.concurrent.Future

/**
  * TWEETテーブルにCRUD処理を行うコントローラー
  *
  * @author yuito.sato
  */
class TweetController @Inject()(val tweetService: TweetService) extends Controller {

  /**
    * Tweetの一覧取得 GET /api/tweets
    *
    * @return Action[AnyContent]
    */
  def list: Action[AnyContent] = Action.async { implicit rs =>
    tweetService.list().map { tweets =>
      Ok(Json.toJson(tweets))
    }
  }

  /**
    * TweetをtweetIdで検索 GET /api/tweets/:tweetId
    *
    * @param tweetId 検索するツイートのID
    * @return Action[AnyContent]
    */
  def find(tweetId: Long): Action[AnyContent] = Action.async { implicit rs =>
    tweetService.find(tweetId).map {
      case Some(tweet) => Ok(Json.toJson(tweet))
      case None => BadRequest(Json.obj("result" -> "failure"))
    }
  }

  /**
    * Tweetを作成 POST /api/tweets
    *
    * @return Action[JsValue]
    */
  def create: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[TweetCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        tweetService.create(form).map { _ =>
          Ok(Json.obj("result" -> "success"))
        }
      }
    )
  }

  /**
    * Tweetを更新 PUT /api/tweets/:tweetId
    *
    * @param tweetId 更新するツイートのID
    * @return Action[JsValue]
    */
  def update(tweetId: Long): Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[TweetCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        tweetService.update(tweetId, form).map { _ =>
          Ok(Json.obj("result" -> "success"))
        }
      }
    )
  }

  /**
    * Tweetを削除 DELETE /api/tweets/:tweetId
    *
    * @param tweetId 削除するツイートのID
    * @return
    */
  def delete(tweetId: Long): Action[AnyContent] = Action.async { implicit rs =>
    tweetService.delete(tweetId).map { _ =>
      Ok(Json.obj("result" -> "success"));
    }
  }
}
