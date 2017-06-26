package controllers

import javax.inject.Inject

import formats.{TweetCreateCommand, TweetUpdateCommand}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import security.AuthenticatedAction
import services.TweetService

import scala.concurrent.Future

/**
  * TWEETテーブルにCRUD処理を行うコントローラー
  *
  * @author yuito.sato
  */
class TweetController @Inject()(val tweetService: TweetService, val authenticatedAction: AuthenticatedAction) extends Controller {

  /**
    * Tweetの一覧取得 GET /api/tweets
    *
    * @return Action[AnyContent]
    */
  def list(accountId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    tweetService.list(accountId).map { tweets =>
      Ok(Json.toJson(tweets))
    }
  }

  /**
    * 指定アカウントのツイート一覧を取得 GET /api/search/:accountId
    *
    * @param accountId 検索するアカウントのiD
    */
  def searchByAccountId(accountId: Long): Action[AnyContent] = authenticatedAction.async{ implicit rs =>
    tweetService.searchByAccountId(accountId).map { tweets =>
      Ok(Json.toJson(tweets))
    }
  }

  /**
    * TweetをtweetIdで検索 GET /api/tweets/:tweetId
    *
    * @param tweetId 検索するツイートのID
    * @return Action[AnyContent]
    */
  def find(tweetId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
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
  def create: Action[JsValue] = authenticatedAction.async(parse.json) { implicit rs =>
    rs.body.validate[TweetCreateCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        tweetService.create(form).map {
          case Some(tweet) => Ok(Json.toJson(tweet))
          case None => BadRequest(Json.obj("result" -> "failure"))
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
  def update(tweetId: Long): Action[JsValue] = authenticatedAction.async(parse.json) { implicit rs =>
    rs.body.validate[TweetUpdateCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        tweetService.update(tweetId, form).map {
          case Some(tweet) => Ok(Json.toJson(tweet))
          case None => BadRequest(Json.obj("result" -> "failure"))
        }.recover{ case e =>
          Unauthorized(Json.obj("result" -> "failure", "error" -> e.getMessage))
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
  def delete(tweetId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    tweetService.delete(tweetId).map {
      case (0, 0) => BadRequest(Json.obj("result" -> "failure", "error" -> "削除に失敗しました。"))
      case (at, t) => Ok(Json.obj("result" -> "success", "error" -> ("AccountTweet削除:" + at.toString + "件" + "Tweet削除:" + t.toString + "件")))
    }.recover{ case e =>
      Unauthorized(Json.obj("result" -> "failure", "error" -> e.getMessage))
    }
  }
}
