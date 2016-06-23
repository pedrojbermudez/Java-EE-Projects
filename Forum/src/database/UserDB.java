
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.mysql.fabric.xmlrpc.base.Array;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import constants.Constant;
import jdk.nashorn.internal.runtime.linker.JavaAdapterFactory;

public class UserDB {

  private Connection conn;
  private DBConnection db;

  public UserDB() {
    db = new DBConnection();
  }

  /**
   * 
   * @param name
   * @param surname
   * @param email
   * @param userName
   * @param password
   * @param imgPath
   * @return
   */
  public boolean newUser(String name, String surname, String email,
      String userName, String password, String imgPath, String country,
      String state, String city) {
    boolean done = false;
    PreparedStatement stm = null;
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement("insert into " + Constant.USER_TABLE
          + " (name, surname, email, user_name, password, profile_picture,"
          + "country, state, city) VALUES (?,?,?,?,md5(?),?,?,?,?)");
      stm.setString(1, name);
      stm.setString(2, surname);
      stm.setString(3, email);
      stm.setString(4, userName);
      stm.setString(5, password);
      stm.setString(6, imgPath);
      stm.setString(7, country);
      stm.setString(8, state);
      stm.setString(9, city);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * 
   * @param name
   * @param surname
   * @param email
   * @param userName
   * @param password
   * @param imgPath
   * @return
   */
  public boolean newModUser(int id) {
    boolean done = false;
    PreparedStatement stm = null;
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement(
          "update " + Constant.USER_TABLE + " set is_mod=? where id=?");
      stm.setInt(1, 1);
      stm.setInt(2, id);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
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
    boolean status;
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement(
          "update " + Constant.USER_TABLE + " set name=?, surname=?, "
              + "profile_picture=?, country=?, state=?, city=? where id=?;");
      stm.setString(1, name);
      stm.setString(2, surname);
      stm.setString(3, imgPath);
      stm.setString(4, country);
      stm.setString(5, state);
      stm.setString(6, city);
      stm.setInt(7, id);
      System.out.println("sql => update " + Constant.USER_TABLE + " set name=\""
          + name + "\", surname=\"" + surname + "\", " + "profile_image=\""
          + imgPath + "\", country=\"" + country + "\", state=\"" + state
          + "\", city=\"" + city + "\" where id=" + id + ";");
      stm.executeUpdate();
      status = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
      status = false;
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
    return status;
  }

  /**
   * 
   * @param id
   * @param userName
   * @return
   */
  public boolean deleteUser(int id) {
    boolean done = false;
    PreparedStatement stm = null;
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement(
          "update " + Constant.USER_TABLE + " set deleted=? where id=?;");
      stm.setInt(1, 1);
      stm.setInt(2, id);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * 
   * @param userId
   * @return name(0), surname(1), user_name(2), profile_image(3), country(4),
   *         state(5), city(6)
   */
  public String[] getUser(int userId) {
    conn = db.getConnection();
    String[] user = new String[7];
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.prepareStatement("select name, surname, user_name, "
          + "profile_picture, country, state, city from " + Constant.USER_TABLE
          + " where id=?");
      stm.setInt(1, userId);
      rs = stm.executeQuery();
      if (rs.next()) {
        user[0] = rs.getObject("name") == null || rs.getString("name").isEmpty()? "No name"
            : rs.getString("name");
        user[1] = rs.getObject("surname") == null || rs.getString("surname").isEmpty()? "No surname"
            : rs.getString("surname");
        user[2] = rs.getString("user_name");
        user[3] = rs.getString("profile_picture");
        user[4] = rs.getObject("country") == null
            || rs.getString("country").isEmpty() ? "No country"
                : rs.getString("country");
        user[5] = rs.getObject("state") == null
            || rs.getString("state").isEmpty() ? "No state"
                : rs.getString("state");
        user[6] = rs.getObject("city") == null || rs.getString("city").isEmpty()
            ? "No city" : rs.getString("city");
      }
    } catch (SQLException e) {
      System.err.println("There is a problem here");
      System.err.println(e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
    return user;
  }

  /**
   * Get users. If you want to select all user.
   * 
   * @return id(0), user_name(1), is_mod(2)
   */
  public ArrayList<String[]> getUsers() {
    ArrayList<String[]> users = new ArrayList<>();
    conn = db.getConnection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      ResultSet rs = stm.executeQuery("select id, user_name, is_mod from "
          + Constant.USER_TABLE + " where id > 1");
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("user_name"), Integer.toString(rs.getInt("is_mod")) };
        users.add(tmp);
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return users;
  }

  /**
   * Get all moderators.
   * 
   * @return id(0), user_name(1), forum_id(2)
   */
  public ArrayList<String[]> getModUsers() {
    ArrayList<String[]> users = new ArrayList<>();
    conn = db.getConnection();
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.prepareStatement("select id as user_id, user_name from "
          + Constant.USER_TABLE + " where is_mod=? and id > 1");
      stm.setInt(1, 1);
      rs = stm.executeQuery();
      while (rs.next()) {
        System.out.println("user id => " + rs.getInt("user_id")
            + " | user name => " + rs.getString("user_name"));
        String[] tmp = { Integer.toString(rs.getInt("user_id")),
            rs.getString("user_name") };
        users.add(tmp);
      }
      stm.setInt(1, 1);
      rs = stm.executeQuery();
      while (rs.next()) {
        System.out.println("user id => " + rs.getInt("user_id")
            + " | user name => " + rs.getString("user_name") + " | forum id => "
            + rs.getInt("forum_id"));
        String[] tmp = { Integer.toString(rs.getInt("user_id")),
            rs.getString("user_name"),
            Integer.toString((rs.getInt("forum_id"))) };
        users.add(tmp);
      }

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (SQLException e) {
        System.out.println(e.getSQLState());
      }
    }
    return users;
  }

  /**
   * Get all moderators by forum id.
   * 
   * @return id(0), user_name(1), forum_id(2)
   */
  public ArrayList<String[]> getModUsers(int forumId) {
    ArrayList<String[]> users = new ArrayList<>();
    conn = db.getConnection();
    PreparedStatement stm = null;
    ResultSet rs = null;
    try {
      stm = conn.prepareStatement("select " + Constant.USER_TABLE + ".id, "
          + Constant.USER_TABLE + ".user_name from " + Constant.USER_TABLE
          + " inner join " + Constant.MODERATOR_TABLE + " on "
          + Constant.USER_TABLE + ".id=" + Constant.MODERATOR_TABLE
          + ".user_id where " + Constant.MODERATOR_TABLE + ".forum_id=?");
      stm.setInt(1, forumId);
      rs = stm.executeQuery();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("user_name") };
        users.add(tmp);
      }
      stm.setInt(1, 1);
      rs = stm.executeQuery();
      while (rs.next()) {
        System.out.println("user id => " + rs.getInt("user_id")
            + " | user name => " + rs.getString("user_name") + " | forum id => "
            + rs.getInt("forum_id"));
        String[] tmp = { Integer.toString(rs.getInt("user_id")),
            rs.getString("user_name"),
            Integer.toString((rs.getInt("forum_id"))) };
        users.add(tmp);
      }

    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (SQLException e) {
        System.out.println(e.getSQLState());
      }
    }
    return users;
  }

  /**
   * Getting a Id list from moderators more easy to check user id from session.
   * 
   * @param forumId
   * @return
   */
  public ArrayList<Integer> getModUserIds(int forumId) {
    ArrayList<Integer> list = new ArrayList<Integer>();
    PreparedStatement stm = null;
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement("select user_id from "
          + Constant.MODERATOR_TABLE + " where forum_id=?");
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        list.add(rs.getInt("user_id"));
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return list;
  }

  public boolean setModUserList(int userId, boolean mod) {
    boolean done = false;
    PreparedStatement stm = null;
    PreparedStatement stm2 = null;
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement(
          mod ? "update " + Constant.USER_TABLE + " set is_mod=? where id=?"
              : "update " + Constant.USER_TABLE + " set is_mod=? where id=?");
      stm.setInt(1, mod ? 1 : 0);
      stm.setInt(2, userId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
    return done;

  }

  /**
   * 
   * @param user
   * @param blockedUser
   * @param userName
   * @param blockedUserName
   * @param blocked
   * @return
   */
  public boolean blockUser(int user, int blockedUser, String userName,
      String blockedUserName, boolean blocked) {
    boolean done = false;
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      if (!blocked) {
        stm = conn.prepareStatement("insert into " + Constant.BLOCKED_USER_TABLE
            + " (user_id, blocked_user_id) VALUES (?, ?)");
      } else {
        stm = conn.prepareStatement("delete from " + Constant.BLOCKED_USER_TABLE
            + " where user_id=? and blocked_user_id=?");
      }
      stm.setInt(1, user);
      stm.setInt(2, blockedUser);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (Exception e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * 
   * @param userName
   * @param password
   * @return Map[Boolean, ArrayList[String]] => true : id(0), user_name(1),
   *         profile_picture(2); false : null;
   */

  public Map<Boolean, String[]> login(String userName, String password) {
    Map<Boolean, String[]> user = new HashMap<>();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      stm = conn.prepareStatement("select id, user_name, profile_picture  from "
          + Constant.USER_TABLE + " where user_name=? and password=MD5(?)");
      stm.setString(1, userName);
      stm.setString(2, password);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("user_name"), rs.getString("profile_picture") };
        user.put(true, tmp);
      } else {
        user.put(false, null);
      }
    } catch (SQLException e) {
      System.out.println(e.getSQLState());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getSQLState());
      }
    }
    return user;
  }

  public boolean existsUser(String userName, String email) {
    boolean user = false;
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      stm = conn.prepareStatement("select user_name, email from "
          + Constant.USER_TABLE + " where user_name=? or email=?");
      stm.setString(1, userName);
      stm.setString(2, email);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        user = true;
      }
    } catch (SQLException e) {
      System.out.println(e.getSQLState());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if (conn != null) {
          conn.close();
        }
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getSQLState());
      }
    }
    return user;
  }
}