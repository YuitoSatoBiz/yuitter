package controllers

import javax.inject.Inject

import play.api.db.slick.DatabaseConfigProvider
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller

/**
  * TWEETテーブルにCRUD処理を行うコントローラー
  *
  * @author yuito.sato
  */
class TweetController @Inject()(val dbConfigProvider: DatabaseConfigProvider, val messagesApi: MessagesApi) extends Controller with I18nSupport {
}
