package controllers

import java.util.UUID
import javax.inject.Inject
import formats.MemberCommand
import play.api.libs.json.{JsError, JsValue}
import play.api.mvc.{Action, AnyContent, Controller}
import play.api.libs.json._
import play.api.cache._
import services.MemberService
import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}

/**
  * ログイン処理をするコントローラー
  *
  * @author yuito.sato
  */
class SessionController @Inject()(val memberService: MemberService, val cache: CacheApi)(implicit ec: ExecutionContext) extends Controller {

  /**
    * サインイン POST /api/sign_in
    *
    * @return サインイン処理の結果
    */
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
            rs.session + ("token" -> token))
        }.recover { case e =>
          BadRequest(Json.obj("result" -> "failure", "error" -> e.getMessage))
        }
      }
    )
  }

  /**
    * サインアウト GET /api/sign_out
    *
    * @return サインアウト処理の結果
    */
  def delete: Action[AnyContent] = Action.async { implicit rs =>
    Future {
      rs.session.get("token").getOrElse {
        throw new IllegalArgumentException("すでにログアウトされています。")
      }
    }.map { token =>
      cache.remove(token)
      Ok(Json.obj("result" -> "success")).withNewSession
    }.recover { case e =>
      BadRequest(Json.obj("result" -> "failure", "error" -> e.getMessage))
    }
  }
}
