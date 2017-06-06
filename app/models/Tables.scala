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
  lazy val schema: profile.SchemaDescription = Member.schema ++ MemberFollowing.schema ++ Tweet.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Member
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberName Database column MEMBER_NAME SqlType(VARCHAR), Length(50,true)
   *  @param emailAddress Database column EMAIL_ADDRESS SqlType(VARCHAR), Length(50,true)
   *  @param memberAccount Database column MEMBER_ACCOUNT SqlType(VARCHAR), Length(50,true)
   *  @param memberAvatar Database column MEMBER_AVATAR SqlType(VARCHAR), Length(200,true)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberRow(memberId: Long, memberName: String, emailAddress: String, memberAccount: String, memberAvatar: String, registerDatetime: java.sql.Timestamp, updateDatetime: java.sql.Timestamp, versionNo: Long)
  /** GetResult implicit for fetching MemberRow objects using plain SQL queries */
  implicit def GetResultMemberRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[MemberRow] = GR{
    prs => import prs._
    MemberRow.tupled((<<[Long], <<[String], <<[String], <<[String], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table MEMBER. Objects of this class serve as prototypes for rows in queries. */
  class Member(_tableTag: Tag) extends Table[MemberRow](_tableTag, "MEMBER") {
    def * = (memberId, memberName, emailAddress, memberAccount, memberAvatar, registerDatetime, updateDatetime, versionNo) <> (MemberRow.tupled, MemberRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberId), Rep.Some(memberName), Rep.Some(emailAddress), Rep.Some(memberAccount), Rep.Some(memberAvatar), Rep.Some(registerDatetime), Rep.Some(updateDatetime), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_NAME SqlType(VARCHAR), Length(50,true) */
    val memberName: Rep[String] = column[String]("MEMBER_NAME", O.Length(50,varying=true))
    /** Database column EMAIL_ADDRESS SqlType(VARCHAR), Length(50,true) */
    val emailAddress: Rep[String] = column[String]("EMAIL_ADDRESS", O.Length(50,varying=true))
    /** Database column MEMBER_ACCOUNT SqlType(VARCHAR), Length(50,true) */
    val memberAccount: Rep[String] = column[String]("MEMBER_ACCOUNT", O.Length(50,varying=true))
    /** Database column MEMBER_AVATAR SqlType(VARCHAR), Length(200,true) */
    val memberAvatar: Rep[String] = column[String]("MEMBER_AVATAR", O.Length(200,varying=true))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Index over (memberAccount) (database name IX_MEMBER_MEMBER_ACCOUNT) */
    val index1 = index("IX_MEMBER_MEMBER_ACCOUNT", memberAccount)
    /** Index over (memberName) (database name IX_MEMBER_MEMBER_NAME) */
    val index2 = index("IX_MEMBER_MEMBER_NAME", memberName)
    /** Uniqueness Index over (memberAccount) (database name MEMBER_ACCOUNT) */
    val index3 = index("MEMBER_ACCOUNT", memberAccount, unique=true)
  }
  /** Collection-like TableQuery object for table Member */
  lazy val Member = new TableQuery(tag => new Member(tag))

  /** Entity class storing rows of table MemberFollowing
   *  @param memberFollowingId Database column MEMBER_FOLLOWING_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param followerId Database column FOLLOWER_ID SqlType(BIGINT)
   *  @param followeeId Database column FOLLOWEE_ID SqlType(BIGINT)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class MemberFollowingRow(memberFollowingId: Long, followerId: Long, followeeId: Long, registerDatetime: java.sql.Timestamp, updateDatetime: java.sql.Timestamp, versionNo: Long)
  /** GetResult implicit for fetching MemberFollowingRow objects using plain SQL queries */
  implicit def GetResultMemberFollowingRow(implicit e0: GR[Long], e1: GR[java.sql.Timestamp]): GR[MemberFollowingRow] = GR{
    prs => import prs._
    MemberFollowingRow.tupled((<<[Long], <<[Long], <<[Long], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table MEMBER_FOLLOWING. Objects of this class serve as prototypes for rows in queries. */
  class MemberFollowing(_tableTag: Tag) extends Table[MemberFollowingRow](_tableTag, "MEMBER_FOLLOWING") {
    def * = (memberFollowingId, followerId, followeeId, registerDatetime, updateDatetime, versionNo) <> (MemberFollowingRow.tupled, MemberFollowingRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(memberFollowingId), Rep.Some(followerId), Rep.Some(followeeId), Rep.Some(registerDatetime), Rep.Some(updateDatetime), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> MemberFollowingRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column MEMBER_FOLLOWING_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val memberFollowingId: Rep[Long] = column[Long]("MEMBER_FOLLOWING_ID", O.AutoInc, O.PrimaryKey)
    /** Database column FOLLOWER_ID SqlType(BIGINT) */
    val followerId: Rep[Long] = column[Long]("FOLLOWER_ID")
    /** Database column FOLLOWEE_ID SqlType(BIGINT) */
    val followeeId: Rep[Long] = column[Long]("FOLLOWEE_ID")
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_MEMBER_FOLLOWEE) */
    lazy val memberFk1 = foreignKey("FK_MEMBER_FOLLOWEE", followeeId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Member (database name FK_MEMBER_FOLLOWER) */
    lazy val memberFk2 = foreignKey("FK_MEMBER_FOLLOWER", followerId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table MemberFollowing */
  lazy val MemberFollowing = new TableQuery(tag => new MemberFollowing(tag))

  /** Entity class storing rows of table Tweet
   *  @param tweetId Database column TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param memberId Database column MEMBER_ID SqlType(BIGINT)
   *  @param tweetText Database column TWEET_TEXT SqlType(VARCHAR), Length(140,true)
   *  @param registerDatetime Database column REGISTER_DATETIME SqlType(DATETIME)
   *  @param updateDatetime Database column UPDATE_DATETIME SqlType(DATETIME)
   *  @param versionNo Database column VERSION_NO SqlType(BIGINT) */
  case class TweetRow(tweetId: Long, memberId: Long, tweetText: String, registerDatetime: java.sql.Timestamp, updateDatetime: java.sql.Timestamp, versionNo: Long)
  /** GetResult implicit for fetching TweetRow objects using plain SQL queries */
  implicit def GetResultTweetRow(implicit e0: GR[Long], e1: GR[String], e2: GR[java.sql.Timestamp]): GR[TweetRow] = GR{
    prs => import prs._
    TweetRow.tupled((<<[Long], <<[Long], <<[String], <<[java.sql.Timestamp], <<[java.sql.Timestamp], <<[Long]))
  }
  /** Table description of table TWEET. Objects of this class serve as prototypes for rows in queries. */
  class Tweet(_tableTag: Tag) extends Table[TweetRow](_tableTag, "TWEET") {
    def * = (tweetId, memberId, tweetText, registerDatetime, updateDatetime, versionNo) <> (TweetRow.tupled, TweetRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(tweetId), Rep.Some(memberId), Rep.Some(tweetText), Rep.Some(registerDatetime), Rep.Some(updateDatetime), Rep.Some(versionNo)).shaped.<>({r=>import r._; _1.map(_=> TweetRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column TWEET_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val tweetId: Rep[Long] = column[Long]("TWEET_ID", O.AutoInc, O.PrimaryKey)
    /** Database column MEMBER_ID SqlType(BIGINT) */
    val memberId: Rep[Long] = column[Long]("MEMBER_ID")
    /** Database column TWEET_TEXT SqlType(VARCHAR), Length(140,true) */
    val tweetText: Rep[String] = column[String]("TWEET_TEXT", O.Length(140,varying=true))
    /** Database column REGISTER_DATETIME SqlType(DATETIME) */
    val registerDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("REGISTER_DATETIME")
    /** Database column UPDATE_DATETIME SqlType(DATETIME) */
    val updateDatetime: Rep[java.sql.Timestamp] = column[java.sql.Timestamp]("UPDATE_DATETIME")
    /** Database column VERSION_NO SqlType(BIGINT) */
    val versionNo: Rep[Long] = column[Long]("VERSION_NO")

    /** Foreign key referencing Member (database name FK_TWEET_MEMBER) */
    lazy val memberFk = foreignKey("FK_TWEET_MEMBER", memberId, Member)(r => r.memberId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Tweet */
  lazy val Tweet = new TableQuery(tag => new Tweet(tag))
}
