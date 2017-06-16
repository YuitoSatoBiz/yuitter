package formats

import play.api.libs.json.{Json, Reads}

/**
  * 検索フォームのケースクラス
  *
  * @author yuito.sato
  */

case class KeywordCommand(
  keyword: String
)

object KeywordCommand {

  implicit val keywordCommandReads: Reads[KeywordCommand] = Json.reads[KeywordCommand]
}
