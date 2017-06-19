package formats

import play.api.libs.json.{Json, Writes}
import models.Tables.Account

/**
  * クライアントで表示用のAccountTableのケースクラス
  *
  * @author yuito.sato
  */
case class AccountView(
  accountId: Long,
  accountName: String,
  avatar: Option[String],
  backgroundImage: Option[String]
)

object AccountView {

  implicit val accountViewWrites: Writes[AccountView] = Json.writes[AccountView]

  def from(account: Account#TableElementType): AccountView = {
    AccountView(
      accountId = account.accountId,
      accountName = account.accountName,
      avatar = account.avatar,
      backgroundImage = account.backgroundImage
    )
  }
}
