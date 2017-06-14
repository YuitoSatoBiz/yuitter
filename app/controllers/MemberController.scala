package controllers

import javax.inject.Inject

import formats.MemberCommand
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MemberService
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future

/**
  * 会員のCRUD処理をするコントローラー
  *
  * @author yuito.sato
  */
class MemberController @Inject()(val memberService: MemberService) extends Controller {

  /**
    * Memberを登録
    */
  def create: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[MemberCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        memberService.create(form).map { _ =>
          Ok(Json.obj("result" -> "success"))
        }.recover { case _ =>
          Ok(Json.obj("result" -> "このメールアドレスは既に登録されています。"))
        }
      }
    )
  }
}
