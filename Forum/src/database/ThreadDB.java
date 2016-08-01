
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import constants.Constant;

/**
 * Class related to operation with database about thread table
 * 
 * @author pedro
 *
 */
public class ThreadDB {
  private Connection conn;
  private DBConnection db;
  private PreparedStatement stm;

  public ThreadDB() {
  }

  /**
   * Create a new thread and return the last ID.
   * 
   * @param forumId
   *          Forum id which belongs to this thread
   * @param userId
   *          User id who creates the thread
   * @param title
   *          Name for the thread
   * @return The last thread ID
   */
  public int newThread(int forumId, int userId, String title) {
    // The thread id if it couldn't be possible to create a new one then thread
    // id will be -1
    int threadId = -1;
    stm = null;
    String sql = "";
    try {
      // Creating the connection
      db = new DBConnection();
      conn = db.getConnection();
      // Inserting a new thread in the database and getting the id
      sql = "insert into " + Constant.THREAD_TABLE + " (forum_id, author, name)"
          + " values (?,?,?)";
      stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      stm.setInt(1, forumId); // forum_id
      stm.setInt(2, userId); // user_id
      stm.setString(3, title); // name
      stm.executeUpdate();
      // Getting the new thread id
      ResultSet rs = stm.getGeneratedKeys();
      if (rs.next()) {
        // Saving the last thread id generated
        threadId = rs.getInt(1);
      }
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
      System.err.println("2? => " + userId);
      System.err.println("3? => " + title);
    } finally {
      try {
        // Closing all sockets and connections
        close();
      } catch (SQLException e) {
        System.err.println(
            "Error closing connections in newThread() => " + e.getMessage());
      }
    }
    // if it was wrong return -1 otherwise return the last thread id
    return threadId;
  }

  /**
   * Edit an existing thread in database
   * 
   * @param threadId
   *          The thread id which we want to edit
   * @param forumId
   *          The forum id which this thread belongs
   * @param title
   *          The name for this thread
   * @return True if it's done otherwise false
   */
  public boolean editThread(int threadId, int forumId, String title) {
    boolean done = false;
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Editing the thread
      sql = "update " + Constant.THREAD_TABLE
          + " set forum_id=?, name=? where id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      stm.setString(2, title); // name
      stm.setInt(3, threadId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in editThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
      System.err.println("2? => " + title);
      System.err.println("3? => " + threadId);
    } finally {
      try {
        // Calling "close" to close all sockets and connections
        close();
      } catch (SQLException e) {
        System.err.println(
            "Error closing connectinos in editThread() => " + e.getMessage());
      }
    }
    return done;
  }

