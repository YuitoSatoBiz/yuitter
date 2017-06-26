package controllers

import javax.inject.Inject

import formats.{AccountCreateCommand, AccountFollowingCommand, AccountUpdateCommand, KeywordCommand}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import security.AuthenticatedAction
import services.{AccountFollowingService, AccountService, MemberService}

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author yuito.sato
  */
class AccountFollowingController @Inject()(val accountFollowingService: AccountFollowingService, val authenticatedAction: AuthenticatedAction)(implicit val ec: ExecutionContext) extends Controller {

  def create: Action[JsValue] = authenticatedAction.async(parse.json) {implicit rs =>
    rs.body.validate[AccountFollowingCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountFollowingService.create(form).map { followeeId =>
          Ok(Json.obj("result" -> "success", "followeeId" -> followeeId))
        }.recover{ case e =>
          BadRequest(Json.obj("result" -> "success", "error" -> e.getMessage))
        }
      }
    )
  }
}
