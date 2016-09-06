
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class related to operation with database about user
 * 
 * @author pedro
 *
 */
public class UserDB {
  public UserDB() {
  }

  /**
   * Create a new user
   * 
   * @param name
   *          The real name of that person
   * @param surname
   *          The real surname of that person
   * @param email
   *          User's email
   * @param userName
   *          User name desired
   * @param password
   *          Password chosen
   * @param imgPath
   *          Profile picture path
   * @param country
   *          The real country of that person
   * @param state
   *          The real state of that person
   * @param city
   *          The real city of that person
   * @return
   */
  public boolean newUser(String name, String surname, String email,
      String userName, String password, String imgPath, String country,
      String state, String city) {
    boolean done = false;
    PreparedStatement stm = null;
    // Creating a new connection
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to create a new user.
      sql = "insert into " + Constant.USER_TABLE + " ("
          + Constant.USER_NAME_FIELD_NAME + ", "
          + Constant.USER_SURNAME_NAME_FIELD_NAME + ", "
          + Constant.USER_EMAIL_FIELD_NAME + ", "
          + Constant.USER_USER_NAME_FIELD_NAME + ", "
          + Constant.USER_PASSWORD_FIELD_NAME + ", "
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + ", "
          + Constant.USER_COUNTRY_FIELD_NAME + ", "
          + Constant.USER_STATE_FIELD_NAME + ", "
          + Constant.USER_CITY_FIELD_NAME + ", "
          + Constant.USER_DELETED_FIELD_NAME
          + ") VALUES (?,?,?,?,md5(?),?,?,?,?,?)";
      stm = conn.prepareStatement(sql);
      stm.setString(1, name); // name
      stm.setString(2, surname); // surname
      stm.setString(3, email); // email
      stm.setString(4, userName); // user name
      stm.setString(5, password); // password
      stm.setString(6, imgPath); // image path
      stm.setString(7, country); // country
      stm.setString(8, state); // state
      stm.setString(9, city); // city
      stm.setInt(10, 0); // deleted
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in newUser():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (Exception e) {
        System.err.println(
            "Error closing connection in newUser() => " + e.getMessage());
      }
    }
    return done;
  }

  /**
   * Create a new moderator
   * 
   * @param id
   *          The user id we want to create a new moderator
   * @return
   */
  public boolean newModUser(int id) {
    boolean done = false;
    PreparedStatement stm = null;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to create a new moderator user
      sql = "update " + Constant.USER_TABLE + " set "
          + Constant.USER_IS_MOD_FIELD_NAME + "=? where "
          + Constant.USER_USER_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, 1); // is_mod
      stm.setInt(2, id); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in newModUser():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => 1");
      System.err.println("2? => " + id);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connection in newModUser:\nMessage => "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return done;
  }

  /**
   * 
   * @param id
   * @param name
   * @param surname
   * @param email
   * @param userName
   * @param imgPath
   * @return
   */
  public boolean editUser(int id, String name, String surname, String imgPath,
      String country, String state, String city) {
    PreparedStatement stm = null;
    DBConnection db = null;
    Connection conn = null;
    boolean status;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to update an existing user
      sql = "update " + Constant.USER_TABLE + " set "
          + Constant.USER_NAME_FIELD_NAME + "=?, "
          + Constant.USER_SURNAME_NAME_FIELD_NAME + "=?, "
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + "=?, "
          + Constant.USER_COUNTRY_FIELD_NAME + "=?, "
          + Constant.USER_STATE_FIELD_NAME + "=?, "
          + Constant.USER_CITY_FIELD_NAME + "=? where "
          + Constant.USER_USER_ID_FIELD_NAME + "=?;";
      stm = conn.prepareStatement(sql);
      stm.setString(1, name); // name
      stm.setString(2, surname); // surname
      stm.setString(3, imgPath); // profile_picture
      stm.setString(4, country); // country
      stm.setString(5, state); // state
      stm.setString(6, city); // city
      stm.setInt(7, id); // id
      stm.executeUpdate();
      status = true;
    } catch (SQLException e) {
      System.err.println("Error in editUser():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + name);
      System.err.println("2? => " + surname);
      System.err.println("3? => " + imgPath);
      System.err.println("4? => " + country);
      System.err.println("5? => " + state);
      System.err.println("6? => " + city);
      System.err.println("7? => " + id);
      status = false;
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in editUser:\nMessage => "
            + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return status;
  }

  /**
   * Put deleted state to a user
   * 
   * @param id
   *          User id who will be deleted
   * @return
   */
  public boolean deleteUser(int id, boolean delete) {
    boolean done = false;
    PreparedStatement stm = null;
    // Creating a new connection
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to delete a user
      sql = "update " + Constant.USER_TABLE + " set "
          + Constant.USER_DELETED_FIELD_NAME + "=? where "
          + Constant.USER_USER_ID_FIELD_NAME + "=?;";
      stm = conn.prepareStatement(sql);
      if (delete)
        stm.setInt(1, 1); // deleted
      else stm.setInt(1, 0); // deleted
      stm.setInt(2, id); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteUser():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => 1");
      System.err.println("2? => " + id);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in deleteUser\n Message=> "
            + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return done;
  }

  /**
   * Get several data from a user
   * 
   * @param userId
   *          User id who will be got data
   * @return name(0), surname(1), user_name(2), profile_image(3), country(4),
   *         state(5), city(6), deleted(7)
   */
  public String[] getUser(int userId) {
    String[] user = new String[8];
    PreparedStatement stm = null;
    DBConnection db = null;
    Connection conn = null;
    ResultSet rs = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to get user's data
      sql = "select " + Constant.USER_NAME_FIELD_NAME + ", "
          + Constant.USER_SURNAME_NAME_FIELD_NAME + ", "
          + Constant.USER_USER_NAME_FIELD_NAME + ", "
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + ", "
          + Constant.USER_COUNTRY_FIELD_NAME + ", "
          + Constant.USER_STATE_FIELD_NAME + ", "
          + Constant.USER_CITY_FIELD_NAME + ", "
          + Constant.USER_DELETED_FIELD_NAME + " from " + Constant.USER_TABLE
          + " where " + Constant.USER_USER_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, userId);
      rs = stm.executeQuery();
      // Getting data
      if (rs.next()) {
        // Saving data into an Array
        // Name
        user[0] = rs.getObject(Constant.USER_NAME_FIELD_NAME) == null
            || rs.getString(Constant.USER_NAME_FIELD_NAME).isEmpty() ? "No name"
                : rs.getString(Constant.USER_NAME_FIELD_NAME);
        // Surname
        user[1] = rs.getObject(Constant.USER_SURNAME_NAME_FIELD_NAME) == null
            || rs.getString(Constant.USER_SURNAME_NAME_FIELD_NAME).isEmpty()
                ? "No surname"
                : rs.getString(Constant.USER_SURNAME_NAME_FIELD_NAME);
        // User name
        user[2] = rs.getString(Constant.USER_USER_NAME_FIELD_NAME);
        // Profile picture
        user[3] = rs.getString(Constant.USER_PROFILE_PICTURE_FIELD_NAME);
        // Country
        user[4] = rs.getObject(Constant.USER_COUNTRY_FIELD_NAME) == null
            || rs.getString(Constant.USER_COUNTRY_FIELD_NAME).isEmpty()
                ? "No country" : rs.getString(Constant.USER_COUNTRY_FIELD_NAME);
        // State
        user[5] = rs.getObject(Constant.USER_STATE_FIELD_NAME) == null
            || rs.getString(Constant.USER_STATE_FIELD_NAME).isEmpty()
                ? "No state" : rs.getString(Constant.USER_STATE_FIELD_NAME);
        // City
        user[6] = rs.getObject(Constant.USER_CITY_FIELD_NAME) == null
            || rs.getString(Constant.USER_CITY_FIELD_NAME).isEmpty() ? "No city"
                : rs.getString(Constant.USER_CITY_FIELD_NAME);
        // Deleted
        user[7] = rs.getInt(Constant.USER_DELETED_FIELD_NAME) == 0 ? ""
            : "Deleted";
      }
    } catch (SQLException e) {
      System.err.println("Error in getUser():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + userId);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in getUser\n Message=> "
            + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return user;
  }

  /**
   * Get users. If you want to select all user.
   * 
   * @param index
   *          Current page
   * @param totalElements
   *          Total elements displayed on screen
   * @return id(0), user_name(1), is_mod(2), deleted(3)
   */
  public ArrayList<String[]> getUsers(int index, int totalElements) {
    ArrayList<String[]> users = new ArrayList<>();
    Statement stm = null;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      stm = conn.createStatement();
      // SQL to get all users from database, the number displayed depends on
      // totalElements
      sql = "select " + Constant.USER_USER_ID_FIELD_NAME + ", "
          + Constant.USER_USER_NAME_FIELD_NAME + ", "
          + Constant.USER_IS_MOD_FIELD_NAME + ", "
          + Constant.USER_DELETED_FIELD_NAME + " from " + Constant.USER_TABLE
          + " where " + Constant.USER_USER_ID_FIELD_NAME + " > 1 limit "
          + ((index - 1) * totalElements) + ", " + totalElements;
      ResultSet rs = stm.executeQuery(sql);
      // Getting data
      while (rs.next()) {
        // Saving data into an ArrayList
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.USER_USER_ID_FIELD_NAME)),
            rs.getString(Constant.USER_USER_NAME_FIELD_NAME),
            Integer.toString(rs.getInt(Constant.USER_IS_MOD_FIELD_NAME)),
            Integer.toString(rs.getInt(Constant.USER_DELETED_FIELD_NAME)) };
        users.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getUsers():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.out.println("Error closing connection in getUsers()\n Message=> "
            + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return users;
  }

  /**
   * Count the total user in the database
   * 
   * @return Total user
   */
  public int getTotalUsers() {
    Statement stm = null;
    int total = 0;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      sql = "select count(" + Constant.USER_USER_ID_FIELD_NAME
          + ") as count from " + Constant.USER_TABLE + " where "
          + Constant.USER_USER_ID_FIELD_NAME + " > 1";
      stm = conn.createStatement();
      // SQL to get the total user from the database
      ResultSet rs = stm.executeQuery(sql);
      // Getting and saving total user
      if (rs.next()) total = rs.getInt("count");
    } catch (SQLException e) {
      System.err.println("Error in getTotalUsers():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
    } finally {
      try {
        // Closing all connections and sockets
        if (conn != null) conn.close();
        if (stm != null) stm.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connection in getTotalUser\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return total;
  }

  /**
   * Get all moderators.
   * 
   * @return id(0), user_name(1)
   */
  public ArrayList<String[]> getModUsers() {
    ArrayList<String[]> users = new ArrayList<>();
    PreparedStatement stm = null;
    ResultSet rs = null;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to get all moderators from database
      sql = "select " + Constant.USER_USER_ID_FIELD_NAME + ", "
          + Constant.USER_USER_NAME_FIELD_NAME + " from " + Constant.USER_TABLE
          + " where " + Constant.USER_IS_MOD_FIELD_NAME + "=? and "
          + Constant.USER_USER_ID_FIELD_NAME + " > 1 ";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, 1); // is_mod
      rs = stm.executeQuery();
      // Getting all moderators
      while (rs.next()) {
        // Saving them into and ArrayList
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.USER_USER_ID_FIELD_NAME)),
            rs.getString(Constant.USER_USER_NAME_FIELD_NAME) };
        users.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getModUsers():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => 1");
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.out
            .println("Error closing connection in getModUsers\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return users;
  }

  /**
   * Get all moderators by forum id.
   * 
   * @param forumId
   *          Forum id we want to get moderator users
   * @return id(0), user_name(1)
   */
  public ArrayList<String[]> getModUsers(int forumId) {
    ArrayList<String[]> users = new ArrayList<>();
    PreparedStatement stm = null;
    ResultSet rs = null;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to get moderator user data
      sql = "select " + Constant.USER_TABLE + "."
          + Constant.USER_USER_ID_FIELD_NAME + ", " + Constant.USER_TABLE + "."
          + Constant.USER_USER_NAME_FIELD_NAME + " from " + Constant.USER_TABLE
          + " inner join " + Constant.MODERATOR_TABLE + " on "
          + Constant.USER_TABLE + "." + Constant.USER_USER_ID_FIELD_NAME + "="
          + Constant.MODERATOR_TABLE + "."
          + Constant.MODERATOR_USER_ID_FIELD_NAME + " where "
          + Constant.MODERATOR_TABLE + "."
          + Constant.MODERATOR_FORUM_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      rs = stm.executeQuery();
      while (rs.next()) {
        // Getting and saving data into an ArrayList
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.USER_USER_ID_FIELD_NAME)),
            rs.getString(Constant.USER_USER_NAME_FIELD_NAME) };
        users.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getModUsers(int):");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.out
            .println("Error closing connection in getModUsers\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return users;
  }

  /**
   * Getting a Id list from moderators more easy to check user id from session.
   * 
   * @param forumId
   *          Forum id we need to get all moderators
   * @return
   */
  public ArrayList<Integer> getModUserIds(int forumId) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    PreparedStatement stm = null;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to get moderator user id from database the total user depends on
      // forum_id
      sql = "select " + Constant.MODERATOR_USER_ID_FIELD_NAME + " from "
          + Constant.MODERATOR_TABLE + " where "
          + Constant.MODERATOR_FORUM_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        // Saving into a list
        list.add(rs.getInt(Constant.MODERATOR_USER_ID_FIELD_NAME));
      }
    } catch (SQLException e) {
      System.err.println("Error in getModUsersIds():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connection in getModUserIds\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return list;
  }

  /**
   * Set a user as a moderator
   * 
   * @param userId
   *          User id we want to put as a moderator
   * @param mod
   *          If the user was moderator will be no moderator, otherwise we will
   *          put as moderator
   * @return True if it was done otherwise false
   */
  public boolean setModUserList(int userId, boolean mod) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL to update an existing user and put moderator state if is_mod = 0,
      // in case the user was moderator and is_mod will be 0
      sql = "update " + Constant.USER_TABLE + " set "
          + Constant.USER_IS_MOD_FIELD_NAME + "=? where "
          + Constant.USER_USER_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, mod ? 1 : 0); // is_mod
      stm.setInt(2, userId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in setModUserList():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + mod);
      System.err.println("2? => " + userId);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connection in setModUserList\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return done;

  }

  /**
   * Check and return an user if the given data was correct
   * 
   * @param userName
   *          User name
   * @param password
   *          Password
   * @return Map[Boolean, ArrayList[String]] => true : id(0), user_name(1),
   *         profile_picture(2), deleted(3) otherwise false : null;
   */

  public Map<Boolean, String[]> login(String userName, String password) {
    Map<Boolean, String[]> user = new HashMap<>();
    PreparedStatement stm = null;
    DBConnection db = null;
    Connection conn = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to get data from the user name and password given
      // This sentence use user_name key
      sql = "select " + Constant.USER_USER_ID_FIELD_NAME + ", "
          + Constant.USER_USER_NAME_FIELD_NAME + ", "
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + ", "
          + Constant.USER_DELETED_FIELD_NAME + " from " + Constant.USER_TABLE
          + " where " + Constant.USER_USER_NAME_FIELD_NAME + "=? and "
          + Constant.USER_PASSWORD_FIELD_NAME + "=MD5(?)";
      stm = conn.prepareStatement(sql);
      stm.setString(1, userName); // user_name
      stm.setString(2, password); // password
      // Getting user data
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        // User data correct
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.USER_USER_ID_FIELD_NAME)),
            rs.getString(Constant.USER_USER_NAME_FIELD_NAME),
            rs.getString(Constant.USER_PROFILE_PICTURE_FIELD_NAME),
            Integer.toString(rs.getInt(Constant.USER_DELETED_FIELD_NAME)) };
        user.put(true, tmp);
      } else {
        // User data wrong
        user.put(false, null);
      }
    } catch (SQLException e) {
      System.err.println("Error in login():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in login()\n Message=> "
            + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return user;
  }

  /**
   * Check if user name was registered
   * 
   * @param userName
   *          The user name which user wants to register
   * @return True if exists otherwise false
   */
  public boolean existingUserName(String userName) {
    boolean user = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to get that user name from database
      sql = "select " + Constant.USER_USER_NAME_FIELD_NAME + " from "
          + Constant.USER_TABLE + " where " + Constant.USER_USER_NAME_FIELD_NAME
          + "=?";
      stm = conn.prepareStatement(sql);
      stm.setString(1, userName); // user_name
      ResultSet rs = stm.executeQuery();
      // Checking user name if there is row then user
      if (rs.next()) user = true;
    } catch (SQLException e) {
      System.err.println("Error in existingUserName():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + userName);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connection in existingUserName\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return user;
  }

  /**
   * Check if someone register his/her email
   * 
   * @param email
   *          Email needed to check
   * @return True email is registered otherwise false
   */
  public boolean existingEmail(String email) {
    boolean user = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // SQL sentence to check if email that email was registered in database
      sql = "select " + Constant.USER_EMAIL_FIELD_NAME + " from "
          + Constant.USER_TABLE + " where " + Constant.USER_EMAIL_FIELD_NAME
          + "=?";
      stm = conn.prepareStatement(sql);
      stm.setString(1, email); // email
      ResultSet rs = stm.executeQuery();
      // Checking email if there is row then email was registered
      if (rs.next()) user = true;
    } catch (SQLException e) {
      System.err.println("Error in existingEmail():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + email);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connection in existingEmail\n Message=> "
                + e.getMessage() + "\nSQL State => " + e.getSQLState());
      }
    }
    return user;
  }
}