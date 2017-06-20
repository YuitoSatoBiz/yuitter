package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.MySQLDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Account.schema ++ AccountFollowing.schema ++ AccountTweet.schema ++ Member.schema ++ Tweet.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Account
   *  @param accountId Database column ACCOUNT_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT)
   *  @param accountName Database column ACCOUNT_NAME SqlType(VARCHAR), Length(50,true)
   *  @param avatar Database column AVATAR SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param backgroundImage Database column BACKGROUND_IMAGE SqlType(VARCHAR), Length(200,true), Default(None)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class AccountRow(accountId: Long, memberId: Long, accountName: String, avatar: Option[String] = None, backgroundImage: Option[String] = None, registerDatetime: java.sql.Timestamp, updateDatetime: java.sql.Timestamp, versionNo: Long)
  /** GetResult implicit for fetching AccountRow objects using plain SQL queries */
  implicit def GetResultAccountRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Option[String]], e3: GR[java.sql.Timestamp]): GR[AccountRow] = GR{
    prs => import prs._
    AccountRow.tupled((<<[Long], <<[Long], <<[String], <<?[String], <<?[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table ACCOUNT. Objects of this class serve as prototypes for rows in queries. */
  class Account(_tableTag: Tag) extends Table[AccountRow](_tableTag, "ACCOUNT") {
    def * = (accountId, memberId, accountName, avatar, backgroundImage, registerDatetime, updateDatetime, versionNo) <> (AccountRow.tupled, AccountRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountId), Rep.Some(memberId), Rep.Some(accountName), avatar, backgroundImage, Rep.Some(registerDatetime), Rep.Some(updateDatetime), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> AccountRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ACCOUNT_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val accountId: Rep[Long] = column[Long]("ACCOUNT_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(BIGINT) */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID")
    /** Database column ACCOUNT_NAME SqlType(VARCHAR), Length(50,true) */
    val accountName: Rep[String] = column[String]("ACCOUNT_NAME", O.Length(50,varying=true))
    /** Database column AVATAR SqlType(VARCHAR), Length(200,true), Default(None) */
    val avatar: Rep[Option[String]] = column[Option[String]]("AVATAR", O.Length(200,varying=true), O.Default(None))
    /** Database column BACKGROUND_IMAGE SqlType(VARCHAR), Length(200,true), Default(None) */
    val backgroundImage: Rep[Option[String]] = column[Option[String]]("BACKGROUND_IMAGE", O.Length(200,varying=true), O.Default(None))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_ACCOUNT_MEMBER) */
    lazy val memberFk = foreignKey("FK_ACCOUNT_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Account */
  lazy val Account = new TableQuery(tag => new Account(tag))

  /** Entity class storing rows of table AccountFollowing
   *  @param accountFollowingId Database column ACCOUNT_FOLLOWING_ID SqlType(BIGINT), PrimaryKey
   *  @param followerId Database column FOLLOWER_ID SqlType(BIGINT)
   *  @param followeeId Database column FOLLOWEE_ID SqlType(BIGINT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME) */
  case class AccountFollowingRow(accountFollowingId: Long, followerId: Long, followeeId: Long, registerDatetime: java.sql.Timestamp)
  /** GetResult implicit for fetching AccountFollowingRow objects using plain SQL queries */
  implicit def GetResultAccountFollowingRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[AccountFollowingRow] = GR{
    prs => import prs._
    AccountFollowingRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table ACCOUNT_FOLLOWING. Objects of this class serve as prototypes for rows in queries. */
  class AccountFollowing(_tableTag: Tag) extends Table[AccountFollowingRow](_tableTag, "ACCOUNT_FOLLOWING") {
    def * = (accountFollowingId, followerId, followeeId, registerDatetime) <> (AccountFollowingRow.tupled, AccountFollowingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountFollowingId), Rep.Some(followerId), Rep.Some(followeeId), Rep.Some(registerDatetime)).shaped.<>({r=>import r._; _1.map(_=> AccountFollowingRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ACCOUNT_FOLLOWING_ID SqlType(BIGINT), PrimaryKey */
    val accountFollowingId: Rep[Long] = column[Long]("ACCOUNT_FOLLOWING_ID", O.PrimaryKey)
    /** Database column FOLLOWER_ID SqlType(BIGINT) */
    val followerId: Rep[Long] = column[Long]("FOLLOWER_ID")
    /** Database column FOLLOWEE_ID SqlType(BIGINT) */
    val followeeId: Rep[Long] = column[Long]("FOLLOWEE_ID")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")

    /** Foreign key referencing Account (database name FK_ACCOUNT_FOLLOWEE_ACCOUNT) */
    lazy val accountFk1 = foreignKey("FK_ACCOUNT_FOLLOWEE_ACCOUNT", followeeId, Account)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Account (database name FK_FOLLOWER_ACCOUNT) */
    lazy val accountFk2 = foreignKey("FK_FOLLOWER_ACCOUNT", followerId, Account)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AccountFollowing */
  lazy val AccountFollowing = new TableQuery(tag => new AccountFollowing(tag))

  /** Entity class storing rows of table AccountTweet
   *  @param accountTweetId Database column ACCOUNT_TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param accountId Database column ACCOUNT_ID SqlType(BIGINT)
   *  @param tweetId Database column TWEET_ID SqlType(BIGINT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME) */
  case class AccountTweetRow(accountTweetId: Long, accountId: Long, tweetId: Long, registerDatetime: java.sql.Timestamp)
  /** GetResult implicit for fetching AccountTweetRow objects using plain SQL queries */
  implicit def GetResultAccountTweetRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[AccountTweetRow] = GR{
    prs => import prs._
    AccountTweetRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp]))
  }
  /** Table description of table ACCOUNT_TWEET. Objects of this class serve as prototypes for rows in queries. */
  class AccountTweet(_tableTag: Tag) extends Table[AccountTweetRow](_tableTag, "ACCOUNT_TWEET") {
    def * = (accountTweetId, accountId, tweetId, registerDatetime) <> (AccountTweetRow.tupled, AccountTweetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(accountTweetId), Rep.Some(accountId), Rep.Some(tweetId), Rep.Some(registerDatetime)).shaped.<>({r=>import r._; _1.map(_=> AccountTweetRow.tupled((_1.get, _2.get, _3.get, _4.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ACCOUNT_TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val accountTweetId: Rep[Long] = column[Long]("ACCOUNT_TWEET_ID", O.AutoInc, O.PrimaryKey)
    /** Database column ACCOUNT_ID SqlType(BIGINT) */
    val accountId: Rep[Long] = column[Long]("ACCOUNT_ID")
    /** Database column TWEET_ID SqlType(BIGINT) */
    val tweetId: Rep[Long] = column[Long]("TWEET_ID")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")

    /** Foreign key referencing Account (database name FK_ACCOUNT_TWEET_ACCOUNT) */
    lazy val accountFk = foreignKey("FK_ACCOUNT_TWEET_ACCOUNT", accountId, Account)(r => r.accountId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Tweet (database name FK_ACCOUNT_TWEET_TWEET) */
    lazy val tweetFk = foreignKey("FK_ACCOUNT_TWEET_TWEET", tweetId, Tweet)(r => r.tweetId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table AccountTweet */
  lazy val AccountTweet = new TableQuery(tag => new AccountTweet(tag))

  /** Entity class storing rows of table Member
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param emailAddress Database column EMAIL_ADDRESS SqlType(VARCHAR), Length(50,true)
   *  @param password Database column PASSWORD SqlType(CHAR), Length(60,false)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberRow(memberId: Long, emailAddress: String, password: String, registerDatetime: java.sql.Timestamp, updateDatetime: java.sql.Timestamp, versionNo: Long)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Long], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table MEMBER. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends Table[MemberRow](_tableTag, "MEMBER") {
    def * = (memberId, emailAddress, password, registerDatetime, updateDatetime, versionNo) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(emailAddress), Rep.Some(password), Rep.Some(registerDatetime), Rep.Some(updateDatetime), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID", O.AutoInc, O.PrimaryKey)
    /** Database column EMAIL_ADDRESS SqlType(VARCHAR), Length(50,true) */
    val emailAddress: Rep[String] = column[String]("EMAIL_ADDRESS", O.Length(50,varying=true))
    /** Database column PASSWORD SqlType(CHAR), Length(60,false) */
    val password: Rep[String] = column[String]("PASSWORD", O.Length(60,varying=false))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Uniqueness Index over (emailAddress) (database name EMAIL_ADDRESS) */
    val index1 = index("EMAIL_ADDRESS", emailAddress, unique=true)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table Tweet
   *  @param tweetId Database column TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param tweetText Database column TWEET_TEXT SqlType(VARCHAR), Length(140,true)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class TweetRow(tweetId: Long, tweetText: String, registerDatetime: java.sql.Timestamp, updateDatetime: java.sql.Timestamp, versionNo: Long)
  /** GetResult implicit for fetching TweetRow objects using plain SQL queries */
  implicit def GetResultTweetRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TweetRow] = GR{
    prs => import prs._
    TweetRow.tupled((<<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table TWEET. Objects of this class serve as prototypes for rows in queries. */
  class Tweet(_tableTag: Tag) extends Table[TweetRow](_tableTag, "TWEET") {
    def * = (tweetId, tweetText, registerDatetime, updateDatetime, versionNo) <> (TweetRow.tupled, TweetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(tweetId), Rep.Some(tweetText), Rep.Some(registerDatetime), Rep.Some(updateDatetime), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> TweetRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val tweetId: Rep[Long] = column[Long]("TWEET_ID", O.AutoInc, O.PrimaryKey)
    /** Database column TWEET_TEXT SqlType(VARCHAR), Length(140,true) */
    val tweetText: Rep[String] = column[String]("TWEET_TEXT", O.Length(140,varying=true))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Index over (registerDatetime) (database name IX_TWEET_REGISTER_DATETIME) */
    val index1 = index("IX_TWEET_REGISTER_DATETIME", registerDatetime)
  }
  /** Collection-like TableQuery object for table Tweet */
  lazy val Tweet = new TableQuery(tag => new Tweet(tag))
}
