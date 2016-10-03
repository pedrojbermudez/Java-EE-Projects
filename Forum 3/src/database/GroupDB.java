
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class to manage chat groups in the database
 * 
 * @author pedro
 *
 */
public class GroupDB {

  /**
   * Create a new group
   * 
   * @param name
   *          The name of the group
   * @param friendsOnly
   *          Specify if this group will be only for friends (set 0 for all, set
   *          1 just for friend)
   * @return
   */
  public boolean newGroup(String name, int friendsOnly) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn.prepareStatement("insert into " + Constant.GROUP_TABLE + " ("
          + Constant.GROUP_NAME_FIELD_NAME + ", "
          + Constant.GROUP_FRIENDS_ONLY_FIELD_NAME + ") values (?,?)");
      // SQL sentence to insert a new group in the database
      stm.setString(1, name); // name
      stm.setInt(2, friendsOnly); // friends_only
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in newGroup:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally {
      try {
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in newGroup:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }

    }
    return done;
  }

  /**
   * Edit an existing group in the database
   * 
   * @param groupId
   *          Group id we want to edit
   * @param name
   *          The new name
   * @param friendsOnly
   *          The new set for friends only field (set 0 to allow all, set 1 to
   *          allow only friend)
   * @return
   */
  public boolean editGroup(int groupId, String name, int friendsOnly) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn.prepareStatement("update " + Constant.GROUP_TABLE + " set "
          + Constant.GROUP_NAME_FIELD_NAME + "=?, "
          + Constant.GROUP_FRIENDS_ONLY_FIELD_NAME + "=? where "
          + Constant.GROUP_GROUP_ID_FIELD_NAME + "=?");
      // SQL sentence to edit a group in the database
      stm.setString(1, name); // name
      stm.setInt(2, friendsOnly); // friends_only
      stm.setInt(3, groupId); // group_id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in editGroup:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally {
      try {
        // Closing connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in editGroup:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }

    }
    return done;
  }

  /**
   * Delete a group from the database
   * 
   * @param groupId
   *          Group id we want to delete
   * @return True if it was possible to delete it otherwise false
   */
  public boolean deleteGroup(int groupId) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn.prepareStatement("delete " + Constant.GROUP_TABLE + " where "
          + Constant.GROUP_GROUP_ID_FIELD_NAME + "=?");
      stm.setInt(1, groupId);
      // Deleting messages in this group
      deleteMessageByGroup(groupId);
      // Deleting this group
      stm.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Error in deleteGroup:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally {
      // Closing connections and sockets
      try {
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deleteGroup:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }
    }
    return done;
  }

  /**
   * Delete a message from a group
   * 
   * @param groupId
   *          Group we want to delete all message
   * @return True if it was possible to delete all message, otherwise false
   */
  private void deleteMessageByGroup(int groupId) {
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to delete all messages in a group
      stm = conn.prepareStatement("delete " + Constant.MESSAGE_TABLE + " where "
          + Constant.MESSAGE_GROUP_ID_FIELD_NAME + "=?");
      stm.setInt(1, groupId); // group_id
      stm.executeUpdate();
    } catch (SQLException e) {
      System.err.println("Error in deleteMessageByGroup:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in editGroup:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }
    }
  }

  /**
   * Get a single group.
   * 
   * @param groupId
   *          Group id we want to get
   * @return
   */
  public String[] getGroup(int groupId) {
    String[] group = null;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    ResultSet rs = null;
    try{
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn.prepareStatement("select " + Constant.GROUP_NAME_FIELD_NAME + ", " + Constant.GROUP_FRIENDS_ONLY_FIELD_NAME);
      rs = stm.executeQuery();
    } catch(SQLException e){
      System.err.println("Error in getGroup");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally{
      try{
        if (rs != null) rs.close();
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch(SQLException e){
        System.err.println("Error closing connections in getGroup");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }
    }
    return group;
  }
}