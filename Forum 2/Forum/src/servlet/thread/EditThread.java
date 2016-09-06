
package servlet.thread;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ThreadDB;

public class EditThread extends HttpServlet {
  private static final long serialVersionUID = 8793172496096879196L;
  private ThreadDB db;

  public void init() {
    db = new ThreadDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    System.out.println("forum_id => " + request.getParameter("forum_id"));
    if (db.editThread(Integer.parseInt(request.getParameter("thread_id")),
        Integer.parseInt(request.getParameter("forum_id")),
        request.getParameter("thread_name"))) {
      response.setHeader("Edit_Thread", "ok");
    } else {
      response.setHeader("Edit_Thread", "error");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
