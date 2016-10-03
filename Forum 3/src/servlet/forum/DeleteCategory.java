
package servlet.forum;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ForumDB;

public class DeleteCategory extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 9069611346171327813L;
  private ForumDB db;

  public void init() {
    db = new ForumDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    try {
      // Deleting a category and its forums, threads and posts
      if (db.deleteCategory(
          Integer.parseInt(request.getParameter("category_id")))) {
        response.setHeader("Delete_Category", "ok");
      } else {
        response.setHeader("Delete_Category", "error");
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println(
            "window.alert(\"There was an error while category was deleting.\")");
        out.println("</script>");
      }
      response.sendRedirect("/Forum/index,jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
