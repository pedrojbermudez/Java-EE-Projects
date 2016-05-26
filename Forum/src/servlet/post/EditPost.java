
package servlet.post;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.PostDB;

public class EditPost extends HttpServlet {
  private static final long serialVersionUID = -7898727993091080544L;
  private PostDB db;

  public void init() {
    db = new PostDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    System.out.println(request.getParameter("post_post"));
    if (db.editPost(Integer.parseInt(request.getParameter("post_id")),
        request.getParameter("post_post"))) {
      response.setHeader("Edit_Post", "ok");
    } else {
      response.setHeader("Edit_Post", "error");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
