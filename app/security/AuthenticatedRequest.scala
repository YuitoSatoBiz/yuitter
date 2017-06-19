package security

import play.api.mvc.{Request, WrappedRequest}

/**
  * サインイン認証承認後のリクエスト
  *
  * @author yuito.sato
  */
class AuthenticatedRequest[A](val memberId: Long, request: Request[A]) extends WrappedRequest[A](request)
