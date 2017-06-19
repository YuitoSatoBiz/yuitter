package formats

import play.api.libs.json.{JsPath, Reads}
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._
import utils.Consts

/**
  * MEMBERテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class MemberCommand(
  emailAddress: String,
  password: String,
  versionNo: Option[Long],
  account: Option[AccountCommand]
)

object MemberCommand {

  implicit val memberCommandReads: Reads[MemberCommand] = (
    (JsPath \ "emailAddress").read[String](maxLength[String](Consts.EmailAddressMaxLength)) and
    (JsPath \ "password").read[String](minLength[String](Consts.PasswordMinLength)) and
    (JsPath \ "versionNo").readNullable[Long] and
    (JsPath \ "account").readNullable[AccountCommand]
    ) (MemberCommand.apply _)
}
