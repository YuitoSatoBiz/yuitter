package services

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.MemberCommand
import repositories.MemberRepositoryJDBC
import scala.concurrent.{ExecutionContext, Future}
import slick.driver.JdbcProfile

/**
  * Memberテーブルに対してDB接続をするクラス
  *
  * @author yuito.sato
  */
class MemberService @Inject()(val memberJdbc: MemberRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def create(form: MemberCommand): Future[(Long, Int)] = {
    for {
      countByEmailAddress <- db.run(memberJdbc.countByEmailAddress(form.emailAddress))
      createMember <- db.run(memberJdbc.create(form)) if countByEmailAddress.equals(0)
    } yield createMember
  }
}
