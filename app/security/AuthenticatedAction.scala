package security

import javax.inject.Inject
import play.api.mvc.{ActionBuilder, Request, Result, Results}
import services.MemberService
import play.api.libs.json._
import scala.concurrent.Future

/**
  * サインイン認証後のアクションビルダー
  *
  * @author yuito.sato
  */
class AuthenticatedAction @Inject()(val memberService: MemberService) extends ActionBuilder[AuthenticatedRequest] {

  override def invokeBlock[A](request: Request[A], block: (AuthenticatedRequest[A]) => Future[Result]): Future[Result] = {
    val optMemberId = memberService.findCurrentMemberId(request)
    if (optMemberId.nonEmpty) {
      block(new AuthenticatedRequest(optMemberId.get, request))
    } else {
      Future.successful(Results.BadRequest(Json.obj("result" -> "failure", "error" -> "ログインが必要です")))
    }
  }
}