  /**
   * Moving a thread to another forum
   * 
   * @param threadId
   *          The thread we want to relocate
   * @param forumId
   *          The forum we want to put this thread
   * @return True if it's done otherwise false
   */
  public boolean moveThreadToForum(int threadId, int forumId) {
    boolean done = false;
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Moving this thread to another forum
      sql = "update " + Constant.THREAD_TABLE + " set forum_id=? where id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      stm.setInt(2, threadId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in moveThreadToForum():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
      System.err.println("2? => " + threadId);
    } finally {
      try {
        // Calling to "close" to close all sockets and connections
        close();
      } catch (SQLException e) {
        System.err.println("Error closing connecions in moveThreadToForum() => "
            + e.getMessage());
      }
    }
    return done;
  }

  /**
   * Delete just a thread
   * 
   * @param threadId
   *          The thread we want to remove of the database
   * @return True if it's done otherwise return false
   */
  public boolean deleteThread(int threadId) {
    boolean done = false;
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Deleting the thread
      sql = "delete from " + Constant.THREAD_TABLE + " where id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, threadId); // id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + threadId);
    } finally {
      try {
        // Closing all sockets and connection
        close();
      } catch (SQLException e) {
        System.err.println("Error closing all connections in deleteThread() => "
            + e.getMessage());
      }
    }
    return done;
  }

  /**
   * Delete all threads given by a forum. For proper use must be used after
   * deleting all posts in each thread in that forum, otherwise posts will be in
   * the database consuming resources
   * 
   * @param forumId
   *          Forum id which we want to delete all its threads
   * @return True it's done otherwise false will be returned
   */
  public boolean deleteThreadByForum(int forumId) {
    boolean done = false;
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Deleting all threads of the forum given
      sql = "delete from " + Constant.THREAD_TABLE + " where forum_id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteThreadByForum():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
    } finally {
      try {
        // Closing all connections and sockets
        close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connections in deleteThreadByForum()+> "
                + e.getMessage());
      }
    }
    return done;
  }

  /**
   * Get a list with a number of threads in a forum (watch total elements to
   * know how many threads there will be
   * 
   * @param forumId
   *          Forum id which we want to get all threads
   * @param index
   *          Current page
   * @param totalElements
   *          Total elements displayed on screen
   * @return ArrayList < String[] > => id(0), name(1), author(2), user_name(3)
   */
  public ArrayList<String[]> getThreadList(int forumId, int index,
      int totalElements) {
    ArrayList<String[]> list = new ArrayList<>();
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting threads, by default 25 threads per page
      sql = "select " + Constant.THREAD_TABLE + ".id, " + Constant.THREAD_TABLE
          + ".name, " + Constant.THREAD_TABLE + ".author, "
          + Constant.USER_TABLE + ".user_name as user_name  from "
          + Constant.THREAD_TABLE + " inner join " + Constant.USER_TABLE
          + " on " + Constant.USER_TABLE + ".id=" + Constant.THREAD_TABLE
          + ".author where forum_id=? limit " + ((index - 1) * totalElements)
          + ", " + totalElements;
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        // Saving each thread in a list (ArrayList<String>)
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), Integer.toString(rs.getInt("author")),
            rs.getString("user_name") };
        list.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
      System.err.println("2? => " + index);
      System.err.println("3? => " + totalElements);
    } finally {
      try {
        // Closing all connection and sockets
        close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getThreadList() => "
            + e.getMessage());
      }
    }
    return list;
  }

  /**
   * Get all threads id in a forum
   * 
   * @param forumId
   *          Forum id which we want to get all threads
   * @return ArrayList < Integer > = id(0)
   */
  public ArrayList<Integer> getThreadIdList(int forumId) {
    ArrayList<Integer> list = new ArrayList<>();
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting all threads in that forum
      sql = "select id from " + Constant.THREAD_TABLE + " where forum_id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        // Saving the data in an ArrayList
        list.add(rs.getInt("id"));
      }
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
    } finally {
      try {
        // Closing all connections and sockets
        close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getThreadIdList()=> "
            + e.getMessage());
      }
    }
    return list;
  }

  /**
   * Get the total thread in a forum, used by pagination
   * 
   * @param forumId
   *          Forum id which we want to count all threads
   * @return The total threads in a forum, if there're not threads, total will
   *         be 0
   */
  public int getTotalThreads(int forumId) {
    stm = null;
    int total = 0;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting the total threads in a forum
      sql = "select count(id) as count from " + Constant.THREAD_TABLE
          + " where forum_id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      ResultSet rs = stm.executeQuery();
      if (rs.next()) total = rs.getInt("count");
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
    } finally {
      try {
        // Closing all connections and sockets
        close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getTotalThreads() => "
            + e.getMessage());
      }
    }
    return total;
  }

  /**
   * Get a thread.
   * 
   * @param threadId
   *          Thread id which we want to get
   * @return String[3] => name[0] forum_id[1] author[2] otherwise null will be
   *         returned
   */
  public String[] getThread(int threadId) {
    String[] thread = null;
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting the thread
      sql = "select name, author, forum_id from " + Constant.THREAD_TABLE
          + " where id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, threadId); // id
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        // Saving the thread in a String array
        thread = new String[3];
        thread[0] = rs.getString("name");
        thread[1] = Integer.toString(rs.getInt("forum_id"));
        thread[2] = Integer.toString(rs.getInt("author"));
      }
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + threadId);
    } finally {
      try {
        // Closing all connections and sockets
        close();
      } catch (SQLException e) {
        System.err.println(
            "Error closing connections in getThread()" + e.getMessage());
      }
    }
    return thread;
  }

  /**
   * Get the title of a thread
   * 
   * @param threadId
   *          Thread id which we want to get the title
   * @return The title otherwise empty string
   */
  public String getThreadTitle(int threadId) {
    stm = null;
    String title;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting the title
      sql = "select name from " + Constant.THREAD_TABLE + " where id=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, threadId); // id
      ResultSet rs = stm.executeQuery();
      // Saving the title
      title = rs.next() ? rs.getString("name") : "";
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + threadId);
      title = "";
    } finally {
      try {
        // Closing all connections and sockets
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return title;
  }

  /**
   * Get 30 threads from the most recent to 30, to show it in index page
   * 
   * @return 6 => thread_id(0), thread_name(1), forum_id(2), forum_name(3),
   *         user_id(4), user_name(5)
   */
  public ArrayList<String[]> get30Threads() {
    ArrayList<String[]> threads = new ArrayList<>();
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // TODO check sql sentence
      // Getting 30 threads from the most recent to 30
      sql = "select " + Constant.POST_TABLE + ".thread_id as thread_id, "
          + Constant.THREAD_TABLE + ".name as thread_name, "
          + Constant.POST_TABLE + ".user_id as user_id, "
          + Constant.THREAD_TABLE + ".author as author, "
          + Constant.THREAD_TABLE + ".forum_id as forum_id, "
          + Constant.USER_TABLE + ".user_name as user_name, "
          + Constant.FORUM_TABLE + ".name as forum_name, " + Constant.POST_TABLE
          + ".creation_date from " + Constant.POST_TABLE + ", "
          + Constant.THREAD_TABLE + ", " + Constant.USER_TABLE + ", "
          + Constant.FORUM_TABLE + " where " + Constant.THREAD_TABLE
          + ".forum_id=" + Constant.FORUM_TABLE + ".id and thread_id="
          + Constant.THREAD_TABLE + ".id and " + Constant.USER_TABLE
          + ".id=user_id group by " + Constant.THREAD_TABLE + ".id order by "
          + Constant.POST_TABLE + ".creation_date desc limit 0, 30";
      stm = conn.prepareStatement(sql);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        // Saving he thread data into an ArrayList
        String[] tmp = { rs.getString("thread_id"), rs.getString("thread_name"),
            rs.getString("forum_id"), rs.getString("forum_name"),
            rs.getString("author"), rs.getString("user_name") };
        threads.add(tmp);
      }
    } catch (SQLException e) {
      System.err
          .println("Error in get30Threads() message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
    } finally {
      try {
        // Closing all connections and sockets
        close();
      } catch (SQLException e) {
        System.err.println(
            "Error closing connections in get30Threads() => " + e.getMessage());
      }
    }
    return threads;
  }

  /**
   * Method to close all sockets and connections
   * 
   * @throws SQLException
   */
  private void close() throws SQLException {
    // Checking stm, conn, db and close if they are not null
    if (stm != null) stm.close();
    if (conn != null) conn.close();
    if (db != null) db.close();
  }
}
