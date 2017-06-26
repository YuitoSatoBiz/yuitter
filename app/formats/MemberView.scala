package formats

import play.api.libs.json.{Json, Writes}
import models.Tables.Member

/**
  * クライアントで表示用のMEMBERテーブルのケースクラス
  *
  * @author yuito.sato
  */
case class MemberView(
  memberId: Long,
  emailAddress: String,
  password: String,
  accounts: Seq[AccountView]
)

object MemberView {

  implicit val memberViewWrites: Writes[MemberView] = Json.writes[MemberView] 

  def from(member: Member#TableElementType, accounts: Seq[AccountView]): MemberView = {
    MemberView(
      memberId = member.memberId,
      emailAddress = member.emailAddress,
      password = member.password,
      accounts = accounts
    )
  }
}
