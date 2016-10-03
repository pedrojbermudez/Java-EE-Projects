
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.data.Post;

import java.sql.Date;

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
      stm = conn.prepareStatement("insert into " + Constant.POST_TABLE + " ("
          + Constant.POST_USER_ID_FIELD_NAME + ", "
          + Constant.POST_THREAD_ID_FIELD_NAME + ", "
          + Constant.POST_POST_FIELD_NAME + ", "
          + Constant.POST_CREATION_DATE_FIELD_NAME + ", "
          + Constant.POST_MODIFICATION_DATE_FIELD_NAME
          + ") values (?,?,?,now(),?)", Statement.RETURN_GENERATED_KEYS);
      stm.setInt(1, userId); // user_id
      stm.setInt(2, threadId); // thread_id
      stm.setString(3, post); // post
      stm.setNull(4, java.sql.Types.DATE); // modification_date (null)
      stm.executeUpdate();
      // Getting the last post id
      ResultSet rs = stm.getGeneratedKeys();
      if (rs.next()) {
        // Saving the post id
        postId = rs.getInt(1);
      }
    } catch (SQLException e) {
      System.err.println("Error in newPost():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in newPost():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      stm = conn.prepareStatement("update " + Constant.POST_TABLE + " set "
          + Constant.POST_POST_FIELD_NAME + "=?, "
          + Constant.POST_MODIFICATION_DATE_FIELD_NAME + "=now() where "
          + Constant.POST_POST_ID_FIELD_NAME + "=?");
      stm.setString(1, post); // post
      stm.setInt(2, postId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in editPost():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in editPost():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      stm = conn.prepareStatement("update " + Constant.POST_TABLE + " set "
          + Constant.POST_POST_FIELD_NAME + "=?, "
          + Constant.POST_MODIFICATION_DATE_FIELD_NAME + "=? where "
          + Constant.POST_POST_ID_FIELD_NAME + "=?");
      if (userId == 1)
        stm.setString(1, "This post was deleted by administrator."); // post
      else stm.setString(1, "This post was deleted by moderators."); // post
      stm.setDate(2, date); // modification_date
      stm.setInt(3, postId); // post_id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deletePost():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deletePost():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
  public ArrayList<Post> getPosts(int threadId, int index, int elements) {
    ArrayList<Post> posts = new ArrayList<Post>();
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to get all post. The number of the post depends on
      // elements
      stm = conn.prepareStatement("select " + Constant.POST_TABLE + "."
          + Constant.POST_POST_ID_FIELD_NAME + " as post_id, "
          + Constant.POST_TABLE + "." + Constant.POST_USER_ID_FIELD_NAME
          + " as post_user_id, " + Constant.POST_TABLE + "."
          + Constant.POST_POST_FIELD_NAME + " as post, " + Constant.POST_TABLE
          + "." + Constant.POST_CREATION_DATE_FIELD_NAME + " as creat_date, "
          + Constant.POST_TABLE + "."
          + Constant.POST_MODIFICATION_DATE_FIELD_NAME + " mod_date, "
          + Constant.USER_TABLE + "." + Constant.USER_USER_NAME_FIELD_NAME
          + " as user_name, " + Constant.USER_TABLE + "."
          + Constant.USER_PROFILE_PICTURE_FIELD_NAME + " as picture from "
          + Constant.POST_TABLE + " inner join " + Constant.USER_TABLE + " on "
          + Constant.POST_TABLE + "." + Constant.POST_USER_ID_FIELD_NAME + "="
          + Constant.USER_TABLE + "." + Constant.USER_USER_ID_FIELD_NAME
          + " where " + Constant.POST_THREAD_ID_FIELD_NAME + "=? limit ?,?");
      stm.setInt(1, threadId);
      stm.setInt(2, (index - 1) * elements);
      stm.setInt(3, elements);
      ResultSet rs = stm.executeQuery();
      if (rs != null) {
        while (rs.next()) {
          posts.add(new Post(rs.getInt("post_id"), rs.getInt("post_user_id"),
              threadId, rs.getString("post"), rs.getString("creat_date"),
              rs.getString("mod_date"), rs.getString("user_name"),
              rs.getString("picture")));
        }
      }
    } catch (SQLException e) {
      System.err.println("Error in getPosts():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getPosts():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
  public ArrayList<Integer> getPostId(int threadId, int index,
      int totalElements) {
    ArrayList<Integer> list = new ArrayList<>();
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to get the total number of post in a thread
      stm = conn.prepareStatement("select " + Constant.POST_POST_ID_FIELD_NAME
          + " from " + Constant.POST_TABLE + " where "
          + Constant.POST_THREAD_ID_FIELD_NAME + "=? limit ?,?");
      stm.setInt(1, threadId);
      stm.setInt(2, (index - 1) * totalElements);
      stm.setInt(3, totalElements);
      ResultSet rs = stm.executeQuery();
      // Check if there was result
      while (rs.next()) {
        // Saving the total number of post
        list.add(rs.getInt("total"));
      }
    } catch (SQLException e) {
      System.err.println("Error in getTotalPosts():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getTotalPosts():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return list;
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
      stm = conn
          .prepareStatement("select count(" + Constant.POST_POST_ID_FIELD_NAME
              + ") as total from " + Constant.POST_TABLE + " where "
              + Constant.POST_THREAD_ID_FIELD_NAME + "=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      // Check if there was result
      if (rs.next()) {
        // Saving the total number of post
        total = rs.getInt("total");
      }
    } catch (SQLException e) {
      System.err.println("Error in getTotalPosts():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getTotalPosts():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
  public Post getPost(int postId) {
    Post post = null;
    PreparedStatement stm = null;
    // Creating a new connection
    db = new DBConnection();
    conn = db.getConnection();
    try {
      // SQL sentence to get a single post
      stm = conn.prepareStatement("select " + Constant.POST_USER_ID_FIELD_NAME
          + ", " + Constant.POST_THREAD_ID_FIELD_NAME + ", "
          + Constant.POST_POST_FIELD_NAME + " from " + Constant.POST_TABLE
          + " where " + Constant.POST_POST_ID_FIELD_NAME + "=?");
      stm.setInt(1, postId);
      ResultSet rs = stm.executeQuery();
      // Checking if post exists
      if (rs.next()) {
        post = new Post(postId, rs.getInt(Constant.POST_USER_ID_FIELD_NAME),
            rs.getInt(Constant.POST_THREAD_ID_FIELD_NAME),
            rs.getString(Constant.POST_POST_FIELD_NAME));
      }
    } catch (SQLException e) {
      System.err.println("Error in getPost():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getPost():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      stm = conn.prepareStatement("delete from " + Constant.POST_TABLE
          + " where " + Constant.POST_THREAD_ID_FIELD_NAME + "=?");
      stm.setInt(1, threadId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deletePostByThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connections in deletePostByThread():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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