
package servlet.forum;

import java.io.IOException;
import java.io.PrintWriter;

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
    // Editing an existing category
    try {
      if (db.editCategory(Integer.parseInt(request.getParameter("category_id")),
          request.getParameter("category_name"), request.getParameter("category_description"))) {
        // Edit category was succeed
        response.setHeader("Edit_Category", "ok");
      } else {
        // An error while category was editing
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println("window.alert(\"There was an error while category was editing.\")");
        out.println("</script>");
        response.setHeader("Edit_Category", "error");
      }
      // Going to the category
      response.sendRedirect("/Forum/forum.jsp?cid="+request.getParameter("category_id"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
