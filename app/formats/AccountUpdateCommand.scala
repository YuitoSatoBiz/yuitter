package formats

import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Reads}
import utils.Consts

/**
  * Accountテーブルにレコードを追加するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class AccountUpdateCommand(
  accountName: Option[String],
  avatar: Option[String],
  backgroundImage: Option[String],
  versionNo: Long
)

object AccountUpdateCommand {

  implicit val accountCommandReads: Reads[AccountUpdateCommand] = (
    (JsPath \ "accountName").readNullable[String](maxLength[String](Consts.AccountNameMaxLength)) and
    (JsPath \ "avatar").readNullable[String](maxLength[String](Consts.ImageMaxLength)) and
    (JsPath \ "backgroundImage").readNullable[String](maxLength[String](Consts.ImageMaxLength)) and
    (JsPath \ "versionNo").read[Long]
    ) (AccountUpdateCommand.apply _)
}
