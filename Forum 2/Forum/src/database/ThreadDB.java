
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

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
      sql = "insert into " + Constant.THREAD_TABLE + " ("
          + Constant.THREAD_FORUM_ID_FIELD_NAME + ", "
          + Constant.THREAD_USER_ID_FIELD_NAME + ", "
          + Constant.THREAD_NAME_FIELD_NAME + ") values (?,?,?)";
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in newThread():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "update " + Constant.THREAD_TABLE + " set "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + "=?, "
          + Constant.THREAD_NAME_FIELD_NAME + "=? where "
          + Constant.THREAD_THREAD_ID_FIELD_NAME + "=?";
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in editThread() => ");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "update " + Constant.THREAD_TABLE + " set "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + "=? where "
          + Constant.THREAD_THREAD_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      stm.setInt(2, threadId); // id
      stm.executeUpdate();
      // Updating indexes on reference_index
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connecions in moveThreadToForum():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "delete from " + Constant.THREAD_TABLE + " where "
          + Constant.THREAD_THREAD_ID_FIELD_NAME + "=?";
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing all connections in deleteThread():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "delete from " + Constant.THREAD_TABLE + " where "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + "=?";
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err
            .println("Error closing connections in deleteThreadByForum()");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    long start = System.currentTimeMillis();
    ArrayList<String[]> list = new ArrayList<>();
    stm = null;
    String sql = "";
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting threads, by default 25 threads per page
      sql = "select thread_id from post where forum_id=? group by thread_id "
          + "order by MAX(post_id) desc limit " + ((index - 1) * totalElements)
          + ", " + totalElements;
      System.out.println("sql=> " + sql);
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      ResultSet rs2;
      long end = System.currentTimeMillis();
      System.out.println("Time => " + (end-start));
      while (rs.next()) {
        // Setting user_id, thread_id and forum_id as String
        String threadId = Integer.toString(rs.getInt("thread_id"));
        // Getting the user name from a user (just get one user)
        sql = "select " + Constant.THREAD_USER_ID_FIELD_NAME + ", "
            + Constant.THREAD_NAME_FIELD_NAME + " from " + Constant.THREAD_TABLE
            + " where " + Constant.THREAD_THREAD_ID_FIELD_NAME + "=" + threadId;
        System.out.println("SQL thread => " + sql);
        stm = conn.prepareStatement(sql);
        rs2 = stm.executeQuery();
        String userId;
        String threadName;
        if(rs2.next()){
          userId = Integer.toString(rs2.getInt(Constant.THREAD_USER_ID_FIELD_NAME));
          threadName = rs2.getString(Constant.THREAD_NAME_FIELD_NAME);
        } else {
          userId = "";
          threadName = "";
        }
        sql = "select " + Constant.USER_USER_NAME_FIELD_NAME
            + " from " + Constant.USER_TABLE
            + " where " + Constant.USER_USER_ID_FIELD_NAME + "=" + userId;
        System.out.println("SQL user => " + sql);
        stm = conn.prepareStatement(sql);
        rs2 = stm.executeQuery();
        String userName = rs2.next()
            ? rs2.getString(Constant.USER_USER_NAME_FIELD_NAME) : "";
        // Saving the data into a temporal Array String and add it to an
        // ArrayList<String[]>
        String[] tmp = { threadId, threadName, userId, userName };
        list.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getThreadList():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
      System.err.println("2? => " + index);
      System.err.println("3? => " + totalElements);
    } finally {
      try {
        // Closing all connection and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getThreadList()");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "select " + Constant.THREAD_THREAD_ID_FIELD_NAME + " from "
          + Constant.THREAD_TABLE + " where "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + "=?";
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getThreadIdList()");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "select count(" + Constant.THREAD_THREAD_ID_FIELD_NAME
          + ") as total from " + Constant.THREAD_TABLE + " where "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, forumId); // forum_id
      ResultSet rs = stm.executeQuery();
      if (rs.next()) total = rs.getInt("total");
    } catch (SQLException e) {
      System.err.println("Error in newThread():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
      System.err.println("1? => " + forumId);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getTotalThreads()");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "select " + Constant.THREAD_NAME_FIELD_NAME + ", "
          + Constant.THREAD_USER_ID_FIELD_NAME + ", "
          + Constant.THREAD_FORUM_ID_FIELD_NAME + " from "
          + Constant.THREAD_TABLE + " where "
          + Constant.THREAD_THREAD_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, threadId); // id
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        // Saving the thread in a String array
        thread = new String[3];
        thread[0] = rs.getString(Constant.THREAD_NAME_FIELD_NAME);
        thread[1] = Integer
            .toString(rs.getInt(Constant.THREAD_FORUM_ID_FIELD_NAME));
        thread[2] = Integer
            .toString(rs.getInt(Constant.THREAD_USER_ID_FIELD_NAME));
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getThread()");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
      sql = "select " + Constant.THREAD_NAME_FIELD_NAME + " from "
          + Constant.THREAD_TABLE + " where "
          + Constant.THREAD_THREAD_ID_FIELD_NAME + "=?";
      stm = conn.prepareStatement(sql);
      stm.setInt(1, threadId); // id
      ResultSet rs = stm.executeQuery();
      // Saving the title
      title = rs.next() ? rs.getString(Constant.THREAD_NAME_FIELD_NAME) : "";
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
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in newThread():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return title;
  }

  /**
   * Get 30 threads from the most recent to 30, to show it in index page.
   * 
   * @return 6 => thread_id(0), thread_name(1), forum_id(2), forum_name(3),
   *         user_id(4), user_name(5)
   */
  public ArrayList<String[]> get30Threads() {
    ArrayList<String[]> threads = new ArrayList<>();
    stm = null;
    String sql = "";
    long start, end;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Getting 30 threads from the most recent to 30
      sql = "select " + Constant.POST_THREAD_ID_FIELD_NAME + " from "
          + Constant.POST_TABLE + " group by "
          + Constant.POST_THREAD_ID_FIELD_NAME + " order by MAX("
          + Constant.POST_POST_ID_FIELD_NAME + ") desc limit 0,30;";
      stm = conn.prepareStatement(sql);
      start = System.currentTimeMillis();
      ResultSet rs = stm.executeQuery();
      ResultSet rs2;
      while (rs.next()) {
        // Setting user_id, thread_id and forum_id as String
        String threadId = Integer.toString(rs.getInt("thread_id"));
        // Getting the user name from a user (just get one user)
        sql = "select user_id, forum_id, name from thread where thread_id="
            + threadId;
        stm = conn.prepareStatement(sql);
        rs2 = stm.executeQuery();
        String userId;
        String forumId;
        String userName;
        String forumName;
        String threadName;
        if (rs2.next()) {
          userId = Integer.toString(rs2.getInt("user_id"));
          forumId = Integer.toString(rs2.getInt("forum_id"));
          threadName = rs2.getString("name");
        } else {
          userId = "";
          forumId = "";
          threadName = "";
        }
        sql = "select user_name from user where user_id=" + userId;
        stm = conn.prepareStatement(sql);
        rs2 = stm.executeQuery();
        userName = rs2.next() ? rs2.getString("user_name") : "";
        sql = "select name from thread where thread_id=" + threadId;
        stm = conn.prepareStatement(sql);
        rs2 = stm.executeQuery();
        threadName = rs2.next() ? rs2.getString(Constant.THREAD_NAME_FIELD_NAME)
            : "";
        // Getting forum name from a forum (just get one forum)
        sql = "select " + Constant.FORUM_NAME_FIELD_NAME + " from "
            + Constant.FORUM_TABLE + " where "
            + Constant.FORUM_FORUM_ID_FIELD_NAME + "=" + forumId;
        stm = conn.prepareStatement(sql);
        rs2 = stm.executeQuery();
        forumName = rs2.next() ? rs2.getString(Constant.FORUM_NAME_FIELD_NAME)
            : "";
        // Saving the data into a temporal Array String and add it to an
        // ArrayList<String[]>
        String[] tmp = { threadId, threadName, forumId, forumName, userId,
            userName };
        threads.add(tmp);
      }
      end = System.currentTimeMillis();
      System.out.println("SQL TIME => " + (end - start));
    } catch (SQLException e) {
      System.err.println("Error in get30Threads()");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
      System.err.println("SQL sentence => " + sql);
    } finally {
      try {
        // Closing all connections and sockets
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in get30Threads()");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
        System.err.println("SQL sentence => " + sql);
      }
    }
    return threads;
  }
}
