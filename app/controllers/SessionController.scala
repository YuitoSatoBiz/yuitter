package controllers

import java.util.UUID
import javax.inject.Inject
import formats.MemberCommand
import play.api.Logger
import play.api.libs.json.{JsError, JsValue}
import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import play.api.cache._
import services.MemberService
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * ログイン処理をするコントローラー
  *
  * @author yuito.sato
  */
class SessionController @Inject()(val memberService: MemberService, val cache: CacheApi)(implicit ec: ExecutionContext) extends Controller {

  def create: Action[JsValue] = Action.async(parse.json) { implicit rs =>
    rs.body.validate[MemberCommand].fold(
      invalid = { e =>
        Future {
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        }
      },
      valid = { form =>
        memberService.authenticate(form).map { member =>
          val token = UUID.randomUUID().toString
          val memberId = member.memberId
          cache.set(token, memberId, 30.days)
          Ok(Json.obj("result" -> "success")).withSession(
            "token" -> token)
        }.recover { case _ =>
          BadRequest(Json.obj("result" -> "failure", "error" -> "メールアドレスまたはパスワードが間違えています。"))
        }
      }
    )
  }
}
