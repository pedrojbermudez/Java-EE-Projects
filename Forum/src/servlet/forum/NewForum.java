
package servlet.forum;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.ForumDB;

public class NewForum extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 8041493382136314010L;

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    HttpSession session = request.getSession();
    // Getting and saving moderators
    String[] tmp = request.getParameterValues("forum_mod_users");
    int[] moderators = new int[tmp.length];
    for (int i = 0; i < moderators.length; i++) {
      moderators[i] = Integer.parseInt(tmp[i]);
    }
    // Creating a new forum
    int forumId = session != null
        && Integer.parseInt(session.getAttribute("id").toString()) == 1
            ? (new ForumDB()).newForum(moderators,
                request.getParameter("forum_name"),
                request.getParameter("forum_description"),
                Integer.parseInt(request.getParameter("forum_category_id")))
            : -1;
    try {
      // Going to the new forum
      if (forumId != -1) {
        response.sendRedirect("/Forum/forum.jsp?cid="
            + request.getParameter("forum_category_id") + "&fid=" + forumId);
      } else {
        // Going to the category id
        PrintWriter out = response.getWriter();
        out.println("<script type=\"text/javascript\">");
        out.println(
            "window.alert(\"There was an error while new forum was creating.\")");
        out.println("</script>");
        response.sendRedirect("/Forum/forum.jsp?cid="
            + request.getParameter("forum_category_id"));
      }
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
