
package servlet.forum;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ForumDB;
import database.PostDB;
import database.ThreadDB;

public class DeleteForum extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = -5190875662006861871L;
  private ForumDB forumDb;
  private ThreadDB threadDb;
  private PostDB postDb;

  public void init() {
    forumDb = new ForumDB();
    threadDb = new ThreadDB();
    postDb = new PostDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    if (forumDb
        .deleteForum(Integer.parseInt(request.getParameter("forum_id")))) {
      response.setHeader("Delete_Forum", "ok");
    } else {
      response.setHeader("Delete_Forum", "error");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
