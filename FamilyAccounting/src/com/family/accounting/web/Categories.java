package com.family.accounting.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.family.accounting.libraries.*;

public class Categories extends HttpServlet {
  /**
   * 
   */
  private Utils utils;
  private Database db;
  private static int numberElements;
  private int numberPage;
  private static final long serialVersionUID = 1L;
  private int userId;
  private int mode;
  private int totalElements;
  private int menuMode;
  private HttpSession session;

  public void init() throws ServletException {
    ServletConfig config = getServletConfig();
    numberElements = Integer
        .parseInt(config.getInitParameter("numberElements"));
    numberPage = Integer.parseInt(config.getInitParameter("numberPage"));
    userId = Integer.parseInt(config.getInitParameter("userId"));
    mode = Integer.parseInt(config.getInitParameter("mode"));
    menuMode = 2;
    utils = new Utils();
    db = new Database(utils.getDatabasePath());
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // **********************************************
    // variables and setters.
    // **********************************************
    session = request.getSession(true);
    userId = session.getAttribute("userId") == null ? -1
        : Integer.parseInt(session.getAttribute("userId").toString());
    totalElements = db.countRowCategories(userId);
    numberPage = request.getParameter("p") != null
        ? Integer.parseInt(request.getParameter("p")) - 1 : numberPage;
    ArrayList<String[]> categories = db
        .getCategories(numberPage * numberElements, numberElements, userId);
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    StringBuilder htmlCode = new StringBuilder();
    // **********************************************
    // making the html code.
    // **********************************************

    htmlCode.append("<HTML><HEAD><TITLE>Categories</TITLE>\n"
        + "\n<link rel=\"stylesheet\" "
        + "type=\"text/css\" href=\"resources/css/general.css\">\n"
        + "<script type=\"text/javascript\" src=\"resources/js/category.js\"></script>"
        + "<script type=\"text/javascript\" src=\"resources/js/general.js\"></script>"
        + "</HEAD>\n<BODY><div id=\"div_body\">"
        + utils.menu(menuMode, request));
    if (categories != null) {
      htmlCode
          .append("<div id=\"div_inner_body\"><table><tr><td class=\"td_header\">Name</a></td></tr>");
      for (int i = 0; i < categories.size(); i++) {
        htmlCode.append("<tr>\n");
        String[] strArr = categories.get(i);
        htmlCode.append("<td class=\"td_data\"><a href=\"movements?cid="
            + strArr[0] + "\">" + strArr[1]
            + "</a></td><td><div class=\"div_edit\"><a href=\"ne-category.jsp?m=1&cid="
            + strArr[0] + "\"><img class=\"img_menu\" "
            + "src=\"resources/images/edit.png\"></a></div></td><td><div "
            + "class=\"div_delete\"><input type=\"image\" class=\"img_menu\" "
            + "src=\"resources/images/delete.png\" onclick=\"deleteCategory("
            + strArr[0] + ")\"></div></td></tr>");
      }
      htmlCode
          .append("</table>" + utils.pagination(totalElements, (numberPage + 1),
              numberElements, request.getRequestURL().toString()));
    } else if (categories == null) {
      if (userId != -1) {
        htmlCode.append("<div id=\"div_inner_body\">There are no categories");
      } else {
        htmlCode.append("<div id=\"div_inner_body\">Please login to watch all categories.");
      }
    }

    htmlCode.append("</div></div></BODY></HTML>");
    out.println(utils.getDocType() + htmlCode.toString());
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    mode = Integer.parseInt(request.getParameter("mode"));
    session = request.getSession(false);
    userId = session.getAttribute("userId") == null ? -1
        : Integer.parseInt(session.getAttribute("userId").toString());
    String name = request.getParameter("name");
    int categoryId = request.getParameter("categoryId") == null ? -1
        : Integer.parseInt(request.getParameter("categoryId"));
    if (userId != -1) {
      switch (mode) {
        case 0: // create mode
          db.newCategory(name, userId);
          mode = -1;
          break;
        case 1: // edit mode
          db.editCategory(categoryId, name, userId);
          mode = -1;
          break;
        case 2: // delete mode
          db.deleteCategory(categoryId, userId);
          mode = -1;
          break;
      }
    } else {
      response.setContentType("html/text");
      PrintWriter out = response.getWriter();
      out.print("<html><head></head><body><script type=\"text/javascript\">"
          + "window.alert(\"Please login if you want to complete your action.\");"
          + "window.history.back();</script></body></html>");
    }
    doGet(request, response);
  }
}