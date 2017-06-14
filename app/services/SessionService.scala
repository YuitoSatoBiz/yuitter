package services

import javax.inject.Inject

import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.MemberCommand
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import repositories.MemberRepositoryJDBC
import slick.driver.JdbcProfile
import slick.driver.MySQLDriver.api._

import scala.concurrent.Future

/**
  * @author yuito.sato
  */
class SessionService @Inject()(val memberJdbc: MemberRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val bCrypt: BCryptPasswordEncoder) extends HasDatabaseConfigProvider[JdbcProfile] {

  def authenticate(form: MemberCommand): Future[Boolean] = {
//    val future = for {
//      optMember <- db.run(memberJdbc.findByEmailAddress(form.emailAddress))
//      member <- optMember.getOrElse{ throw new IllegalArgumentException("error") }
//    } yield bCrypt.matches(form.password, optMember.getOrElse().password)
    for {
      optMember <- db.run(memberJdbc.findByEmailAddress(form.emailAddress))
      member <- optMember
    } yield bCrypt.matches(form.password, member.password)
  }
}
