package formats

import play.api.libs.json.{Json, Reads}

/**
  * Accountテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
class AccountCommand(
  accountName: String,
  avatar: Option[String],
  backgroundImage: Option[String],
  versionNo: Option[Long]
)

object AccountCommand {

  implicit val accountCommandReads: Reads[AccountCommand] = Json.reads[AccountCommand]
}
