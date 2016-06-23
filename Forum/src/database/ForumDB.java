
package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import constants.Constant;

public class ForumDB {
  private Connection conn;

  public ForumDB() {
    new CreateTables();
  }

  /**
   * Used when you just want to create a new forum.
   * 
   * @param name
   * @return
   */
  public boolean newCategory(String name, String description) {
    boolean done = false;
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      stm = conn.prepareStatement("insert into " + Constant.FORUM_TABLE
          + " (name, description) VALUES (?, ?)");
      stm.setString(1, name);
      stm.setString(2, description);
      stm.executeUpdate();
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
    return done;
  }

  /**
   * Used when you want to create a new category in the forum with 2 moderators.
   * 
   * @param moderators
   * @param name
   * @param forumId
   * @return boolean whether it was possible to do the MySQL sentence.
   */
  public int newForum(int[] moderators, String name, String description,
      int categoryId) {
    int forumId = -1;
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      stm = conn.prepareStatement(
          "insert into " + Constant.FORUM_TABLE
              + " (name, description, forum_id) " + "values(?,?,?)",
          Statement.RETURN_GENERATED_KEYS);
      stm.setString(1, name);
      stm.setString(2, description);
      stm.setInt(3, categoryId);
      stm.executeUpdate();
      ResultSet rs = stm.getGeneratedKeys();
      if (rs.next()) {
        forumId = rs.getInt(1);
      }
      if (forumId != -1) {
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
    return forumId;
  }

  public boolean editCategory(int categoryId, String name, String description) {
    boolean done = false;
    DBConnection db = new DBConnection();
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("update " + Constant.FORUM_TABLE
          + " set name=?, description=? where id=?");
      stm.setString(1, name);
      stm.setString(2, description);
      stm.setInt(3, categoryId);
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
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean editForum(int[] moderators, String name, String description,
      int categoryId, int forumId) {
    boolean done = false;
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    PreparedStatement stm = null;
    try {
      stm = conn.prepareStatement("update " + Constant.FORUM_TABLE
          + " set name=?, description=?, forum_id=? where id=?;");
      stm.setString(1, name);
      stm.setString(2, name);
      stm.setInt(3, categoryId);
      stm.setInt(4, forumId);
      stm.executeUpdate();
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
    return done;
  }

  public boolean deleteForum(int forumId) {
    boolean done = false;
    DBConnection db = new DBConnection();
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn
          .prepareStatement("delete from " + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, forumId);
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
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean deleteCategory(int categoryId) {
    boolean done = false;
    DBConnection db = new DBConnection();
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn
          .prepareStatement("delete from " + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, categoryId);
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
      } catch (SQLException e) {
        System.out.println(e.getMessage());
      }
    }
    return done;
  }

  public boolean deleteModerators(int forumId) {
    boolean done = false;
    PreparedStatement stm = null;
    DBConnection db = new DBConnection();
    conn = db.getConnection();
    try {
      System.out.println("delete from " + Constant.MODERATOR_TABLE + " where forum_id=8");
      stm = conn.prepareStatement(
          "delete from " + Constant.MODERATOR_TABLE + " where forum_id=?");
      stm.setInt(1, forumId);
      stm.executeUpdate();
      done = true;
    } catch (SQLException e) {
      System.err.println("Delete mods => " + e.getMessage());
    } finally {
      try {
        if (stm != null) {
          stm.close();
        }
        if(conn != null){
          conn.close();
        }
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return done;
  }

  /**
   * Get a specific forum.
   * 
   * @param forumId
   * @return String[3] => name(0), description(1), forum_id(2)
   */
  public String[] getForum(int forumId) {
    String[] forum = new String[3];
    DBConnection db = new DBConnection();
    PreparedStatement stm = null;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("select name, description, forum_id from "
          + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, forumId);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        forum[0] = rs.getString("name");
        forum[1] = rs.getString("description");
        forum[2] = Integer.toString(rs.getInt("forum_id"));
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
    return forum;
  }

  /**
   * Get all forums by a category id.
   * 
   * @param categoryId
   * @return An ArrayList < String[] > => id(0), name(1)
   */
  public ArrayList<String[]> getForums(int categoryId) {
    ArrayList<String[]> forums = new ArrayList<>();
    PreparedStatement stm = null;
    DBConnection db = new DBConnection();
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("select id, name " + " from "
          + Constant.FORUM_TABLE + " where forum_id=?");
      stm.setInt(1, categoryId);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name") };
        forums.add(tmp);
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
    DBConnection db = new DBConnection();
    try {
      conn = db.getConnection();
      stm = conn.createStatement();
      ResultSet rs = stm.executeQuery(
          "select id, name, forum_id " + " from " + Constant.FORUM_TABLE);
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), rs.getObject("forum_id") == null ? "-1"
                : Integer.toString(rs.getInt("forum_id")) };
        forums.add(tmp);
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
        System.out.println(e.getMessage());
      }
    }
    return forums;
  }

  public int getForumId(int threadId) {
    PreparedStatement stm = null;
    DBConnection db = new DBConnection();
    int forumId = -1;
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement(
          "select forum_id from " + Constant.THREAD_TABLE + " where id=?");
      stm.setInt(1, threadId);
      ResultSet rs = stm.executeQuery();
      if (rs.next()) {
        forumId = rs.getInt("forum_id");
      }
    } catch (SQLException e) {
      System.err.println(e.getSQLState());
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
    DBConnection db = new DBConnection();
    try {
      conn = db.getConnection();
      stm = conn.createStatement();
      ResultSet rs = stm.executeQuery("select id, name, description from "
          + Constant.FORUM_TABLE + " where forum_id is null");
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), rs.getString("description") };
        categories.add(tmp);
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
        System.out.println(e.getMessage());
      }
    }
    return categories;
  }

  /**
   * Used to get a category and its name and category.
   * 
   * @param id
   * @return ArrayList[String] => name(0), description(1)
   */
  public String[] getCategory(int id) {
    String[] category = new String[2];
    PreparedStatement stm = null;
    DBConnection db = new DBConnection();
    try {
      conn = db.getConnection();
      stm = conn.prepareStatement("select name, description from "
          + Constant.FORUM_TABLE + " where id=?");
      stm.setInt(1, id);
      ResultSet rs = stm.executeQuery();
      while (rs.next()) {
        category[0] = rs.getString("name");
        category[1] = rs.getString("description");
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
        System.out.println(e.getMessage());
      }
    }
    return category;
  }
}