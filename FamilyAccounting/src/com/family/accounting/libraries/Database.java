package com.family.accounting.libraries;

import java.applet.Applet;
import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;;

public class Database extends Applet {
  private static final long serialVersionUID = -6203437374511205246L;
  private String path;
  private MD5String md5;
  private static Utils utils;
  private static Connection conn;

  /**
   * Constructor with database path.
   * 
   * @param path
   *          - The path which the DB is located.
   * @return - Connection if failed returns null.
   */
  public Database(String path) {
    this.path = path;
    this.md5 = new MD5String();
    utils = new Utils();
    utils.setTableUser("user");
    utils.setTableMovement("movement");
    utils.setTableSource("money_source");
    utils.setTableCategory("category");
    this.createTables();

  }

  /**
   * Constructor without arguments. Use a default path in the user home path.
   */
  public Database() {
    this(System.getProperty("user.home") + File.separator + "test.db");
  }

  /**
   * Connect to a database
   * 
   * @return - Default value is null but if the connection was realized return a
   *         Connection
   */
  private void connection() {
    conn = null;
    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.err.print(e.getMessage());
    }
    try {
      conn = DriverManager.getConnection("jdbc:sqlite:" + path);
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Create all tables if not exist.
   */
  public void createTables() {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("create table if not exists " + utils.getTableUser()
          + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, name blob, surname blob, "
          + "email blob not null, "
          + "user_name blob not null UNIQUE, password blob not null);");
      stm.executeUpdate("create table if not exists " + utils.getTableCategory()
          + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
          + " name blob not null, user_id int not null, "
          + "constraint user_id_sourcefk foreign key(user_id) references "
          + utils.getTableUser()
          + "(id) on update cascade on delete cascade);");
      stm.executeUpdate("create table if not exists " + utils.getTableSource()
          + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
          + "name blob not null, total int not null, user_id int not null,"
          + " constraint user_id_sourcefk foreign key(user_id) references "
          + utils.getTableUser()
          + "(id) on update cascade on delete cascade);");
      stm.executeUpdate("create table if not exists " + utils.getTableMovement()
          + " (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
          + "source_id int not null, user_id int not null, category_id int not null, "
          + "name mediumblob not null, "
          + "movement_date date not null, income int, outgoing int, "
          + "constraint source_id_movementsfk foreign key(source_id) "
          + "references " + utils.getTableSource()
          + "(id) on update cascade on delete cascade, "
          + "constraint category_id_movementfk foreign key(category_id) references "
          + utils.getTableCategory()
          + "(id) on update cascade on delete cascade);");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  // ******************************************************
  // Counter functions.
  // ******************************************************
  /**
   * 
   * @param userId
   *          The user logged in the web site.
   * @return int Number of row in source table. The number of rows depends only
   *         by user id.
   */
  public int countRowSource(int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    int result = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select count(id) as count from "
          + utils.getTableSource() + " where user_id=" + userId + ";");
      if (rs.next()) {
        result = rs.getInt("count");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * 
   * @param userId
   *          The user logged in the web site.
   * @return int Number of row in movement table. The number of rows depends
   *         only by user id.
   */
  public int countRowMovements(int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    int result = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select count(id) as count from "
          + utils.getTableMovement() + " where user_id=" + userId + ";");
      if (rs.next()) {
        result = rs.getInt("count");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * 
   * @param userId
   *          The user logged in the web site.
   * @return int Number of row in category table. The number of rows depends
   *         only by user id.
   */
  public int countRowCategories(int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    int result = 0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(
          "select count(id) as count from " + utils.getTableCategory()

      + " where user_id=" + userId + ";");
      if (rs.next()) {
        result = rs.getInt("count");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  // ******************************************************
  // user functions.
  // ******************************************************

  /**
   * Create a new user in the database. The table used is "user" by default. You
   * can change the name using setTableUser(table_name) from Utils class.
   * 
   * @param name
   * @param surname
   * @param email
   * @param userName
   * @param password
   */
  public void newUser(String name, String surname, String email,
      String userName, String password) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("insert into " + utils.getTableUser()
          + " (name, surname, email, user_name, password) values(\"" + name
          + "\", \"" + surname + "\", \"" + email + "\", \"" + userName
          + "\", \"" + md5.getMD5(password) + "\");");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * Edit an existing user. Can only change name, surname, and email.
   * 
   * @param userId
   * @param name
   * @param surname
   * @param email
   */
  public void editUser(int userId, String name, String surname, String email) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("update " + utils.getTableUser() + " set name=\"" + name
          + "\", surname=\"" + surname + "\", email=\"" + email + "\" where id="
          + userId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * This function only change the password. Once did can't be given the old
   * password.
   * 
   * @param userId
   * @param password
   */
  public void changePassword(int userId, String password) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("update " + utils.getTableUser() + " set password=\""
          + md5.getMD5(password) + "\" where id=" + userId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * Delete an user and his/her sources, movements and categories from the
   * database.
   * 
   * @param id
   */
  public void deleteUser(int id) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("PRAGMA foreign_keys = ON;");
      stm.executeUpdate(
          "delete from " + utils.getTableUser() + " where id=" + id);
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
      } catch (SQLException e2) {
        System.err.println(e2.getMessage());
      }
    }
  }

  /**
   * Get name, surname, email.
   * 
   * @param userID
   * @return String[3] = name[0], surname[1], email[2] if incorrect return null.
   */
  public String[] getUser(int userID) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    String[] res = new String[4];
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select name, surname, email from "
          + utils.getTableUser() + " where id=" + userID);
      if (rs.next()) {
        res[0] = rs.getString("name");
        res[1] = rs.getString("surname");
        res[2] = rs.getString("email");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return res;
  }

  /**
   * Get id and user_name when the user wants to login.
   * 
   * @param userName
   * @param password
   * @return String[2] = id[0], user_name[1] if login incorrect return null.
   */
  public String[] getUser(String userName, String password) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    String[] res = new String[2];
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select id, user_name from " + utils.getTableUser()
          + " where user_name=\"" + userName + "\" and password=\""
          + md5.getMD5(password) + "\"");
      if (rs.next()) {
        res[0] = Integer.toString(rs.getInt("id"));
        res[1] = rs.getString("user_name");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return res;
  }

  // ******************************************************
  // source functions.
  // ******************************************************

  /**
   * Create a new money source in the database. The table used is "money_source"
   * by default. You can change the name using setTableSource(table_name) from
   * Utils class.
   * 
   * @param name
   * @param total
   * @param userId
   */
  public void newSource(String name, double total, int userId) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("insert into " + utils.getTableSource()
          + " (name, total, user_id) values ('" + name + "', " + total + ", "
          + userId + ");");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * @param sourceId
   * @param name
   * @param total
   * @param userId
   */
  public void editSource(int sourceId, String name, double total, int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null, rs2 = null;
    double currentTotal, countTotal = 0.0;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select total from " + utils.getTableSource()
          + " where id=" + sourceId + " and user_id=" + userId + ";");
      if (rs.next()) {
        currentTotal = rs.getDouble("total");
        if (currentTotal != total) {
          rs2 = stm.executeQuery("select income, outgoing from "
              + utils.getTableMovement() + " where source_id=" + sourceId);
          while (rs2.next()) {
            countTotal -= rs2.getDouble("outgoing");
            countTotal += rs2.getDouble("income");
          }
          stm.executeUpdate("update " + utils.getTableSource() + " set name=\""
              + name + "\", total=" + (total + countTotal) + " where id="
              + sourceId);
        } else {
          stm.executeUpdate("update " + utils.getTableSource() + " set name=\""
              + name + "\" where id=" + sourceId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * @param id
   * @param userId
   */
  public void deleteSource(int id, int userId) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      // deleteMovements(id);
      stm.executeUpdate("PRAGMA foreign_keys = ON;");
      stm.executeUpdate("delete from " + utils.getTableSource() + " where id="
          + id + " and user_id=" + userId);
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
      } catch (SQLException e2) {
        System.err.println(e2.getMessage());
      }
    }
  }

  /**
   * 
   * @param id
   * @return String[2] = name[0], total[1]
   */
  public String[] getSource(int id, int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    String[] result = new String[2];
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select name, total from " + utils.getTableSource()
          + " where user_id=" + userId + " and id=" + id);
      if (rs.next()) {
        result[0] = rs.getString("name");
        result[1] = Double.toString(rs.getDouble("total"));
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * Get all sources limited by numberElements parameter.
   * 
   * @param startIndex
   * @param numberElements
   * @param userId
   * @return ArrayList<String[3]> = id[0], name[1], total[2]
   */
  public ArrayList<String[]> getSources(int startIndex, int numberElements,
      int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select id, name, total from "
          + utils.getTableSource() + " where user_id=" + userId + " limit "
          + startIndex + ", " + numberElements + ";");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), Double.toString(rs.getDouble("total")) };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * Get all sources with no limit of number elements.
   * 
   * @param userId
   * @return ArrayList<String[3]> = id[0], name[1], total[2]
   */
  public ArrayList<String[]> getSources(int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select id, name, total from "
          + utils.getTableSource() + " where user_id=" + userId + ";");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), Double.toString(rs.getDouble("total")) };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * Get all sources when the user create a new movement.
   * 
   * @param userId
   * @return ArrayList<String[]> = id[1], name[2]
   */
  public ArrayList<String[]> getSourcesComboBox(int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select id, name from " + utils.getTableSource()
          + " where user_id=" + userId + ";");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name") };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * Update total value in the table "money_source" (default name).
   * 
   * @param sourceId
   * @param movement
   */
  public void updateTotal(int sourceId, double movement, int userId) {
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate(
          "update " + utils.getTableSource() + " set total=total+" + movement
              + " where id=" + sourceId + " and user_id=" + userId + ";");
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  // ******************************************************
  // movement functions.
  // ******************************************************

  /**
   * Create a new movement in the database. The table used is "movement" by
   * default. You can change the name using setTableMovement(table_name) from
   * Utils class.
   * 
   * @param name
   * @param movementDate
   *          The format date must be yyyy/mm/dd
   * @param income
   * @param outgoing
   * @param sourceId
   * @param userId
   * @param categoryId
   */
  public void newMovement(String name, String movementDate, double income,
      double outgoing, int sourceId, int userId, int categoryId) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("insert into " + utils.getTableMovement()
          + "(source_id, name, movement_date, income, outgoing, category_id, user_id) "
          + "values (" + sourceId + ", \"" + name + "\", \"" + movementDate
          + "\", " + income + ", " + outgoing + ", " + categoryId + ", "
          + userId + ");");
      if (outgoing > 0.0) {
        updateTotal(sourceId, -outgoing, userId);
      } else {
        updateTotal(sourceId, income, userId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * @param movId
   * @param sourceId
   * @param name
   * @param movementDate
   *          The date format must be "yyyy/mm/dd"
   * @param income
   * @param outgoing
   * @param oldIncome
   * @param oldOutgoing
   * @param userId
   * @param categoryId
   */
  public void editMovement(int movId, int sourceId, String name,
      String movementDate, double income, double outgoing, double oldIncome,
      double oldOutgoing, int userId, int categoryId) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("update " + utils.getTableMovement() + " set source_id="
          + sourceId + ", name=\"" + name + "\", movement_date=\""
          + movementDate + "\", income=" + income + ", outgoing=" + outgoing
          + " where id=" + movId + " and user_id=" + userId + ";");
      if (income != oldIncome || outgoing != oldOutgoing) {
        updateTotal(sourceId, income, userId);
        updateTotal(sourceId, -outgoing, userId);
        updateTotal(sourceId, -oldIncome, userId);
        updateTotal(sourceId, oldOutgoing, userId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * @param id
   * @param userId
   * @param oldIncome
   * @param oldOutgoing
   */
  public void deleteMovement(int id, int sourceId, int userId, double oldIncome,
      double oldOutgoing) {
    connection();
    Statement stm = null;

    updateTotal(sourceId, -oldIncome, userId);
    updateTotal(sourceId, oldOutgoing, userId);
    try {
      stm = conn.createStatement();
      stm.executeUpdate("delete from " + utils.getTableMovement()
          + " where user_id=" + userId + " and id=" + id);
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
      } catch (SQLException e2) {
        System.err.println(e2.getMessage());
      }
    }
  }

  public void deleteMovements(int sourceId) {
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("delete from " + utils.getTableMovement()
          + " where source_id=" + sourceId);
    } catch (SQLException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * 
   * @param id
   * @param userId
   * @return String[6] = name[0], movement_date[1](dd/mm/yyyy) or (dd-mm-yyyy),
   *         income[2], outgoing[3], source_id[4], category_id[5]
   */
  public String[] getMovement(int id, int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    String[] result = new String[6];
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(
          "select name, source_id, movement_date, income, outgoing, category_id "
              + "from " + utils.getTableMovement() + " where id=" + id + ";");
      if (rs.next()) {
        String tmpDate = rs.getString("movement_date");
        String[] arrayTmp;
        String date;
        if (tmpDate.indexOf('-') != -1) {
          arrayTmp = tmpDate.split("-");
          date = arrayTmp[2] + "-" + arrayTmp[1] + "-" + arrayTmp[0];
        } else {
          arrayTmp = tmpDate.split("/");
          date = arrayTmp[2] + "/" + arrayTmp[1] + "/" + arrayTmp[0];
        }
        result[0] = rs.getString("name");
        result[1] = date;
        result[2] = Double.toString(rs.getDouble("income"));
        result[3] = Double.toString(rs.getDouble("outgoing"));
        result[4] = Integer.toString(rs.getInt("source_id"));
        result[5] = Integer.toString(rs.getInt("category_id"));
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * 
   * @param startIndex
   * @param numberElements
   * @param userId
   * @return Arraylist<String[9]> = id[0], name[1], date[2] income[3],
   *         outgoing[4], source_id[5], source_name[6], category_id[7],
   *         category_name[8]
   * 
   */
  public ArrayList<String[]> getMovements(int startIndex, int numberElements,
      int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    try {
      stm = conn.createStatement();
      String sqlMovement = utils.getTableMovement() + ".id, "
          + utils.getTableMovement() + ".name, " + utils.getTableMovement()
          + ".income, " + utils.getTableMovement() + ".outgoing, "
          + utils.getTableMovement() + ".movement_date, ";
      String sqlSource = utils.getTableMovement() + ".source_id, "
          + utils.getTableSource() + ".name as source_name, ";
      String sqlCategory = utils.getTableMovement() + ".category_id, "
          + utils.getTableCategory() + ".name as category_name";

      rs = stm.executeQuery("select " + sqlMovement + sqlSource + sqlCategory
          + " from " + utils.getTableMovement() + " inner join "
          + utils.getTableSource() + ", " + utils.getTableCategory() + " on "
          + utils.getTableMovement() + ".user_id=" + userId + " and "
          + utils.getTableCategory() + ".id=" + utils.getTableMovement()
          + ".category_id and " + utils.getTableSource() + ".id="
          + utils.getTableMovement()
          + ".source_id order by movement_date desc limit " + startIndex + ", "
          + numberElements);
      result = new ArrayList<String[]>();
      while (rs.next()) {
        // Getting and formatting date.
        String tmpDate = rs.getString("movement_date");
        String[] arrayTmp;
        String date;
        if (tmpDate.indexOf('-') != -1) {
          arrayTmp = tmpDate.split("-");
          date = arrayTmp[2] + "-" + arrayTmp[1] + "-" + arrayTmp[0];
        } else {
          arrayTmp = tmpDate.split("/");
          date = arrayTmp[2] + "/" + arrayTmp[1] + "/" + arrayTmp[0];
        }
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), date, Double.toString(rs.getDouble("income")),
            Double.toString(rs.getDouble("outgoing")),
            Integer.toString(rs.getInt("source_id")),
            rs.getString("source_name"),
            Integer.toString(rs.getInt("category_id")),
            rs.getString("category_name") };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * 
   * @param startIndex
   * @param numberElements
   * @param userId
   * @param categoryId
   * @return Arraylist<String[9]> = id[0], name[1], date[2] income[3],
   *         outgoing[4], source_id[5], source_name[6], category_id[7],
   *         category_name[8]
   */
  public ArrayList<String[]> getMovements(int startIndex, int numberElements,
      int userId, int categoryId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    // Creating the sql. There will be several parts to be more clean.
    try {
      stm = conn.createStatement();
      String sqlMovement = utils.getTableMovement() + ".id, "
          + utils.getTableMovement() + ".name, " + utils.getTableMovement()
          + ".income, " + utils.getTableMovement() + ".outgoing, "
          + utils.getTableMovement() + ".movement_date, ";
      String sqlSource = utils.getTableMovement() + ".source_id, "
          + utils.getTableSource() + ".name as source_name, ";
      String sqlCategory = utils.getTableMovement() + ".category_id, "
          + utils.getTableCategory() + ".name as category_name";
      rs = stm.executeQuery("select " + sqlMovement + sqlSource + sqlCategory
          + " from " + utils.getTableMovement() + " inner join "
          + utils.getTableSource() + ", " + utils.getTableCategory() + " on "
          + utils.getTableCategory() + ".id=" + categoryId + " and "
          + utils.getTableCategory() + ".id=" + utils.getTableMovement()
          + ".category_id and " + utils.getTableMovement() + ".source_id="
          + utils.getTableSource() + ".id and " + utils.getTableMovement()
          + ".user_id=" + userId + " order by movement_date desc limit "
          + startIndex + ", " + numberElements + ";");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String tmpDate = rs.getString("movement_date");
        String[] arrayTmp;
        String date;
        if (tmpDate.indexOf('-') != -1) {
          arrayTmp = tmpDate.split("-");
          date = arrayTmp[2] + "-" + arrayTmp[1] + "-" + arrayTmp[0];
        } else {
          arrayTmp = tmpDate.split("/");
          date = arrayTmp[2] + "/" + arrayTmp[1] + "/" + arrayTmp[0];
        }
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), date, Double.toString(rs.getDouble("income")),
            Double.toString(rs.getDouble("outgoing")),
            Integer.toString(rs.getInt("source_id")),
            rs.getString("source_name"),
            Integer.toString(rs.getInt("category_id")),
            rs.getString("category_name") };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }
  
  /**
   * 
   * @param userId
   * @param categoryId
   * @return
   */
  public ArrayList<String[]> getMovements(int userId, int categoryId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    // Creating the sql. There will be several parts to be more clean.
    try {
      stm = conn.createStatement();
      String sqlMovement = utils.getTableMovement() + ".id, "
          + utils.getTableMovement() + ".name, " + utils.getTableMovement()
          + ".income, " + utils.getTableMovement() + ".outgoing, "
          + utils.getTableMovement() + ".movement_date, ";
      String sqlSource = utils.getTableMovement() + ".source_id, "
          + utils.getTableSource() + ".name as source_name, ";
      String sqlCategory = utils.getTableMovement() + ".category_id, "
          + utils.getTableCategory() + ".name as category_name";
      rs = stm.executeQuery("select " + sqlMovement + sqlSource + sqlCategory
          + " from " + utils.getTableMovement() + " inner join "
          + utils.getTableSource() + ", " + utils.getTableCategory() + " on "
          + utils.getTableCategory() + ".id=" + categoryId + " and "
          + utils.getTableCategory() + ".id=" + utils.getTableMovement()
          + ".category_id and " + utils.getTableMovement() + ".source_id="
          + utils.getTableSource() + ".id and " + utils.getTableMovement()
          + ".user_id=" + userId + " order by movement_date desc;");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String tmpDate = rs.getString("movement_date");
        String[] arrayTmp;
        String date;
        if (tmpDate.indexOf('-') != -1) {
          arrayTmp = tmpDate.split("-");
          date = arrayTmp[2] + "-" + arrayTmp[1] + "-" + arrayTmp[0];
        } else {
          arrayTmp = tmpDate.split("/");
          date = arrayTmp[2] + "/" + arrayTmp[1] + "/" + arrayTmp[0];
        }
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name"), date, Double.toString(rs.getDouble("income")),
            Double.toString(rs.getDouble("outgoing")),
            Integer.toString(rs.getInt("source_id")),
            rs.getString("source_name"),
            Integer.toString(rs.getInt("category_id")),
            rs.getString("category_name") };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }
  // ******************************************************
  // category functions.
  // ******************************************************

  /**
   * Create a new category in the database. The table used is "category" by
   * default. You can change the name using setTableCategory(table_name) from
   * Utils class.
   * 
   * @param name
   * @param userId
   */
  public void newCategory(String name, int userId) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("insert into " + utils.getTableCategory()
          + " (name, user_id) values(\"" + name + "\", " + userId + ");");
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  public void editCategory(int categoryId, String name, int userId) {
    connection();
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("update " + utils.getTableCategory() + " set name=\""
          + name + "\" where id=" + categoryId + " and user_id=" + userId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * @param id
   * @param userId
   */
  public void deleteCategory(int id, int userId) {
    ArrayList<String[]> movList = getMovements(userId, id);
    connection();
    for (int i = 0; i < movList.size(); i++) {
      String[] movArr =movList.get(i); 
      if(!movArr[3].equals("0")){
        updateTotal(Integer.parseInt(movArr[5]),
            -Double.parseDouble(movArr[3]), userId);  
      } else {
        updateTotal(Integer.parseInt(movArr[5]),
            +Double.parseDouble(movArr[4]), userId); 
      }
    }
    Statement stm = null;
    try {
      stm = conn.createStatement();
      stm.executeUpdate("PRAGMA foreign_keys = ON;");
      stm.executeUpdate("delete from " + utils.getTableCategory() + " where id="
          + id + " and user_id=" + userId);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  /**
   * 
   * @param categoryId
   * @param userId
   * @return String name of category. null if can't select the name.
   */
  public String getCategory(int categoryId, int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    String res = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select name from " + utils.getTableCategory()
          + " where id=" + categoryId + " and user_id=" + userId);
      res = rs.next() ? rs.getString("name") : "";
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return res;
  }

  /**
   * Get all categories with limit (startIndex-(numberElements - startIndex))
   * 
   * @param startIndex
   * @param numberElements
   * @param userId
   * @return ArrayList<String[2]> = id[0], name[2]
   */
  public ArrayList<String[]> getCategories(int startIndex, int numberElements,
      int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery(
          "select id, name from " + utils.getTableCategory() + " where user_id="
              + userId + " limit " + startIndex + ", " + numberElements + ";");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name") };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }

  /**
   * Get all categories with no limit.
   * 
   * @param userId
   * @return ArrayList<String[2]> = id[0], name[1]
   */
  public ArrayList<String[]> getCategories(int userId) {
    connection();
    Statement stm = null;
    ResultSet rs = null;
    ArrayList<String[]> result = null;
    try {
      stm = conn.createStatement();
      rs = stm.executeQuery("select id, name from " + utils.getTableCategory()
          + " where user_id=" + userId + ";");
      result = new ArrayList<String[]>();
      while (rs.next()) {
        String[] tmp = { Integer.toString(rs.getInt("id")),
            rs.getString("name") };
        result.add(tmp);
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
      } catch (SQLException e) {
        System.err.println(e.getMessage());
      }
    }
    return result;
  }
}