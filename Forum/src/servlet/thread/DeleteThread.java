
package servlet.thread;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ThreadDB;

public class DeleteThread extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 5118829847141368861L;
  private ThreadDB db;

  public void init() {
    db = new ThreadDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    if (db.deleteThread(Integer.parseInt(request.getParameter("id")))) {
      response.setHeader("Delete_Thread", "ok");
    } else {
      response.setHeader("Delete_Thread", "error");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
