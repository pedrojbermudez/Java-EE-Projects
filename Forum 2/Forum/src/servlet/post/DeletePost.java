
package servlet.post;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.PostDB;

public class DeletePost extends HttpServlet {
  private static final long serialVersionUID = 6379133826116209269L;
  private PostDB db;

  public void init() {
    db = new PostDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    boolean administrator = Integer
        .parseInt(request.getAttribute("id").toString()) == 0 ? true : false;
    HttpSession session = request.getSession();
    try {
      // Deleting post
      if (administrator
          && db.deletePost(Integer.parseInt(request.getParameter("id")),
              Integer.parseInt(session.getAttribute("id").toString()))) {
        response.setHeader("Delete_Post", "ok");
      } else {
        // An error was occurred 
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println(
            "window.alert(\"There was an error while category was deleting.\")");
        out.println("</script>");
        response.setHeader("Delete_Post", "error");
      }
      response.sendRedirect("/Forum/index,jsp");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
