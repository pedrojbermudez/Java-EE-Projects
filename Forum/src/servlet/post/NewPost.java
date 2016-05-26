
package servlet.post;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.PostDB;

public class NewPost extends HttpServlet {
  private static final long serialVersionUID = -4242323963421615995L;
  private PostDB db;

  public void init() {
    db = new PostDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    int postId = session != null && session.getAttribute("id") != null
        ? db.newPost(Integer.parseInt(session.getAttribute("id").toString()),
            Integer.parseInt(request.getParameter("post_thread_id")),
            request.getParameter("post_post"))
        : -1;
    try {
      if(postId != -1){
        response.sendRedirect("/Forum/thread.jsp?tid="
            + Integer.parseInt(request.getParameter("post_thread_id")) + "#post="
            + postId);
      } else {
        response.sendRedirect("/Forum/index.jsp");  
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
