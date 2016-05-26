package database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import constants.Constant;

public class MessageDB {
  private DBConnection db;
  private Connection conn;

  public MessageDB() {
    db = new DBConnection();
  }

  public boolean newMessage(String message, int sender, int receiver) {
    boolean done = false;
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("insert into " + Constant.MESSAGE_TABLE
          + " (message, sender, receiver, creation_date) values (?,?,?,?)");
      stm.setString(1, message);
      stm.setInt(2, sender);
      stm.setInt(3, receiver);
      stm.setDate(4, getCurrentDatetime());
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close(stm);
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean deleteMessage(int id) {
    boolean done = false;
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn
          .prepareStatement("delete " + Constant.MESSAGE_TABLE + " where id=?");
      stm.setInt(1, id);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close(stm);
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  private void close(PreparedStatement stm) throws SQLException {
    if (stm != null) {
      stm.close();
    }
    if (conn != null) {
      conn.close();
    }
    db.close();
  }

  public Date getCurrentDatetime() {
    java.util.Date today = new java.util.Date();
    return new Date(today.getTime());
  }
}
