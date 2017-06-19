package utils

import scala.concurrent.duration._

/**
  * アプリ上で使用する定数を管理するオブジェクト
  *
  * @author yuito.sato
  */
object Consts {

  val DefaultId = 0L
  val DefaultVersionNo = 0L
  val PasswordMinLength = 8
  val EmailAddressMaxLength = 50
  val CacheRetentionPeriod = 30.days

}
