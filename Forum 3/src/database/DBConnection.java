
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
  private static String database;
  private static String userName;
  private static String password;
  private static String host;
  private static Connection conn;
  
  // *********************************************************
  // Change this if it is necessary. Mysql Root user, password database and
  // host.
  private static final String ROOT_USER = "root";
  private static final String ROOT_PASSWORD = "toor";
  private static final String DATABASE_NAME = "forum_db?autoReconnect=true&useSSL=false";
  private static final String HOST = "localhost";
  private static final String PORT = "3306";
  // *********************************************************

  protected DBConnection() {
    database = DATABASE_NAME;
    userName = ROOT_USER;
    password = ROOT_PASSWORD;
    host = HOST + ":" + PORT;
    conn = null;
  }

  protected Connection getConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      // Creating a connection with the database
      conn = DriverManager.getConnection(
          "jdbc:mysql://" + host + "/" + database, userName, password);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return conn;
  }

  protected void close() throws SQLException {
    if (conn != null) conn.close();
  }
}