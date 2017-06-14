package services

import javax.inject.Inject
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import formats.MemberCommand
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import repositories.MemberRepositoryJDBC
import slick.driver.JdbcProfile
import models.Tables.Member
import scala.concurrent.{ExecutionContext, Future}

/**
  * @author yuito.sato
  */
class SessionService @Inject()(val memberJdbc: MemberRepositoryJDBC, val dbConfigProvider: DatabaseConfigProvider, val bCrypt: BCryptPasswordEncoder)(implicit ec: ExecutionContext) extends HasDatabaseConfigProvider[JdbcProfile] {

  def authenticate(form: MemberCommand): Future[Member#TableElementType] = {
    for {
      optMember <- db.run(memberJdbc.findByEmailAddress(form.emailAddress))
      member <- Future.successful(optMember.getOrElse(throw new Exception))
      member <- Future.successful(member) if bCrypt.matches(form.password, member.password)
    } yield member
  }
}
