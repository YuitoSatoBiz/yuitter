package formats

import play.api.libs.json.Reads.maxLength
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}
import utils.Consts

/**
  * Accountテーブルにレコードを追加するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class AccountCreateCommand(
  accountName: String,
  avatar: Option[String],
  backgroundImage: Option[String]
)

object AccountCreateCommand {

  implicit val accountCommandReads: Reads[AccountCreateCommand] = (
    (JsPath \ "accountName").read[String](maxLength[String](Consts.AccountNameMaxLength)) and
    (JsPath \ "avatar").readNullable[String](maxLength[String](Consts.ImageMaxLength)) and
    (JsPath \ "backgroundImage").readNullable[String](maxLength[String](Consts.ImageMaxLength))
  ) (AccountCreateCommand.apply _)
}
