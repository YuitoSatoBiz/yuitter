package controllers

import javax.inject.Inject

import formats.AccountFollowingCommand
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import security.AuthenticatedAction
import services.AccountFollowingService
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
        val optFollowerId = rs.cookies.get("accountId").map(_.value.toLong)
        if (optFollowerId.nonEmpty) {
          accountFollowingService.create(optFollowerId.get, form.followeeId).map { _ =>
            Ok(Json.obj("result" -> "success"))
          }
        } else {
          Future.successful(BadRequest(Json.obj("result" -> "failure", "error" -> "セッションが切れています。")))
        }
      }
    )
  }

  def delete(followeeId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    val optFollowerId = rs.cookies.get("accountId").map(_.value.toLong)
    if (optFollowerId.nonEmpty) {
      accountFollowingService.delete(optFollowerId.get, followeeId).map { _ =>
        Ok(Json.obj("result" -> "success"))
      }
    } else {
      Future.successful(BadRequest(Json.obj("result" -> "failure", "error" -> "セッションが切れています。")))
    }
  }

  def find(followeeId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    val optFollowerId = rs.cookies.get("accountId").map(_.value.toLong)
    if (optFollowerId.nonEmpty) {
      accountFollowingService.find(optFollowerId.get, followeeId).map {
        case Some(resultFolloweeId) => Ok(Json.obj("result" -> true, "followeeId" -> resultFolloweeId))
        case None => Ok(Json.obj("result" -> false))
      }
    } else {
      Future.successful(BadRequest(Json.obj("result" -> "failure", "error" -> "セッションが切れています。")))
    }
  }
}
