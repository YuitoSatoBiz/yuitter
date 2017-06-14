package formats

import play.api.libs.json.{Json, Reads}

/**
  * MEMBERテーブルに関するフォームのケースクラス
  *
  * @author yuito.sato
  */
case class MemberCommand (
  emailAddress: String,
  password: String,
  versionNo: Option[Long],
  account: Option[AccountCommand]
)

object MemberCommand {

  implicit val memberCommandReads: Reads[MemberCommand] = Json.reads[MemberCommand]
}
