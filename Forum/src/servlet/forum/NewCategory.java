
package servlet.forum;

import java.io.IOException;
import java.io.PrintWriter;

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
    // Creating a new category
    int categoryId = db.newCategory(request.getParameter("category_name"),
        request.getParameter("category_description"));
    try {
      if (categoryId != -1) {
        // Going to the new category
        response.setHeader("New_Category", "ok");
        response.sendRedirect("/Forum/forum.jsp?cid=" + categoryId);
      } else {
        // Going to index because of error
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("window.alert(\"There was an error while new category was creating.\")");
        out.println("</script>");
        response.setHeader("New_Category", "error");
        response.sendRedirect("/Forum/index.jsp");
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
