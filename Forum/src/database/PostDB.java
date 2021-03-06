
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.sql.Date;

import constants.Constant;

/**
 * Class related to operation with database about post
 * 
 * @author pedro
 *
 */
public class PostDB {
  Connection conn;
  DBConnection db;

  public PostDB() {
  }

  /**
   * Create a new post in database
   * 
   * @param userId
   *          User id who creates a new post
   * @param threadId
   *          Thread id where the post will be
   * @param post
   *          Post written
   * @return The post id, in case there is a problem post id will be -1
   */
  public int newPost(int userId, int threadId, String post) {
    int postId = -1;
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to create a new post in database
      stm = conn.prepareStatement(
          "insert into " + Constant.POST_TABLE
              + " (user_id, thread_id, post, creation_date,"
              + " modification_date) values (?,?,?,?,?)",
          Statement.RETURN_GENERATED_KEYS);
      stm.setInt(1, userId); // user_id
      stm.setInt(2, threadId); // thread_id
      stm.setString(3, post); // post
      stm.setDate(4, getCurrentDatetime()); // creation_date
      stm.setNull(5, java.sql.Types.DATE); // modification_date (null)
      stm.executeUpdate();
      // Getting the last post id
      ResultSet rs = stm.getGeneratedKeys();
      if (rs.next()) {
        // Saving the post id
        postId = rs.getInt(1);
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return postId;
  }

  /**
   * Edit an existing post
   * 
   * @param postId
   *          Post id which user wants to edit
   * @param post
   *          Post content
   * @return True if it was done, otherwise false
   */
  public boolean editPost(int postId, String post) {
    boolean done = false;
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to edit an existing post
      stm = conn.prepareStatement("update " + Constant.POST_TABLE
          + " set post=?, modification_date=? where id=?");
      stm.setString(1, post); // post
      stm.setDate(2, getCurrentDatetime()); // modification_date
      stm.setInt(3, postId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Delete a post. Just write a message in that post.
   * 
   * @param postId
   *          Post id which we want to delete
   * @param userId
   *          User id to know if was an administrator or moderator
   * @return True if it is done otherwise false
   */
  public boolean deletePost(int postId, int userId) {
    boolean done = false;
    PreparedStatement stm = null;
    Date date = getCurrentDatetime();
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      stm = conn.prepareStatement("update " + Constant.POST_TABLE
          + " set post=?, modification_date=?, deleted=? where id=?");
      if (userId == -1)
        stm.setString(1, "This post was deleted by administrator."); // post
      else stm.setString(1, "This post was deleted by moderators."); // post
      stm.setDate(2, date); // modification_date
      stm.setInt(3, 1); // deleted
      stm.setInt(4, postId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Function to get all post from a thread
   * 
   * @param threadId
   *          Thread id we want to get all posts
   * @param index
   *          Current page (Remember current page * total elements displayed)
   * @param elements
   *          Total elements displayed on screen
   * @return ArrayList{String[]} => id(0), user_id(1), post(2),
   *         creation_date(3), modification_date(4), user_name(5),
   *         profile_picture(6)
   */
  public ArrayList<String[]> getPosts(int threadId, int index, int elements) {
    ArrayList<String[]> posts = new ArrayList<String[]>();
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to get all post. The number of the post depends on
      // elements
      stm = conn.prepareStatement("select " + Constant.POST_TABLE + ".id, "
          + Constant.POST_TABLE + ".user_id, " + Constant.POST_TABLE + ".post, "
          + Constant.POST_TABLE + ".creation_date, " + Constant.POST_TABLE
          + ".modification_date, " + Constant.USER_TABLE
          + ".user_name as user_name, " + Constant.USER_TABLE
          + ".profile_picture from " + Constant.POST_TABLE + " inner join "
          + Constant.USER_TABLE + " on " + Constant.POST_TABLE + ".user_id="
          + Constant.USER_TABLE + ".id where thread_id=? limit ?,?");
      stm.setInt(1, threadId);
      stm.setInt(2, (index - 1) * elements);
      stm.setInt(3, elements);
      ResultSet rs = stm.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          String[] tmp = { Integer.toString(rs.getInt("id")),
              Integer.toString(rs.getInt("user_id")), rs.getString("post"),
              rs.getString("creation_date"), rs.getString("modification_date"),
              rs.getString("user_name"), rs.getString("profile_picture") };
          posts.add(tmp);
        }
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return posts;
  }

  /**
   * Function to get the number of total post in a thread
   * 
   * @param threadId
   *          Thread id which we want to know the total posts in a thread
   * @return Total post in a thread, if there're not posts total will be 0
   */
  public int getTotalPosts(int threadId) {
    int total = 0;
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to get the total number of post in a thread
      stm = conn.prepareStatement("select count(id) as total from "
          + Constant.POST_TABLE + " where thread_id=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      // Check if there was result
      if (rs.next()) {
        // Saving the total number of post
        total = rs.getInt("total");
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return total;
  }

  /**
   * Get a single post
   * 
   * @param postId
   *          Post id which we want to get
   * @return String[2] => post(0), user_id(1)
   */
  public String[] getPost(int postId) {
    String[] post = new String[2];
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to get a single post
      stm = conn.prepareStatement(
          "select user_id, post from " + Constant.POST_TABLE + " where id=?");
      stm.setInt(1, postId);
      ResultSet rs = stm.executeQuery();
      // Checking if post exists
      if (rs.next()) {
        // Saving the post into an Array
        post[0] = rs.getString("post");
        post[1] = Integer.toString(rs.getInt("user_id"));
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return post;
  }

  /**
   * Delete all posts in a thread from database
   * 
   * @param threadId
   *          Thread id which we want to delete all posts
   * @return True if it is done, otherwise false
   */
  public boolean deletePostbyThread(int threadId) {
    boolean done = false;
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence o delete all posts
      stm = conn.prepareStatement(
          "delete from " + Constant.POST_TABLE + " where thread_id=?");
      stm.setInt(1, threadId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Get the current date time
   * 
   * @return The current date time
   */
  private Date getCurrentDatetime() {
    java.util.Date today = new java.util.Date();
    return new Date(today.getTime());
  }
}