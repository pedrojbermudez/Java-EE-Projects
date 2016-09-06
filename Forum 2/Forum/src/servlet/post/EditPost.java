
package servlet.post;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.PostDB;
import utils.Pagination;

public class EditPost extends HttpServlet {
  private static final long serialVersionUID = -7898727993091080544L;
  private PostDB db;

  public void init() {
    db = new PostDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    Pagination pag = new Pagination();
    int p = Integer.parseInt(request.getParameter("p"));
    try {
      // Editing the post
      if (db.editPost(Integer.parseInt(request.getParameter("post_id")),
          Integer.parseInt(request.getParameter("post_forum_id")),
          request.getParameter("post_post").replace("\n", "<br>"))) {
        response.setHeader("Edit_Post", "ok");
        response.sendRedirect("/Forum/thread.jsptid="
            + request.getParameter("post_thread_id") + "&p="
            + p + "#post" + request.getParameter("post_id"));
      } else {
        // Error
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println(
            "window.alert(\"There was an error while your post was being editing.\")");
        out.println("</script>");
        response.setHeader("Edit_Post", "error");
        response.sendRedirect("/Forum/thread.jsp?fid="
            + request.getParameter("post_forum_id") + "&tid="
            + request.getParameter("post_thread_id") + "&p="
            + p + "#post" + request.getParameter("post_id"));
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
