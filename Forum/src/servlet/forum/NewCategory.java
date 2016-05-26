
package servlet.forum;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ForumDB;

public class NewCategory extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 8742560787354326799L;
  private ForumDB db;

  public void init() {
    db = new ForumDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    if (db.newCategory(request.getParameter("category_name"),
        request.getParameter("category_description"))) {
      response.setHeader("New_Category", "ok");
    } else {
      response.setHeader("New_Category", "error");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
