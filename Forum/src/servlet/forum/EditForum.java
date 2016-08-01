
package servlet.forum;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.ForumDB;

public class EditForum extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = -9179074729635554800L;
  private ForumDB db;

  public void init() {
    db = new ForumDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    // Adding moderators
    String[] tmp = request.getParameterValues("forum_mod_users");
    int[] moderators = new int[tmp.length];
    for (int i = 0; i < moderators.length; i++) {
      moderators[i] = Integer.parseInt(tmp[i]);
    }
    if (db.editForum(moderators, request.getParameter("forum_name"),
        request.getParameter("forum_description"),
        Integer.parseInt(request.getParameter("forum_category_id")),
        Integer.parseInt(request.getParameter("forum_id")))) {
      response.setHeader("Edit_Forum", "ok");
    } else {
      response.setHeader("Edit_Forum", "error");
    }
    try {
      response.sendRedirect(
          "/Forum/forum.jsp?cid=" + request.getParameter("forum_category_id")
              + "&fid=" + request.getParameter("forum_forum_id"));
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
