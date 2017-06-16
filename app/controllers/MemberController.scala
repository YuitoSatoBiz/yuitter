package controllers

import javax.inject.Inject
import formats.MemberCommand
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import services.MemberService
import scala.concurrent.{ExecutionContext, Future}

/**
  * 会員のCRUD処理をするコントローラー
  *
  * @author yuito.sato
  */
class MemberController @Inject()(val memberService: MemberService)(implicit ec: ExecutionContext) extends Controller {

  /**
    * Memberを登録 POST /api/members
    *
    * @return 登録処理の結果
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

  /**
    * サインイン中の会員情報を取得 GET /api/my_page
    *
    * @return サインイン中の会員情報
    */
  def findCurrentMember: Action[AnyContent] = Action.async { implicit rs =>
    memberService.findCurrentMemberWithAccounts.map { member =>
      Ok(Json.toJson(member))
    }.recover { case e =>
      BadRequest(Json.obj("result" -> "failure", "error" -> e.getMessage))
    }
  }
}
