package com.family.accounting.libraries;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * This class has utilities as a menu and constants
 * 
 * @author Pedro Bermudez
 *
 */
public class Utils {
  private static String docType;
  private static String databasePath;
  private HttpSession session;
  private String tableSource, tableMovement, tableCategory, tableUser;

  public Utils() {
    docType = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 "
        + "Transitional//EN\">\n";
    databasePath = "test.db";
  }

  public void setTableSource(String table) {
    tableSource = table;
  }

  public String getTableSource() {
    return tableSource;
  }

  public void setTableMovement(String table) {
    tableMovement = table;
  }

  public String getTableMovement() {
    return tableMovement;
  }

  public void setTableCategory(String table) {
    tableCategory = table;
  }

  public String getTableCategory() {
    return tableCategory;
  }

  public void setTableUser(String table) {
    tableUser = table;
  }

  public String getTableUser() {
    return tableUser;
  }

  public String getDatabasePath() {
    return databasePath;
  }

  public void setDatabasePath(String path) {
    databasePath = path;
  }

  public String getDocType() {
    return docType;
  }

  public void setDocType(String doc) {
    docType = doc;
  }

  /**
   * 
   * @param mode
   *          0 = source; 1 = movement; 2 = category; mode < 0 || mode > 2 =
   *          others;
   * @return String with the full menu. Format: <div><span><a>the
   *         url</a></span></div> and if user is not login after new, edit and
   *         delete buttons add a login, otherwise say the user and let a link
   *         to edit or logout.
   */
  public String menu(int mode, HttpServletRequest request) {
    StringBuilder menu = new StringBuilder();
    session = request.getSession(true);
    int userId = session.getAttribute("userId") == null ? -1
        : Integer.parseInt(session.getAttribute("userId").toString());
    menu.append(
        "<div id=\"div_menu\"><div><span class=\"span_menu_header\"><a href=\"sources\">Money Sources</a></span>"
            + "<span class=\"span_menu_header\"><a href=\"movements\">Movements</a></span>"
            + "<span class=\"span_menu_header\"><a href=\"categories\">Categories</a></span>");

    if (userId == -1) {
      return menu.append(
          "<div id=\"div_menu_bottom\"><form action=\"user\" method=\"POST\">"
              + "<input type=\"hidden\" name=\"mode\" value=\"3\">"
              + "User: <input class=\"input_menu_login\" type=\"text\" name=\"userName\">"
              + " Password: <input class=\"input_menu_login\""
              + " type=\"password\" name=\"password\"> "
              + "<input class=\"input_menu_login\" type=\"submit\" "
              + "value=\"Login\"><span id=\"span_menu_bottom_new_user\">"
              + "<a href=\"ne-user.jsp?m=0\"><br>New user</a></span>"
              + "</form></div></div></div>")
          .toString();
    } else {
      String userName = (String) session.getAttribute("userName");
      menu.append("<div id=\"div_menu_bottom\"><span id=\"span_user_logged\"><a href=\"ne-user.jsp?m=1&uid="
          + userId + "\">" + userName + "</a></span><form action=\"user\" method=\"POST\">"
          + "<input type=\"hidden\" name=\"mode\" value=\"4\">"
          + "<button id=\"button_as_text\" onclick=\"sendForm(this)\">Logout</button></form></div>");
      switch (mode) {
        case 0: // source
          menu.append(
              "\n</div><div><span><a href=\"ne-source.jsp?m=0\"><img class=\"img_menu\" src=\"resources/images/new.png\"></a></span>"
                  + "<span><input type=\"image\" class=\"img_menu\" src=\"resources/images/edit.png\" onclick=\"showEdit()\"></span>"
                  + "<span><input type=\"image\" class id=\"img_menu\" src=\"resources/images/delete.png\" onclick=\"showDelete()\"></span>");
          break;
        case 1: // movement
          menu.append(
              "\n</div><div><span><a href=\"ne-movement.jsp?m=0\"><img class=\"img_menu\" src=\"resources/images/new.png\"></a></span>"
                  + "<span><input type=\"image\" class=\"img_menu\" src=\"resources/images/edit.png\" onclick=\"showEdit()\"></span>"
                  + "<span><input type=\"image\" class id=\"img_menu\" src=\"resources/images/delete.png\" onclick=\"showDelete()\"></span>"
                  );
          break;
        case 2: // category
          menu.append(
              "\n</div><div><span><a href=\"ne-category.jsp?m=0\"><img class=\"img_menu\" src=\"resources/images/new.png\"></a></span>"
                  + "<span><input type=\"image\" class=\"img_menu\" src=\"resources/images/edit.png\" onclick=\"showEdit()\"></span>"
                  + "<span><input type=\"image\" class id=\"img_menu\" src=\"resources/images/delete.png\" onclick=\"showDelete()\"></span>"
                  );
          break;
      }
    }
    return menu.append("</div></div>").toString();
  }

  public String pagination(int totalElements, int currentPage,
      int numberElementsPerPage, String url) {
    StringBuilder pag = new StringBuilder();
    String newUrl = url.indexOf('=') != -1
        ? url.substring(0, url.indexOf('=') + 1) : url + "?p=";
    pag.append("<div id=\"div_pagination\">");
    int totalPages = (totalElements / numberElementsPerPage) + 1;
    if (totalPages == 1) {
      return pag.append("</div>").toString();
    }
    if (currentPage == 1) {
      pag.append("<span>1</span>");
    } else {
      pag.append("<span><a href=\"" + newUrl + "1\">1</a></span>");
    }
    if (currentPage - 1 > 1) {
      pag.append("<span><a href=\"" + newUrl + (currentPage - 1) + "\">"
          + (currentPage - 1) + "</a></span>");
    }
    if (currentPage >= 2 && currentPage <= totalPages - 1) {
      pag.append("<span>" + currentPage + "</span>");
    }
    if (currentPage + 1 <= totalPages - 1) {
      pag.append("<span><a href=\"" + newUrl + (currentPage + 1) + "\">"
          + (currentPage + 1) + "</a></span>");
    }
    if (currentPage < totalPages) {
      pag.append("<span><a href=\"" + newUrl + totalPages + "\">" + totalPages
          + "</a></span>");
    } else {
      pag.append("<span>" + totalPages + "</span>");
    }
    return pag.append("</div></div>").toString();
  }
}
