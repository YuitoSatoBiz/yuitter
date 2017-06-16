package controllers

import javax.inject.Inject

import formats.KeywordCommand
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.AccountService

import scala.concurrent.Future


/**
  * AccountテーブルのCRUD処理をするコントローラー
  *
  * @author yuito.sato
  */
class AccountController @Inject()(val accountService: AccountService) extends Controller {

  def search: Action[JsValue] = Action.async(parse.json) { implicit  rs =>
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

  def listFollowers(accountId: Long) = Action.async { implicit rs =>
    accountService.listFollowers(accountId).map
  }

  def listFollowees(accountId: Long) = ???

  def find(accountId: Long) = ???

  def create = ???

  def update(accountId: Long) = ???

  def delete(accountId: Long) = ???
}
