
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import constants.Constant;

/**
 * Class related to operation with database about forums
 * 
 * @author pedro
 *
 */
public class ForumDB {
  private Connection conn;

  public ForumDB() {
    new CreateTables();
  }

  /**
   * Add a new category into database.
   * 
   * @param name
   *          Name of the category
   * @param description
   *          A little description of the category
   * @return true if it's done otherwise false
   */
  public int newCategory(String name, String description) {
    int categoryId = -1;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Inserting a new category into database
      stm = conn.prepareStatement(
          "insert into " + Constant.FORUM_TABLE
              + " (name, description) VALUES (?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.executeUpdate();
      ResultSet rs = stm.getGeneratedKeys(); // last id
      categoryId = rs.next() ? rs.getInt(1) : -1;
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (conn != null) conn.close();
        if (stm != null) stm.close();
        db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return categoryId;
  }

  /**
   * Add a new forum into database
   * 
   * @param moderators
   *          Integer array that contains all moderator ids
   * @param name
   *          Forum name
   * @param description
   *          A description for the forum
   * @param categoryId
   *          Category id
   * @return The forum id created otherwise forum id will be -1
   */
  public int newForum(int[] moderators, String name, String description,
      int categoryId) {
    // If not possible to create a new forum forumId will be -1
    int forumId = -1;
    // Creating a connection to the database
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Inserting a new forum into the database
      stm = conn.prepareStatement(
          "insert into " + Constant.FORUM_TABLE
              + " (name, description, forum_id) " + "values(?,?,?)",
          Statement.RETURN_GENERATED_KEYS);
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.setInt(3, categoryId); // forum_id
      stm.executeUpdate();
      // Catching the new forum id generated
      ResultSet rs = stm.getGeneratedKeys(); // last id
      forumId = rs.next() ? rs.getInt(1) : -1;
      if (forumId != -1) {
        // if forum id is not equals to -1 insert each moderator into the
        // database
        for (int i = 0; i < moderators.length; i++) {
          stm = conn.prepareStatement("insert into " + Constant.MODERATOR_TABLE
              + " (forum_id, user_id) values (?,?);");
          stm.setInt(1, forumId);
          stm.setInt(2, moderators[i]);
          stm.executeUpdate();
        }
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
        System.out.println(e.getMessage());
      }
    }
    return forumId;
  }

