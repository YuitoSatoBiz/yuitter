package formats

import play.api.libs.json.{Json, OWrites}
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

  implicit val accountViewWrites: OWrites[AccountView] = Json.writes[AccountView]

  def from(a: Account#TableElementType): AccountView =
    AccountView(
      accountId = a.accountId,
      accountName = a.accountName,
      avatar = a.avatar,
      backgroundImage = a.backgroundImage
    )
}
