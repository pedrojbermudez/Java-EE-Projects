
package servlet.thread;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.PostDB;
import database.ThreadDB;

public class NewThread extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = -77499143704863215L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    int threadId = session != null && session.getAttribute("id") != null
        ? (new ThreadDB()).newThread(
            Integer.parseInt(request.getParameter("thread_forum_id")),
            Integer.parseInt(session.getAttribute("id").toString()),
            request.getParameter("thread_name"))
        : -1;
    int postId = session != null && session.getAttribute("id") != null ? (new PostDB()).newPost(
        Integer.parseInt(session.getAttribute("id").toString()), threadId,
        request.getParameter("thread_post")) : -1;
    try {
      if(threadId != -1 && postId != -1){
        response.sendRedirect(
            "/Forum/thread.jsp?tid=" + threadId + "#post=" + postId); 
      } else{
        response.sendRedirect(
            "/Forum/index.jsp");
      }
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
