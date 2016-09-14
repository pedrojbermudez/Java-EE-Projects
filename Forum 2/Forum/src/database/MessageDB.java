
package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class related to operation with database about message table
 * 
 * Available in version 2
 * 
 * @author pedro
 *
 */
public class MessageDB {

  public MessageDB() {
  }

  /**
   * Create a new message.
   * 
   * @param message
   *          The message
   * @param sender
   *          Who sends the message
   * @param receiver
   *          Who receives the message
   * @return True if the message was created,otherwise false.
   */
  public boolean newMessage(String message, int sender, int receiver) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      // Inserting a new message into the database
      stm = conn.prepareStatement("insert into " + Constant.MESSAGE_TABLE + " ("
          + Constant.MESSAGE_MESSAGE_FIELD_NAME + ", "
          + Constant.MESSAGE_SENDER_FIELD_NAME + ", "
          + Constant.MESSAGE_RECEIVER_FIELD_NAME + ", "
          + Constant.MESSAGE_CREATION_DATE_FIELD_NAME + ") values (?,?,?,?)");
      stm.setString(1, message);
      stm.setInt(2, sender);
      stm.setInt(3, receiver);
      stm.setDate(4, getCurrentDatetime());
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in newMessage:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State: " + e.getSQLState());
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error while clossing connections in newMessage:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
      }
    }
    return done;
  }

  /**
   * Delete a message from the database (for now this method won't be used
   * 
   * @param messageId
   *          The message id we want to delete
   * @return True if it was deleted, otherwise false
   */
  public boolean deleteMessage(int messageId) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Establishing connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      // Deleting a message
      stm = conn.prepareStatement("delete from " + Constant.MESSAGE_TABLE
          + " where " + Constant.MESSAGE_MESSAGE_ID_FIELD_NAME + "=?");
      stm.setInt(1, messageId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteMessage:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State: " + e.getSQLState());
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error while clossing connections in deleteMessage:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
      }
    }
    return done;
  }

  public ArrayList<String[]> getMessages(int sender, int receiver) {
    PreparedStatement stm = null;
    Connection conn = null;
    DBConnection db = null;
    ArrayList<String[]> messages = new ArrayList<>();
    try {
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn
          .prepareStatement("select " + Constant.MESSAGE_MESSAGE_FIELD_NAME
              + ", " + Constant.MESSAGE_CREATION_DATE_FIELD_NAME + " from "
              + Constant.MESSAGE_TABLE + " where "
              + Constant.MESSAGE_SENDER_FIELD_NAME + "=? and "
              + Constant.MESSAGE_RECEIVER_FIELD_NAME + "=?");
      stm.setInt(1, sender);
      stm.setInt(2, receiver);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        String[] tmp = { rs.getString("message"),
            rs.getString("creation_date") };
        messages.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getMessages:");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State: " + e.getSQLState());
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error while clossing connections in getMessages:");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State: " + e.getSQLState());
      }
    }
    return messages;
  }

  public Date getCurrentDatetime() {
    java.util.Date today = new java.util.Date();
    return new Date(today.getTime());
  }
}
