
package database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Create the used tables and some datas
 */
public class CreateTables {
  Connection conn;
  DBConnection db;

  public CreateTables() {
    db = new DBConnection();
    conn = db.getConnection();
    createTables();
  }

  private void createTables() {
    Statement stm = null;
    String sql = "";

    try {
      // Setting user, forum, moderator, thread, post, message, block user
      // friend and group tables.
      stm = conn.createStatement();
      // User table
      String userT = "create table if not exists " + Constant.USER_TABLE + " ("
          + Constant.USER_USER_ID_FIELD_NAME + " int auto_increment not null, "
          + Constant.USER_NAME_FIELD_NAME + " tinytext, "
          + Constant.USER_SURNAME_NAME_FIELD_NAME + " tinytext, "
          + Constant.USER_EMAIL_FIELD_NAME + " tinytext not null, "
          + Constant.USER_COUNTRY_FIELD_NAME + " tinytext, "
          + Constant.USER_STATE_FIELD_NAME + " tinytext, "
          + Constant.USER_CITY_FIELD_NAME + " tinytext, "
          + Constant.USER_USER_NAME_FIELD_NAME + " tinytext not null, "
          + Constant.USER_PASSWORD_FIELD_NAME + " text not null, "
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + " text not null, "
          + Constant.USER_IS_MOD_FIELD_NAME + " tinyint not null default 0, "
          + Constant.USER_DELETED_FIELD_NAME + " tinyint not null default 0, "
          + "primary key(" + Constant.USER_USER_ID_FIELD_NAME
          + "), unique key (" + Constant.USER_USER_NAME_FIELD_NAME + "(30), "
          + Constant.USER_EMAIL_FIELD_NAME + "(75)));";
      sql = userT;
      stm.executeUpdate(userT);
      // Forum table
      String forumT = "create table if not exists " + Constant.FORUM_TABLE
          + " (" + Constant.FORUM_FORUM_ID_FIELD_NAME
          + " int not null auto_increment, " + Constant.FORUM_NAME_FIELD_NAME
          + " tinytext not null, " + Constant.FORUM_DESCRIPTION_FIELD_NAME
          + " text, " + Constant.FORUM_CATEGORY_ID_FIELD_NAME
          + " int, primary key(" + Constant.FORUM_FORUM_ID_FIELD_NAME + "));";
      sql = forumT;
      stm.executeUpdate(forumT);
      // Moderator table
      String moderatorT = "create table if not exists "
          + Constant.MODERATOR_TABLE + " ("
          + Constant.MODERATOR_MOD_ID_FIELD_NAME
          + " int auto_increment not null, "
          + Constant.MODERATOR_FORUM_ID_FIELD_NAME + " int, "
          + Constant.MODERATOR_USER_ID_FIELD_NAME
          + " int not null, primary key(" + Constant.MODERATOR_MOD_ID_FIELD_NAME
          + "));";
      sql = moderatorT;
      stm.executeUpdate(moderatorT);
      // Thread table
      String threadT = "create table if not exists " + Constant.THREAD_TABLE
          + " (" + Constant.THREAD_THREAD_ID_FIELD_NAME
          + " int auto_increment not null, "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + " int not null, "
          + Constant.THREAD_NAME_FIELD_NAME + " tinytext not null, "
          + Constant.THREAD_USER_ID_FIELD_NAME + " int not null, primary key("
          + Constant.THREAD_THREAD_ID_FIELD_NAME + "));";
      sql = threadT;
      stm.executeUpdate(threadT);
      // Post table
      String postT = "create table if not exists " + Constant.POST_TABLE + " ("
          + Constant.POST_POST_ID_FIELD_NAME + " int auto_increment not null, "
          + Constant.POST_USER_ID_FIELD_NAME + " int not null, "
          + Constant.POST_FORUM_ID_FIELD_NAME + ""
          + Constant.POST_THREAD_ID_FIELD_NAME + " int not null, "
          + Constant.POST_POST_FIELD_NAME + " mediumblob not null, "
          + Constant.POST_CREATION_DATE_FIELD_NAME + " datetime not null, "
          + Constant.POST_MODIFICATION_DATE_FIELD_NAME + " datetime, "
          + Constant.POST_DELETED_FIELD_NAME + " tinyint not null default 0, "
          + "primary key(" + Constant.POST_POST_ID_FIELD_NAME + "));";
      sql = postT;
      stm.executeUpdate(postT);
      // Message table
      String messageT = "create table if not exists " + Constant.MESSAGE_TABLE
          + " (" + Constant.MESSAGE_MESSAGE_ID_FIELD_NAME
          + " int auto_increment not null, "
          + Constant.MESSAGE_MESSAGE_FIELD_NAME + " mediumblob not null, "
          + Constant.MESSAGE_SENDER_FIELD_NAME + " int, "
          + Constant.MESSAGE_RECEIVER_FIELD_NAME + " int, "
          + Constant.MESSAGE_GROUP_ID_FIELD_NAME + " int, "
          + Constant.MESSAGE_CREATION_DATE_FIELD_NAME + " date not null, "
          + "primary key(" + Constant.MESSAGE_MESSAGE_ID_FIELD_NAME + "));";
      sql = messageT;
      stm.executeUpdate(messageT);
      // Blocked table
      String blockedUserT = "create table if not exists "
          + Constant.BLOCKED_USER_TABLE + " ("
          + Constant.BLOCKED_BLOCK_ID_FIELD_NAME
          + " int auto_increment not null, "
          + Constant.BLOCKED_USER_ID_FIELD_NAME + " int not null, "
          + Constant.BLOCKED_BLOCKED_USER_ID_FIELD_NAME + " int not null, "
          + "primary key(" + Constant.BLOCKED_BLOCK_ID_FIELD_NAME + "));";
      sql = blockedUserT;
      stm.executeUpdate(blockedUserT);
      // Friend table
      String friendT = "create table if not exists " + Constant.FRIEND_TABLE
          + " (" + Constant.FRIEND_FRIEND_ID_FIELD_NAME
          + " int auto_increment not null, "
          + Constant.FRIEND_USER_ID_FIELD_NAME + " int not null, "
          + Constant.FRIEND_FRIEND_USER_ID_FIELD_NAME + " int not null, "
          + "primary key(" + Constant.FRIEND_FRIEND_ID_FIELD_NAME + "));";
      stm.executeUpdate(friendT);
      // Group table
      String groupT = "create table if not exists " + Constant.GROUP_TABLE
          + " (" + Constant.GROUP_GROUP_ID_FIELD_NAME
          + " int auto_increment not null, " + Constant.GROUP_NAME_FIELD_NAME
          + " tinytext not null, " + Constant.GROUP_FRIENDS_ONLY_FIELD_NAME
          + " tinyint not null, primary key("
          + Constant.GROUP_GROUP_ID_FIELD_NAME + "));";
      sql = groupT;
      stm.executeUpdate(groupT);

      // Creating indexes
      // SQL sentence to check and create an index for user_id and friend_id
      String indexNameFriendSearch = "index_friend_search";
      String checkFriendSearch = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.FRIEND_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameFriendSearch + "\"";
      String sqlIndexFriendSearch = "create index " + indexNameFriendSearch
          + " on " + Constant.FRIEND_TABLE + " ("
          + Constant.FRIEND_USER_ID_FIELD_NAME + ", "
          + Constant.FRIEND_FRIEND_ID_FIELD_NAME + ")";
      ResultSet rs = stm.executeQuery(checkFriendSearch);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexFriendSearch);
      }
      // SQL sentence to check and create an index for forum_id on moderator
      // table
      String indexNameForumIdMod = "index_forum_id_mod";
      String checkForumIdMod = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.MODERATOR_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameForumIdMod + "\"";
      String sqlIndexForumIdMod = "create index " + indexNameForumIdMod + " on "
          + Constant.MODERATOR_TABLE + " ("
          + Constant.MODERATOR_FORUM_ID_FIELD_NAME + ")";
      rs = stm.executeQuery(checkForumIdMod);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexForumIdMod);
      }
      // SQL sentence to check and create an index for is_mod and user_id search
      String indexNameIsModUserIdName = "index_is_mod_user_id";
      String checkIsModUserId = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.USER_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameIsModUserIdName + "\"";
      String sqlIndexIsModUserId = "create index " + indexNameIsModUserIdName
          + " on " + Constant.USER_TABLE + " ("
          + Constant.USER_IS_MOD_FIELD_NAME + ", "
          + Constant.USER_USER_ID_FIELD_NAME + ")";
      rs = stm.executeQuery(checkIsModUserId);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexIsModUserId);
      }
      // SQL sentence to check and create an index for forum_id on moderator
      // table
      String indexNameBlockUser = "index_block_user";
      String checkBlockUser = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.BLOCKED_USER_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameBlockUser + "\"";
      String sqlIndexBlockUser = "create index " + indexNameBlockUser + " on "
          + Constant.BLOCKED_USER_TABLE + " ("
          + Constant.BLOCKED_USER_ID_FIELD_NAME + ", "
          + Constant.BLOCKED_BLOCKED_USER_ID_FIELD_NAME + ")";
      rs = stm.executeQuery(checkBlockUser);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexBlockUser);
      }
      // SQL sentence to check and create an index for email on user table
      String indexNamePostForumId = "index_post_forum_id";
      String checkIndexPostForumId = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.POST_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNamePostForumId + "\"";
      String sqlIndexPostForumId = "alter table " + Constant.POST_TABLE
          + " add index " + indexNamePostForumId + " ("
          + Constant.POST_FORUM_ID_FIELD_NAME + ")";
      rs = stm.executeQuery(checkIndexPostForumId);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexPostForumId);
      }
      // SQL sentence to check and create an index for user_name and password on
      // user table
      String indexNameUserNamePass = "index_user_name_pass";
      String checkUserNamePass = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.USER_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameUserNamePass + "\"";
      String sqlIndexUserNamePass = "create index " + indexNameUserNamePass
          + " on " + Constant.USER_TABLE + " ("
          + Constant.USER_USER_NAME_FIELD_NAME + "(30), "
          + Constant.USER_PASSWORD_FIELD_NAME + "(255))";
      rs = stm.executeQuery(checkUserNamePass);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexUserNamePass);
      }
      // SQL sentence to check and create an index for user_name on user table
      String indexNameUserName = "index_user_name";
      String checkUserName = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.USER_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameUserName + "\"";
      String sqlIndexUserName = "create index index_user_name on "
          + Constant.USER_TABLE + " (" + Constant.USER_USER_NAME_FIELD_NAME
          + "(30))";
      rs = stm.executeQuery(checkUserName);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexUserName);
      }
      // SQL sentence to check and create an index for email on user table
      String indexNameEmail = "index_email";
      String checkEmail = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.USER_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameEmail + "\"";
      String sqlIndexEmail = "create index " + indexNameEmail + " on "
          + Constant.USER_TABLE + " (" + Constant.USER_EMAIL_FIELD_NAME
          + "(75))";
      rs = stm.executeQuery(checkEmail);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexEmail);
      }
      // SQL sentence to check and create an index for email on user table
      String indexNameSenderReceiver = "index_sender_receiver";
      String checkSenderReceiver = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.MESSAGE_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameSenderReceiver + "\"";
      String sqlIndexSenderReceiver = "create index " + indexNameSenderReceiver
          + " on " + Constant.MESSAGE_TABLE + " ("
          + Constant.MESSAGE_SENDER_FIELD_NAME + ", "
          + Constant.MESSAGE_RECEIVER_FIELD_NAME + ")";
      rs = stm.executeQuery(checkSenderReceiver);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexSenderReceiver);
      }
      // SQL sentence to check and create an index for thread_id on post table
      String indexNameThreadIdPost = "index_thread_id_post";
      String checkThreadIdPost = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.POST_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameThreadIdPost + "\"";
      String sqlIndexThreadIdPost = "create index " + indexNameThreadIdPost
          + " on " + Constant.POST_TABLE + " ("
          + Constant.THREAD_THREAD_ID_FIELD_NAME + ")";
      rs = stm.executeQuery(checkThreadIdPost);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexThreadIdPost);
      }
      // SQL sentence to check and create an index for forum_id on thread table
      String indexNameForumIdThread = "index_forum_id_thread";
      String checkForumIdThread = "SELECT INDEX_NAME FROM INFORMATION_SCHEMA.STATISTICS WHERE"
          + " `TABLE_CATALOG` = 'def' AND `TABLE_SCHEMA` = DATABASE() AND "
          + "`TABLE_NAME` = \"" + Constant.THREAD_TABLE
          + "\" AND `INDEX_NAME` = \"" + indexNameForumIdThread + "\"";
      String sqlIndexForumIdThread = "create index " + indexNameForumIdThread
          + " on " + Constant.THREAD_TABLE + " ("
          + Constant.THREAD_FORUM_ID_FIELD_NAME + ")";
      rs = stm.executeQuery(checkForumIdThread);
      if (!rs.next()) {
        stm.executeUpdate(sqlIndexForumIdThread);
      }

      // Setting administrator account
      String fields = Constant.USER_USER_ID_FIELD_NAME + ", "
          + Constant.USER_USER_NAME_FIELD_NAME + ", "
          + Constant.USER_PASSWORD_FIELD_NAME + ", "
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + ", "
          + Constant.USER_IS_MOD_FIELD_NAME + ", "
          + Constant.USER_NAME_FIELD_NAME + ", "
          + Constant.USER_SURNAME_NAME_FIELD_NAME + ", "
          + Constant.USER_EMAIL_FIELD_NAME;
      String values = "1, \"Administrator\", MD5(\"admin\"), \""
          + Constant.PROFILE_PICTURE_DEFAULT.replace("\\", "\\\\")
          + "\", 1, \"admin\", \"admin\", \"" + Constant.ADMIN_EMAIL + "\"";
      stm.executeUpdate("replace into " + Constant.USER_TABLE + " (" + fields
          + ") values (" + values + ")");
    } catch (SQLException e) {
      System.err.println("Error in createTable:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
      System.err.println("SQL => " + sql);
    } finally {
      try {
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in createTable:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }
    }
  }

}
