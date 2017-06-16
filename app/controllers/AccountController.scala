package controllers

import javax.inject.Inject

import formats.{AccountCommand, KeywordCommand, TweetCommand}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.{AccountService, MemberService}

import scala.concurrent.Future


/**
  * AccountテーブルのCRUD処理をするコントローラー
  *
  * @author yuito.sato
  */
class AccountController @Inject()(val accountService: AccountService, val memberService: MemberService) extends Controller {

  def search: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[KeywordCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountService.search(form).map { accounts =>
          Ok(Json.toJson(accounts))
        }
      }
    )
  }

  def listFollowers(accountId: Long): Action[AnyContent] = Action.async { implicit rs =>
    accountService.listFollowers(accountId).map { followers =>
      Ok(Json.toJson(followers))
    }
  }

  def listFollowees(accountId: Long): Action[AnyContent] = Action.async { implicit rs =>
    accountService.listFollowers(accountId).map { followers =>
      Ok(Json.toJson(followers))
    }
  }

  def find(accountId: Long): Action[AnyContent] = Action.async { implicit rs =>
    accountService.find(accountId).map {
      case Some(account) => Ok(Json.toJson(account))
      case None => BadRequest(Json.obj("result" -> "failure", "errors" -> "指定されたIDのアカウントは存在しません"))
    }
  }

  def create: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[AccountCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountService.create(form).map { _ =>
          Ok(Json.obj("result" -> "success"))
        }.recover{ case e =>
          BadRequest(Json.obj("result" -> "failure", "error" -> e.getMessage))
        }
      }
    )
  }

  def update(accountId: Long): Action[JsValue] = Action.async(parse.json) {implicit rs =>
    rs.body.validate[AccountCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountService.update(accountId, form).map { _ =>
          Ok(Json.obj("result" -> "success"))
        }.recover{ case e =>
        BadRequest(Json.obj("result" -> "failure", "error" -> e.getMessage))}
      }
    )
  }

  def delete(accountId: Long) = ???
}
