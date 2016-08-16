
package servlet.post;

import java.io.IOException;
import java.io.PrintWriter;

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
    System.out.println(request.getParameter("post_post"));
    Pagination pag = new Pagination();
    pag.setElementsPerPage(25);
    pag.setTotalElements((new PostDB()).getTotalPosts(
        Integer.parseInt(request.getParameter("post_thread_id"))));
    try {
      // Editing the post
      if (db.editPost(Integer.parseInt(request.getParameter("post_id")),
          request.getParameter("post_post").replace("\n", "<br>"))) {
        response.setHeader("Edit_Post", "ok");
        response.sendRedirect("/Forum/thread.jsp?tid="
            + request.getParameter("post_thread_id") + "&p="
            + pag.getTotalPage() + "#post" + request.getParameter("post_id"));
      } else {
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println(
            "window.alert(\"There was an error while your post was being editing.\")");
        out.println("</script>");
        response.setHeader("Edit_Post", "error");

        response.sendRedirect("/Forum/thread.jsp?tid="
            + request.getParameter("post_thread_id") + "&p="
            + pag.getTotalPage() + "#post" + request.getParameter("post_id"));      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
