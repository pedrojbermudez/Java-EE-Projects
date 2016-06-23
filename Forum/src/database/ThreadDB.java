
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.mysql.jdbc.Statement;

import constants.Constant;

public class ThreadDB {
  private Connection conn;
  private DBConnection db;
  private PreparedStatement stm;

  public ThreadDB() {

    db = new DBConnection();
  }

  /**
   * Create a new thread and return the last ID.
   * 
   * @param forumId
   * @param userId
   * @param title
   * @return The last ID
   */
  public int newThread(int forumId, int userId, String title) {
    int threadId = -1;
    stm = null;
    try {
      conn = db.getConnection();
      stm = conn
          .prepareStatement(
              "insert into " + Constant.THREAD_TABLE
                  + " (forum_id, author, name)" + " values (?,?,?)",
              Statement.RETURN_GENERATED_KEYS);
      stm.setInt(1, forumId);
      stm.setInt(2, userId);
      stm.setString(3, title);
      stm.executeUpdate();
      ResultSet rs = stm.getGeneratedKeys();
      if (rs.next()) {
        threadId = rs.getInt(1);
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return threadId;
  }

  public boolean editThread(int threadId, int forumId, String title) {
    boolean done = false;
    stm = null;
    try {
      conn = db.getConnection();
      System.out
          .println("thread id => " + threadId + " | forum id => " + forumId);
      stm = conn.prepareStatement("update " + Constant.THREAD_TABLE
          + " set forum_id=?, name=? where id=?");
      stm.setInt(1, forumId);
      stm.setString(2, title);
      stm.setInt(3, threadId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean moveThreadToForum(int threadId, int forumId) {
    boolean done = false;
    stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "update " + Constant.THREAD_TABLE + " set forum_id=? where id=?");
      stm.setInt(1, forumId);
      stm.setInt(2, threadId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean deleteThread(int threadId) {
    boolean done = false;
    stm = null;
    try {
      conn = db.getConnection();
      stm = conn
          .prepareStatement("delete from " + Constant.THREAD_TABLE + " where id=?");
      stm.setInt(1, threadId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean deleteThreadByForum(int forumId) {
    boolean done = false;
    stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "delete from " + Constant.THREAD_TABLE + " where forum_id=?");
      stm.setInt(1, forumId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * 
   * @param forumId
   * @return ArrayList < String[] > => id(0), name(1), author(2), user_name(3)
   */
  public ArrayList<String[]> getThreadList(int forumId) {
    ArrayList<String[]> list = new ArrayList<>();
    stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("select " + Constant.THREAD_TABLE + ".id, "
          + Constant.THREAD_TABLE + ".name, " + Constant.THREAD_TABLE
          + ".author, " + Constant.USER_TABLE + ".user_name as user_name  from "
          + Constant.THREAD_TABLE + " inner join " + Constant.USER_TABLE
          + " on " + Constant.USER_TABLE + ".id=" + Constant.THREAD_TABLE
          + ".author where forum_id=?");
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), Integer.toString(rs.getInt("author")),
            rs.getString("user_name") };
        list.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return list;
  }

  public ArrayList<Integer> getThreadIdList(int forumId) {
    ArrayList<Integer> list = new ArrayList<>();
    stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "select id from " + Constant.THREAD_TABLE + " where forum_id=?");
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        list.add(rs.getInt("id"));
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return list;
  }

  /**
   * Get a thread. String size depends on the session.
   * 
   * @param threadId
   * @param session
   * @return String[3] => name[0] forum_id[1] author[2]
   */
  public String[] getThread(int threadId) {
    String[] thread = null;
    stm = null;
    try {
      conn = db.getConnection();
      thread = new String[3];
      stm = conn.prepareStatement("select name, author, forum_id from "
          + Constant.THREAD_TABLE + " where id=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        thread[0] = rs.getString("name");
        thread[1] = Integer.toString(rs.getInt("forum_id"));
        thread[2] = Integer.toString(rs.getInt("author"));
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return thread;
  }
  
  public String getThreadTitle(int threadId){
    stm = null;
    String title;
    try{
      conn = db.getConnection();
      stm = conn.prepareStatement("select name from " + Constant.THREAD_TABLE + " where id=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();      
      title = rs.next() ? rs.getString("name") : "";
    } catch(SQLException e){
      System.err.println(e.getMessage());
      title = "";
    }
    return title;
  }

  /**
   * 
   * @return 6 => thread_id(0), thread_name(1), forum_id(2), forum_name(3),
   *         user_id(4), user_name(5)
   */
  public ArrayList<String[]> get30Threads() {
    ArrayList<String[]> threads = new ArrayList<>();
    stm = null;
    try {
      conn = db.getConnection();
      //TODO revisar todo el sql.
      stm = conn.prepareStatement(
          "select " + Constant.POST_TABLE + ".thread_id as thread_id, "
              + Constant.THREAD_TABLE + ".name as thread_name, "
              + Constant.POST_TABLE + ".user_id as user_id, "
              + Constant.THREAD_TABLE + ".forum_id as forum_id, "
              + Constant.USER_TABLE + ".user_name as user_name, "
              + Constant.FORUM_TABLE + ".name as forum_name, "
              + Constant.POST_TABLE + ".creation_date from "
              + Constant.POST_TABLE + ", " + Constant.THREAD_TABLE + ", "
              + Constant.USER_TABLE + ", " + Constant.FORUM_TABLE + " where "
              + Constant.THREAD_TABLE + ".forum_id=" + Constant.FORUM_TABLE
              + ".id and thread_id=" + Constant.THREAD_TABLE + ".id and "
              + Constant.USER_TABLE + ".id=user_id");
      System.out
          .println("select " + Constant.POST_TABLE + ".thread_id as thread_id, "
              + Constant.THREAD_TABLE + ".name as thread_name, "
              + Constant.POST_TABLE + ".user_id as user_id, "
              + Constant.THREAD_TABLE + ".forum_id as forum_id, "
              + Constant.USER_TABLE + ".user_name as user_name, "
              + Constant.FORUM_TABLE + ".name as forum_name, "
              + Constant.POST_TABLE + ".creation_date from "
              + Constant.POST_TABLE + ", " + Constant.THREAD_TABLE + ", "
              + Constant.USER_TABLE + ", " + Constant.FORUM_TABLE + " where "
              + Constant.THREAD_TABLE + ".forum_id=" + Constant.FORUM_TABLE
              + ".id and thread_id=" + Constant.THREAD_TABLE + ".id and "
              + Constant.USER_TABLE + ".id=user_id");
      ResultSet rs = stm.executeQuery();
      /*
       * ResultSet rs = stm.executeQuery("select " + Constant.THREAD_TABLE +
       * ".id as thread_id, " + Constant.THREAD_TABLE + ".name as thread_name, "
       * + Constant.THREAD_TABLE + ".author as author, " + Constant.THREAD_TABLE
       * + ".forum_id as forum_id, " + Constant.USER_TABLE +
       * ".user_name as user_name, " + Constant.FORUM_TABLE +
       * ".name as forum_name, " + Constant.POST_TABLE + ".creation_date, " +
       * Constant.POST_TABLE + ".thread_id from " + Constant.POST_TABLE +
       * " inner join " + Constant.USER_TABLE + " on " + Constant.POST_TABLE +
       * ".user_id=" + Constant.USER_TABLE + ".id inner join " +
       * Constant.FORUM_TABLE + " on " + Constant.THREAD_TABLE + ".forum_id=" +
       * Constant.FORUM_TABLE + ".id inner join " + Constant.THREAD_TABLE +
       * " on " + Constant.POST_TABLE + ".id=" + Constant.THREAD_TABLE +
       * ".id order by " + Constant.POST_TABLE + ".creation_date desc");
       */
      int i = 0;
      while (rs.next() && i < 30) {
        String[] tmp = { rs.getString("thread_id"), rs.getString("thread_name"),
            rs.getString("forum_id"), rs.getString("forum_name"),
            rs.getString("author"), rs.getString("user_name") };
        threads.add(tmp);
        i++;
      }
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
    return threads;
  }
  
  private void close() throws SQLException {
    if (stm != null) {
      stm.close();
    }
    if (conn != null) {
      conn.close();
    }
    db.close();
  }
}
