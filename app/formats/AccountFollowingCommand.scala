package formats

import play.api.libs.json.{Json, Reads}

/**
  * @author yuito.sato
  */
case class AccountFollowingCommand(
  followeeId: Long
)

object AccountFollowingCommand {

  implicit val accountFollowingCommandReads: Reads[AccountFollowingCommand] = Json.reads[AccountFollowingCommand]
}
