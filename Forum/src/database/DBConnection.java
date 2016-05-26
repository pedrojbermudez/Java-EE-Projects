
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import constants.Constant;

public class DBConnection {
  private static String database;
  private static String userName;
  private static String password;
  private static String host;
  private static Connection conn;

  protected DBConnection() {
    database = Constant.DATABASE_NAME;
    userName = Constant.ROOT_USER;
    password = Constant.ROOT_PASSWORD;
    host = Constant.HOST + ":" + Constant.PORT;
    conn = null;
  }

  protected Connection getConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    try {
      conn = DriverManager.getConnection(
          "jdbc:mysql://" + host + "/" + database, userName, password);
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return conn;
  }

  protected void close() throws SQLException {
    if (conn != null) conn.close();
  }
}
