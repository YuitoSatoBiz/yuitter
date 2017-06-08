package services

import javax.inject.Inject

import formats.TweetWithMemberView
import repositories.TweetRepositoryJDBC

import scala.concurrent.Future

/**
  * @author yuito.sato
  */
class TweetService @Inject()(val tweetJdbc: TweetRepositoryJDBC) {

  def list(): Future[Seq[TweetWithMemberView]] = {
    tweetJdbc.listWithMember()
  }
}
