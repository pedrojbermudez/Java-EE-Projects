
package servlet.forum;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ForumDB;

public class EditCategory extends HttpServlet{
  /**
   * 
   */
  private static final long serialVersionUID = -4023680091689278135L;
  private ForumDB db;

  public void init() {
    db = new ForumDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    if (db.editCategory(Integer.parseInt(request.getParameter("category_id")),
        request.getParameter("category_name"), request.getParameter("category_description"))) {
      response.setHeader("Edit_Category", "ok");
    } else {
      response.setHeader("Edit_Category", "error");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
