package controllers

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, Controller}

import scala.concurrent.Future
/**
  * 会員のCRUD処理をするコントローラー
  *
  * @author yuito.sato
  */
class MemberController {

  /**
    * Memberを登録
    */
  def create: Action[JsValue] = ???
}
