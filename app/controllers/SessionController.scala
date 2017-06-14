package controllers

import javax.inject.Inject

import formats.{MemberCommand, TweetCommand}
import play.api.libs.json.{JsError, JsValue}
import play.api.mvc.{Action, AnyContent, BodyParsers, Controller, Result}
import play.api.libs.json._
import services.SessionService
//import play.mvc.BodyParsers

import scala.concurrent.Future

/**
  * ログイン処理をするコントローラー
  *
  * @author yuito.sato
  */
class SessionController @Inject()(val sessionService: SessionService) extends Controller {

  def create: Action[JsValue] = Action(BodyParsers.parse.json) { implicit rs =>
    rs.body.validate[MemberCommand].fold(
      invalid = { e =>
        BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
      },
      valid = { form =>
        sessionService.authenticate(form)
        Ok("result" -> "success").withSession(
          "connected" -> "user@gmail.com")
      }
    )
  }
}
