
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Date;

import constants.Constant;

public class PostDB {
  Connection conn;
  DBConnection db;

  public PostDB() {
    db = new DBConnection();
  }

  public int newPost(int userId, int threadId, String post) {
    int postId = -1;
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "insert into " + Constant.POST_TABLE
              + " (user_id, thread_id, post, creation_date,"
              + " modification_date) values (?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS);
      System.out.println("new post => " + "insert into " + Constant.POST_TABLE
          + " (user_id, thread_id, post, creation_date, modification_date) "
          + "values (" + userId + "," + threadId + ",\"" + post + "\","
          + getCurrentDatetime() + ",null)");
      stm.setInt(1, userId);
      stm.setInt(2, threadId);
      stm.setString(3, post);
      stm.setDate(4, getCurrentDatetime());
      stm.setNull(5, java.sql.Types.DATE);
      stm.executeUpdate();
      ResultSet rs = stm.getGeneratedKeys();
      if (rs.next()) {
        postId = rs.getInt(1);
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close(stm);
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return postId;
  }

  public boolean editPost(int postId, String post) {
    boolean done = false;
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("update " + Constant.POST_TABLE
          + " set post=?, modification_date=? where id=?");
      stm.setString(1, post);
      stm.setDate(2, getCurrentDatetime());
      stm.setInt(3, postId);
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

  public boolean deletePost(int postId) {
    boolean done = false;
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      Date date = getCurrentDatetime();
      stm = conn.prepareStatement(
          "update " + Constant.POST_TABLE + " set post=? where id=?");
      stm.setString(1, "This post was deletd by the administrator.");
      stm.setDate(2, date);
      stm.setInt(3, postId);
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

  /**
   * 
   * @param threadId
   * @return ArrayList{String[]} => id(0), user_id(1), post(2),
   *         creation_date(3), modification_date(4), user_name(5),
   *         profile_picture(6)
   */
  public ArrayList<String[]> getPosts(int threadId) {
    ArrayList<String[]> posts = new ArrayList<String[]>();
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("select " + Constant.POST_TABLE + ".id, "
          + Constant.POST_TABLE + ".user_id, " + Constant.POST_TABLE + ".post, "
          + Constant.POST_TABLE + ".creation_date, " + Constant.POST_TABLE
          + ".modification_date, " + Constant.USER_TABLE
          + ".user_name as user_name, " + Constant.USER_TABLE
          + ".profile_picture from " + Constant.POST_TABLE + " inner join "
          + Constant.USER_TABLE + " on " + Constant.POST_TABLE + ".user_id="
          + Constant.USER_TABLE + ".id where thread_id=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          // TODO revisar la parte de las fechas por posible error
          String[] tmp = { Integer.toString(rs.getInt("id")),
              Integer.toString(rs.getInt("user_id")), rs.getString("post"),
              rs.getString("creation_date"), rs.getString("modification_date"),
              rs.getString("user_name"), rs.getString("profile_picture") };
          posts.add(tmp);
        }
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    }
    return posts;
  }

  /**
   * 
   * @param postId
   * @return String[2] => post(0), user_id(1)
   */
  public String[] getPost(int postId) {
    String[] post = new String[2];
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "select user_id, post from " + Constant.POST_TABLE + " where id=?");
      stm.setInt(1, postId);
      ResultSet rs = stm.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          // TODO revisar la parte de las fechas por posible error
          post[0] = rs.getString("post");
          post[1] = Integer.toString(rs.getInt("user_id"));
        }
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    }
    return post;
  }

  public boolean deletePostbyThread(int threadId) {
    boolean done = false;
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "delete " + Constant.POST_TABLE + " where thread_id=?");
      stm.setInt(1, threadId);
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

  public Date getCurrentDatetime() {
    java.util.Date today = new java.util.Date();
    return new Date(today.getTime());
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
}