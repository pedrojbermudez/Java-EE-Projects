
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class to manage friends in the database
 * 
 * @author pedro
 *
 */
public class FriendDB {

  public FriendDB() {
  }

  /**
   * Adding a new friend to the database.
   * 
   * @param userId
   *          User who want to add as his/her friend
   * @param friendUserId
   *          User whom goes the invitation
   * @return True if it was possible create a new friend, otherwise false will
   *         be returned
   */
  public boolean newFriend(int userId, int friendUserId) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to
      stm = conn.prepareStatement("insert into " + Constant.FRIEND_TABLE + " ("
          + Constant.FRIEND_USER_ID_FIELD_NAME + ", "
          + Constant.FRIEND_FRIEND_USER_ID_FIELD_NAME + ") values (?,?)");
      stm.setInt(1, userId);
      stm.setInt(2, friendUserId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in newFriend:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in newFriend:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return done;
  }

  public boolean deleteFriend(int userId, int friendUserId) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to delete a friend
      stm = conn.prepareStatement("delete from " + Constant.FRIEND_TABLE
          + " where " + Constant.FRIEND_USER_ID_FIELD_NAME + "=? and "
          + Constant.FRIEND_FRIEND_USER_ID_FIELD_NAME + "=?");
      stm.setInt(1, userId); // user_id
      stm.setInt(2, friendUserId); // friend_user_id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteFriend:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deleteFriend:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return done;
  }

  public ArrayList<Integer> getFriendsList(int userId) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn.prepareStatement("select " + Constant.FRIEND_USER_ID_FIELD_NAME
          + ", " + Constant.FRIEND_FRIEND_USER_ID_FIELD_NAME + " where "
          + Constant.FRIEND_USER_ID_FIELD_NAME + "=? or "
          + Constant.FRIEND_FRIEND_USER_ID_FIELD_NAME + "=?");
      stm.setInt(1, userId);
      stm.setInt(2, userId);
      rs = stm.executeQuery();
      while (rs.next()) {
        // Saving data in temporal variable
        int userIdTmp = rs.getInt(Constant.FRIEND_USER_ID_FIELD_NAME);
        int friendUserIdTmp = rs
            .getInt(Constant.FRIEND_FRIEND_USER_ID_FIELD_NAME);
        // If userIdTmp = userId was the user who add to the other person,
        // otherwise will be the added person. The other user id will be added
        if (userIdTmp == userId)
          list.add(friendUserIdTmp);
        else list.add(userIdTmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getFriendsList:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all connections and sockets
        if (rs != null) rs.close();
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getFriendsList:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return list;
  }

  /**
   * When user wants to block or delete a block
   * 
   * @param user
   *          User id from the user who desire to block
   * @param blockedUser
   *          User id who the user wants to block
   * @param blocked
   *          If the user was block or not (help to remove the block or put a
   *          block)
   * @return True if it was done otherwise false
   */
  public boolean blockUser(int userId, int blockedUser, boolean blocked) {
    boolean done = false;
    // Creating a new connection
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    String sql = "";
    try {
      db = new DBConnection();
      conn = db.getConnection();
      if (!blocked) {
        // Someone wants to block other user
        sql = "insert into " + Constant.BLOCKED_USER_TABLE + " ("
            + Constant.BLOCKED_USER_ID_FIELD_NAME + ", "
            + Constant.BLOCKED_BLOCKED_USER_ID_FIELD_NAME + ") VALUES (?, ?)";
        stm = conn.prepareStatement(sql);
      } else {
        // Someone wants to delete block user
        sql = "delete from " + Constant.BLOCKED_USER_TABLE + " where "
            + Constant.BLOCKED_USER_ID_FIELD_NAME + "=? and "
            + Constant.BLOCKED_BLOCKED_USER_ID_FIELD_NAME + "=?";
        stm = conn.prepareStatement(sql);
      }
      // Setting same data
      stm.setInt(1, userId); // user_id
      stm.setInt(2, blockedUser); // blocked_user_id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in blockUser:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in blockUser:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return done;
  }

}
