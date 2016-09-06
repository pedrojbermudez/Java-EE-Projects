
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

/**
 * Class related to operation with database about forums
 * 
 * @author pedro
 *
 */
public class ForumDB {
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Inserting a new category into database
      stm = conn.prepareStatement(
          "insert into " + Constant.FORUM_TABLE + " ("
              + Constant.FORUM_NAME_FIELD_NAME + ", "
              + Constant.FORUM_DESCRIPTION_FIELD_NAME + ") VALUES (?, ?)",
          Statement.RETURN_GENERATED_KEYS);
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.executeUpdate();
      ResultSet rs = stm.getGeneratedKeys(); // last id
      categoryId = rs.next() ? rs.getInt(1) : -1;
    } catch (SQLException e) {
      System.err.println("Error in newCategory():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (conn != null) conn.close();
        if (stm != null) stm.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing all connections in newCategory():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a connection to the database
      db = new DBConnection();
      conn = db.getConnection();
      // Inserting a new forum into the database
      stm = conn.prepareStatement(
          "insert into " + Constant.FORUM_TABLE + " ("
              + Constant.FORUM_NAME_FIELD_NAME + ", "
              + Constant.FORUM_DESCRIPTION_FIELD_NAME + ", "
              + Constant.FORUM_CATEGORY_ID_FIELD_NAME + ") " + "values(?,?,?)",
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
        insertModerators(moderators, forumId);
      }
    } catch (SQLException e) {
      System.err.println("Error in newForum():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in newForum():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Editing the chosen category
      stm = conn.prepareStatement("update " + Constant.FORUM_TABLE + " set "
          + Constant.FORUM_NAME_FIELD_NAME + "=?, "
          + Constant.FORUM_DESCRIPTION_FIELD_NAME + "=? where "
          + Constant.FORUM_FORUM_ID_FIELD_NAME + "=?");
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.setInt(3, categoryId); // category_id
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in editCategory():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in editCategory():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Editing forum
      stm = conn.prepareStatement("update " + Constant.FORUM_TABLE + " set "
          + Constant.FORUM_NAME_FIELD_NAME + "=?, "
          + Constant.FORUM_DESCRIPTION_FIELD_NAME + "=?, "
          + Constant.FORUM_CATEGORY_ID_FIELD_NAME + "=? where "
          + Constant.FORUM_FORUM_ID_FIELD_NAME + "=?;");
      stm.setString(1, name); // name
      stm.setString(2, description); // description
      stm.setInt(3, categoryId); // category_id
      stm.setInt(4, forumId); // forum_id
      stm.executeUpdate();
      // Deleting previous moderators
      deleteModerators(forumId);
      // Adding the new moderators
      insertModerators(moderators, forumId);
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in editForum():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in editForum():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Deleting a forum
      stm = conn.prepareStatement("delete from " + Constant.FORUM_TABLE
          + " where " + Constant.FORUM_FORUM_ID_FIELD_NAME + "=?");
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
      System.err.println("Error in deleteForum():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deleteForum():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a new connecion
      db = new DBConnection();
      conn = db.getConnection();
      // Deleting a category
      stm = conn.prepareStatement("delete from " + Constant.FORUM_TABLE
          + " where " + Constant.FORUM_FORUM_ID_FIELD_NAME + "=?");
      stm.setInt(1, categoryId);
      stm.executeUpdate();
      // Deleting forums inside in this category and its threads and posts
      deleteForumByCat(categoryId, conn);
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteCategory():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deleteCategory():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
  private void deleteForumByCat(int categoryId, Connection conn) {
    // Checking if it there's a previous connection otherwise create a new
    // connection
    PreparedStatement stm = null;
    try {
      // Select all id from category
      stm = conn.prepareStatement("select " + Constant.FORUM_FORUM_ID_FIELD_NAME
          + " from " + Constant.FORUM_TABLE + " where "
          + Constant.FORUM_CATEGORY_ID_FIELD_NAME + "=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      // Deleting each forum and its posts and messages calling deleteForum
      // passing the id we have with the select
      while (rs.next())
        deleteForum(rs.getInt(Constant.FORUM_FORUM_ID_FIELD_NAME));
    } catch (SQLException e) {
      System.err.println("Error in deleteForumByCat():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Clossing sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deleteForumByCat():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
  }

  /**
   * Insert all moderators in a specified forum
   * 
   * @param moderators
   * @param forumId
   * @return
   */
  private boolean insertModerators(int[] moderators, int forumId) {
    boolean done = false;
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      db = new DBConnection();
      conn = db.getConnection();
      for (int i = 0; i < moderators.length; i++) {
        stm = conn.prepareStatement("insert into " + Constant.MODERATOR_TABLE
            + " (" + Constant.MODERATOR_FORUM_ID_FIELD_NAME + ", "
            + Constant.MODERATOR_USER_ID_FIELD_NAME + ") values (?,?)");
        stm.setInt(1, forumId);
        stm.setInt(2, moderators[i]);
        stm.executeUpdate();
      }
    } catch (SQLException e) {
      System.err.println("Error in insertModerator():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL State => " + e.getSQLState());
    } finally {
      try {
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in insertModerator");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL State => " + e.getSQLState());
      }
    }
    return done;
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
    DBConnection db = null;
    Connection conn = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Deleting moderators
      stm = conn.prepareStatement("delete from " + Constant.MODERATOR_TABLE
          + " where " + Constant.MODERATOR_FORUM_ID_FIELD_NAME + "=?");
      stm.setInt(1, forumId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Error in deleteModerators():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in deleteModerators():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    PreparedStatement stm = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Selecting the wished forum
      stm = conn.prepareStatement("select " + Constant.FORUM_NAME_FIELD_NAME
          + ", " + Constant.FORUM_DESCRIPTION_FIELD_NAME + ", "
          + Constant.FORUM_CATEGORY_ID_FIELD_NAME + " from "
          + Constant.FORUM_TABLE + " where "
          + Constant.FORUM_FORUM_ID_FIELD_NAME + "=?");
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        // Saving the data into an string array
        forum[0] = rs.getString(Constant.FORUM_NAME_FIELD_NAME);
        forum[1] = rs.getString(Constant.FORUM_DESCRIPTION_FIELD_NAME);
        forum[2] = Integer
            .toString(rs.getInt(Constant.FORUM_CATEGORY_ID_FIELD_NAME));
      }
    } catch (SQLException e) {
      System.err.println("Error in getForum():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getForum():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Selecting all forums by a category given
      stm = conn.prepareStatement(
          "select " + Constant.FORUM_FORUM_ID_FIELD_NAME + ", "
              + Constant.FORUM_NAME_FIELD_NAME + " from " + Constant.FORUM_TABLE
              + " where " + Constant.FORUM_CATEGORY_ID_FIELD_NAME + "=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      // Saving all forums into an ArrayList in case there is a forum at least
      while (rs.next()) {
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.FORUM_FORUM_ID_FIELD_NAME)),
            rs.getString(Constant.FORUM_NAME_FIELD_NAME) };
        forums.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getForums(int):");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connection in getForums(int):");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Selecting all forums
      stm = conn.createStatement();
      ResultSet rs = stm
          .executeQuery("select " + Constant.FORUM_FORUM_ID_FIELD_NAME + ", "
              + Constant.FORUM_NAME_FIELD_NAME + ", "
              + Constant.FORUM_CATEGORY_ID_FIELD_NAME + " " + " from "
              + Constant.FORUM_TABLE);
      while (rs.next()) {
        // Saving all forum's data
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.FORUM_FORUM_ID_FIELD_NAME)),
            rs.getString(Constant.FORUM_NAME_FIELD_NAME),
            rs.getObject(Constant.FORUM_CATEGORY_ID_FIELD_NAME) == null ? "-1"
                : Integer.toString(
                    rs.getInt(Constant.FORUM_CATEGORY_ID_FIELD_NAME)) };
        forums.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getForums():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getForums():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    int forumId = -1;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Selecting the forum id
      stm = conn
          .prepareStatement("select " + Constant.THREAD_FORUM_ID_FIELD_NAME
              + " from " + Constant.THREAD_TABLE + " where "
              + Constant.THREAD_THREAD_ID_FIELD_NAME + "=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      // Saving the forum id in case there's not forum id it will be -1
      forumId = rs.next() ? rs.getInt(Constant.THREAD_FORUM_ID_FIELD_NAME) : -1;
    } catch (SQLException e) {
      System.err.println("Error in getForumId():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getForumId():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Selecting all caegories
      stm = conn.createStatement();
      ResultSet rs = stm
          .executeQuery("select " + Constant.FORUM_FORUM_ID_FIELD_NAME + ", "
              + Constant.FORUM_NAME_FIELD_NAME + ", "
              + Constant.FORUM_DESCRIPTION_FIELD_NAME + " from "
              + Constant.FORUM_TABLE + " where "
              + Constant.FORUM_CATEGORY_ID_FIELD_NAME + " is null");
      while (rs.next()) {
        // Saving category's data
        String[] tmp = {
            Integer.toString(rs.getInt(Constant.FORUM_FORUM_ID_FIELD_NAME)),
            rs.getString(Constant.FORUM_NAME_FIELD_NAME),
            rs.getString(Constant.FORUM_DESCRIPTION_FIELD_NAME) };
        categories.add(tmp);
      }
    } catch (SQLException e) {
      System.err.println("Error in getCategories():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error closing connections in getCategories():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
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
    DBConnection db = null;
    Connection conn = null;
    
    try {
      // Creating a new connection
      db = new DBConnection();
      conn = db.getConnection();
      // Selecting the specified category
      stm = conn.prepareStatement("select " + Constant.FORUM_NAME_FIELD_NAME
          + "name, " + Constant.FORUM_DESCRIPTION_FIELD_NAME + " from "
          + Constant.FORUM_TABLE + " where "
          + Constant.FORUM_FORUM_ID_FIELD_NAME + "=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        // Saving category's data
        category[0] = rs.getString(Constant.FORUM_NAME_FIELD_NAME);
        category[1] = rs.getString(Constant.FORUM_DESCRIPTION_FIELD_NAME);
      }
    } catch (SQLException e) {
      System.err.println("Error in getCategory():");
      System.err.println("Message => " + e.getMessage());
      System.err.println("SQL state => " + e.getSQLState());
    } finally {
      try {
        // Closing all sockets and connections
        if (stm != null) stm.close();
        if (conn != null) conn.close();
        db.close();
      } catch (SQLException e) {
        System.err.println("Error in getCategory():");
        System.err.println("Message => " + e.getMessage());
        System.err.println("SQL state => " + e.getSQLState());
      }
    }
    return category;
  }
}