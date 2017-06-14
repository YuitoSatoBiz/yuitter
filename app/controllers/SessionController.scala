package controllers

import javax.inject.Inject
import formats.MemberCommand
import play.api.libs.json.{JsError, JsValue}
import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import services.SessionService
import scala.concurrent.{ExecutionContext, Future}

/**
  * ログイン処理をするコントローラー
  *
  * @author yuito.sato
  */
class SessionController @Inject()(val sessionService: SessionService)(implicit ec: ExecutionContext) extends Controller {

  def create: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[MemberCommand].fold(
      invalid = { e =>
        Future {
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        }
      },
      valid = { form =>
        sessionService.authenticate(form).map { authenticateFlag =>
          if (authenticateFlag) {
            Ok(Json.obj("result" -> "success")).withSession(
              "connected" -> "ok")
          } else {
            BadRequest(Json.obj("result" -> "failure", "error" -> "パスワードまたはメールアドレスが間違っています。"))
          }
        }.recover { case e =>
            BadRequest(Json.obj("result" -> "failure", "error" -> e.getMessage))
        }
      }
    )
  }
}
