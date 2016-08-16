
package servlet.post;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.PostDB;
import utils.Pagination;

public class NewPost extends HttpServlet {
  private static final long serialVersionUID = -4242323963421615995L;
  private PostDB db;

  public void init() {
    db = new PostDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    int threadId = Integer.parseInt(request.getParameter("post_thread_id"));
    int postId = session != null && session.getAttribute("id") != null
        ? db.newPost(Integer.parseInt(session.getAttribute("id").toString()),
            threadId, request.getParameter("post_post").replace("\n", "<br>"))
        : -1;
    try {
      if (postId != -1) {
        Pagination pag = new Pagination();
        pag.setElementsPerPage(25);
        pag.setTotalElements((new PostDB()).getTotalPosts(threadId));
        response.sendRedirect("/Forum/thread.jsp?tid=" + threadId + "&p="
            + pag.getTotalPage() + "#post" + postId);
      } else {
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println(
            "window.alert(\"There was an error while yout post was being creating.\")");
        out.println("</script>");
        response.sendRedirect("/Forum/index.jsp");
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
