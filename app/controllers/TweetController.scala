package controllers

import javax.inject.Inject

import formats.TweetForm
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
    * Tweetの一覧取得 /tweet/list
    *
    * @return Action[AnyContent]
    */
  def list: Action[AnyContent] = Action.async { implicit rs =>
    tweetService.list().map { tweets =>
      Ok(Json.toJson(tweets))
    }
  }

  def find(tweetId: Long): Action[AnyContent] = Action.async { implicit rs =>
    tweetService.find(tweetId).map {
      case Some(tweet) => Ok(Json.toJson(tweet))
      case None => BadRequest(Json.obj("result" -> "failure"))
    }
  }

  def create: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[TweetForm].map { form =>
      tweetService.create(form).map { _ =>
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  def update(tweetId: Long): Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[TweetForm].map { form =>
      tweetService.update(tweetId, form).map { _ =>
        Ok(Json.obj("result" -> "success"))
      }
    }.recoverTotal { e =>
      Future {
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      }
    }
  }

  def delete(tweetId: Long): Action[AnyContent] = Action.async {implicit rs =>
    tweetService.delete(tweetId).map { _ =>
      Ok(Json.obj("result" -> "success"));
    }
  }
}
