package controllers

import javax.inject.Inject
import formats.{AccountCreateCommand, AccountUpdateCommand, KeywordCommand}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}
import security.AuthenticatedAction
import services.{AccountService, MemberService}
import scala.concurrent.{ExecutionContext, Future}

/**
  * AccountテーブルのCRUD処理をするコントローラー
  *
  * @author yuito.sato
  */
class AccountController @Inject()(val authenticatedAction: AuthenticatedAction, val accountService: AccountService, val memberService: MemberService)(implicit ec: ExecutionContext) extends Controller {

  /**
    * Accountの一覧取得 GET /api/accounts
    *
    * @return Action[AnyContent]
    */
  def list: Action[AnyContent] = authenticatedAction.async { implicit rs =>
    accountService.list.map { accounts =>
      Ok(Json.toJson(accounts))
    }
  }

  /**
    * アカウント名でアカウントを検索 POST /api/accounts/search
    *
    * @return 検索にマッチしたアカウント一覧
    */
  def search: Action[JsValue] = authenticatedAction.async(parse.json) { implicit rs =>
    rs.body.validate[KeywordCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountService.search(form).map { accounts =>
          Ok(Json.toJson(accounts))
        }
      }
    )
  }

  /**
    * フォローされているアカウント（フォロワー）一覧を取得 GET /api/accounts/followers/:accountId
    *
    * @param accountId フォローされているアカウントID
    * @return フォロワー一覧
    */
  def listFollowers(accountId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    accountService.listFollowers(accountId).map { followers =>
      Ok(Json.toJson(followers))
    }
  }

  /**
    * フォローしているアカウント（フォロイー）一覧を取得 GET /api/accounts/followees/:accountId
    *
    * @param accountId フォローしているアカウントID
    * @return フォロイー一覧
    */
  def listFollowees(accountId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    accountService.listFollowees(accountId).map { followers =>
      Ok(Json.toJson(followers))
    }
  }

  /**
    * IDでアカウントを検索 GET /api/accounts/:accountId
    *
    * @param accountId 検索を書けるID
    * @return 検索にマッチしたアカウント一覧
    */
  def find(accountId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    accountService.find(accountId).map {
      case Some(account) => Ok(Json.toJson(account))
      case None => BadRequest(Json.obj("result" -> "failure", "errors" -> "指定されたIDのアカウントは存在しません"))
    }
  }

  /**
    * アカウントを追加 POST /api/accounts
    *
    * @return 処理の結果
    */
  def create: Action[JsValue] = authenticatedAction.async(parse.json) { implicit rs =>
    rs.body.validate[AccountCreateCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountService.create(form).map {
          case Some(account) => Ok(Json.toJson(account))
          case None => BadRequest(Json.obj("result" -> "failure"))
        }
      }
    )
  }

  /**
    * アカウントを更新 PUT /api/accounts/:accountId
    *
    * @return 処理の結果
    */
  def update(accountId: Long): Action[JsValue] = authenticatedAction.async(parse.json) { implicit rs =>
    rs.body.validate[AccountUpdateCommand].fold(
      invalid = { e =>
        Future.successful(
          BadRequest(Json.obj("result" -> "failure", "error" -> JsError.toJson(e)))
        )
      },
      valid = { form =>
        accountService.update(accountId, form).map {
          case 0 => BadRequest(Json.obj("result" -> "failure", "error" -> "更新に失敗しました。"))
          case _ => Ok(Json.obj("result" -> "success"))
        }
      }
    )
  }

  /**
    * アカウントを削除 DELETE /api/accounts/:accountId
    *
    * @return 処理の結果
    */
  def delete(accountId: Long): Action[AnyContent] = authenticatedAction.async { implicit rs =>
    accountService.delete(accountId).map {
      case (0, 0) => BadRequest(Json.obj("result" -> "failure", "error" -> "削除に失敗しました。"))
      case (at, a) => Ok(Json.obj("result" -> "success", "error" -> ("AccountTweet削除:" + at.toString + "件" + "Account削除:" + a.toString + "件")))
    }
  }
}
