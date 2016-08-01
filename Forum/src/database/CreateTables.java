
package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import constants.Constant;

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
    // Setting user, forum, moderator, thread, post, message, block user
    // tables.
    String userT = "create table if not exists " + Constant.USER_TABLE
        + " (id int auto_increment not null, name blob, "
        + "surname blob, email blob not null, country blob, state blob, "
        + "city blob, user_name blob not null, password blob not null, "
        + "profile_picture blob not null, is_mod tinyint not null, "
        + "deleted tinyint not null, primary key(id), "
        + "unique key (user_name(255), email(255)));";
    String forumT = "create table if not exists " + Constant.FORUM_TABLE
        + " (id int not null auto_increment, name blob not null, description blob, "
        + "forum_id int, primary key(id));";
    String moderatorT = "create table if not exists " + Constant.MODERATOR_TABLE
        + " (id int auto_increment not null, forum_id int, "
        + "user_id int not null, primary key(id));";
    String threadT = "create table if not exists " + Constant.THREAD_TABLE
        + " (id int auto_increment not null, forum_id int not null, "
        + "name blob not null, author int not null, primary key(id));";
    String postT = "create table if not exists " + Constant.POST_TABLE
        + " (id int auto_increment not null, user_id int not null, "
        + "thread_id int not null, post mediumblob not null, "
        + "creation_date date not null, modification_date date, "
        + "deleted tinyint not null, primary key(id));";
    String messageT = "create table if not exists " + Constant.MESSAGE_TABLE
        + " (id int auto_increment not null, message mediumblob not null, "
        + "sender int not null, receiver int not null, creation_date date not null, "
        + "primary key(id));";
    String blockedUserT = "create table if not exists "
        + Constant.BLOCKED_USER_TABLE + " (id int auto_increment not null, "
        + "user_id int not null, blocked_user_id int not null, "
        + "primary key(id));";
    try {
      // Creating the tables
      stm = conn.createStatement();
      stm.executeUpdate(userT);
      stm.executeUpdate(forumT);
      stm.executeUpdate(moderatorT);
      stm.executeUpdate(threadT);
      stm.executeUpdate(postT);
      stm.executeUpdate(messageT);
      stm.executeUpdate(blockedUserT);

      // Setting administrator account
      String fields = "id, user_name, password, profile_picture, is_mod, name, surname, email";
      String values = "1, \"Administrator\", MD5(\"admin\"), \""
          + Constant.PROFILE_PICTURE_DEFAULT.replace("\\", "\\\\")
          + "\", 1, \"admin\", \"admin\", \"" + Constant.ADMIN_EMAIL + "\"";
      stm.executeUpdate("replace into " + Constant.USER_TABLE + " (" + fields
          + ") values (" + values + ")");
    } catch (SQLException e) {
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally {
      try {
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