  /**
   * Editing an existing category in database
   * 
   * @param categoryId
   *          Category id we want to edit
   * @param name
   *          The new category name for this category
   * @param description
   *          The new description for this category
   * @return true if it's done otherwise false
   */
  public boolean editCategory(int categoryId, String name, String description) {
    boolean done = false;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Editing the chosen category
      stm = conn.prepareStatement("update " + Constant.FORUM_TABLE
          + " set name=?, description=? where id=?");
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.setInt(3, categoryId); // forum_id
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
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Editing an existing forum in database
   * 
   * @param moderators
   *          Integer array with moderator id
   * @param name
   *          New name for the forum
   * @param description
   *          New description for the forum
   * @param categoryId
   *          New category which this forum belongs
   * @param forumId
   *          The forum we want to edit
   * @return true if it's done otherwise false
   */
  public boolean editForum(int[] moderators, String name, String description,
      int categoryId, int forumId) {
    boolean done = false;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Editing forum
      stm = conn.prepareStatement("update " + Constant.FORUM_TABLE
          + " set name=?, description=?, forum_id=? where id=?;");
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.setInt(3, categoryId); // forum_id
      stm.setInt(4, forumId); // id
      stm.executeUpdate();
      // Deleting previous moderators
      deleteModerators(forumId);
      // Adding the new moderators
      for (int i = 0; i < moderators.length; i++) {
        stm = conn.prepareStatement("insert into " + Constant.MODERATOR_TABLE
            + " (forum_id, user_id) values (?,?)");
        stm.setInt(1, forumId);
        stm.setInt(2, moderators[i]);
        stm.executeUpdate();
      }
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
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Deleting an existing forum in database
   * 
   * @param forumId
   *          Forum id which we want to delete
   * @return true if it's done otherwise false
   */
  public boolean deleteForum(int forumId) {
    boolean done = false;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Deleting a forum
      stm = conn.prepareStatement(
          "delete from " + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, forumId);
      stm.executeUpdate();
      // Proceeding to delete threads and post in this forum
      ThreadDB threadDb = new ThreadDB();
      ArrayList<Integer> threadIdList = threadDb.getThreadIdList(forumId);
      // Deleting posts by thread
      for (int i = 0; i < threadIdList.size(); i++) {
        (new PostDB()).deletePostbyThread(threadIdList.get(i));
      }
      // Deleting threads in this forum
      threadDb.deleteThreadByForum(forumId);
      // Deleting moderators
      deleteModerators(forumId);
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
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Deleting a category in database
   * 
   * @param categoryId
   *          Category id which we want to delete
   * @return True if it's done otherwise false
   */
  public boolean deleteCategory(int categoryId) {
    boolean done = false;
    // Creating a new connecion
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Deleting a category
      stm = conn.prepareStatement(
          "delete from " + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, categoryId);
      stm.executeUpdate();
      // Deleting forums inside in this category and its threads and posts
      deleteForumByCat(categoryId);
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
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Delete all forums given by a category id
   * 
   * @param categoryId
   *          Category id which we want to delete
   */
  private void deleteForumByCat(int categoryId) {
    // Checking if it there's a previous connection otherwise create a new
    // connection
    DBConnection db = null;
    PreparedStatement stm = null;
    if (conn == null) {
      db = new DBConnection();
      conn = db.getConnection();
    }
    try {
      // Select all ids from category
      stm = conn.prepareStatement(
          "select id from " + Constant.FORUM_TABLE + " where forum_id=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      // Deleting each forum and its posts and messages calling deleteForum
      // passing the ids we have with the select
      while (rs.next())
        deleteForum(rs.getInt("id"));
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    } finally {
      try {
        // Clossing sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        if (db != null) db.close();
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * Deleting moderators from a forum
   * 
   * @param forumId
   *          Forum id which we want to delete
   * @return True if it's done otherwise false
   */
  private boolean deleteModerators(int forumId) {
    boolean done = false;
    PreparedStatement stm = null;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    try {
      // Deleting moderators
      stm = conn.prepareStatement(
          "delete from " + Constant.MODERATOR_TABLE + " where forum_id=?");
      stm.setInt(1, forumId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Delete mods => " + e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Get a specific forum.
   * 
   * @param forumId
   *          Forum id which we want to get
   * @return String[3] => name(0), description(1), forum_id(2)
   */
  public String[] getForum(int forumId) {
    String[] forum = new String[3];
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      // Selecting the wished forum
      stm = conn.prepareStatement("select name, description, forum_id from "
          + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        // Saving the data into an string array
        forum[0] = rs.getString("name");
        forum[1] = rs.getString("description");
        forum[2] = Integer.toString(rs.getInt("forum_id"));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return forum;
  }

  /**
   * Get all forums by a category id.
   * 
   * @param categoryId
   *          Category id which we want to get forums
   * @return An ArrayList < String[] > => id(0), name(1)
   */
  public ArrayList<String[]> getForums(int categoryId) {
    ArrayList<String[]> forums = new ArrayList<>();
    PreparedStatement stm = null;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    try {
      // Selecting all forums by a category given
      stm = conn.prepareStatement("select id, name " + " from "
          + Constant.FORUM_TABLE + " where forum_id=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      // Saving all forums into an ArrayList in case there is a forum at least
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name") };
        forums.add(tmp);
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
        System.out.println(e.getMessage());
      }
    }
    return forums;
  }

  /**
   * Get all forums.
   * 
   * @return An ArrayList < String[] > => id(0), name(1), forum_id(2)
   */
  public ArrayList<String[]> getForums() {
    ArrayList<String[]> forums = new ArrayList<>();
    Statement stm = null;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    try {
      // Selecting all forums
      stm = conn.createStatement();
      ResultSet rs = stm.executeQuery(
          "select id, name, forum_id " + " from " + Constant.FORUM_TABLE);
      while (rs.next()) {
        // Saving all forum's data
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), rs.getObject("forum_id") == null ? "-1"
                : Integer.toString(rs.getInt("forum_id")) };
        forums.add(tmp);
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
        System.out.println(e.getMessage());
      }
    }
    return forums;
  }

  /**
   * Get a forum id from a thread
   * 
   * @param threadId
   *          Thread id which we want to get the forum id
   * @return Forum id
   */
  public int getForumId(int threadId) {
    PreparedStatement stm = null;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    // TODO check if there's not problem closing connection
    // In case there's a problem forum id will be -1
    int forumId = -1;
    try {
      // Selecting the forum id
      stm = conn.prepareStatement(
          "select forum_id from " + Constant.THREAD_TABLE + " where id=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      // Saving the forum id in case there's not forum id it will be -1
      forumId = rs.next() ? rs.getInt("forum_id") : -1;
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return forumId;
  }

  /**
   * Return all categories in the database used by menu.
   * 
   * @return ArrayList < String[] > => id(0), name(1), description(2)
   */
  public ArrayList<String[]> getCategories() {
    ArrayList<String[]> categories = new ArrayList<>();
    Statement stm = null;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    try {
      // Selecting all caegories
      stm = conn.createStatement();
      ResultSet rs = stm.executeQuery("select id, name, description from "
          + Constant.FORUM_TABLE + " where forum_id is null");
      while (rs.next()) {
        // Saving category's data
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), rs.getString("description") };
        categories.add(tmp);
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
        System.out.println(e.getMessage());
      }
    }
    return categories;
  }

  /**
   * Used to get a category and its name and category.
   * 
   * @param categoryId
   * @return ArrayList[String] => name(0), description(1)
   */
  public String[] getCategory(int categoryId) {
    String[] category = new String[2];
    PreparedStatement stm = null;
    // Creating a new connection
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    try {
      // Selecting the specified category
      stm = conn.prepareStatement("select name, description from "
          + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        // Saving category's data
        category[0] = rs.getString("name");
        category[1] = rs.getString("description");
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
        System.out.println(e.getMessage());
      }
    }
    return category;
  }
}