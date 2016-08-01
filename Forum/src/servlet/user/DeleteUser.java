
package servlet.user;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.UserDB;

public class DeleteUser extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 3427451394157715956L;
  private UserDB db;

  public void init() {
    db = new UserDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (db.deleteUser(Integer.parseInt(request.getParameter("id")),
        request.getParameter("deleted").equals("y") ? true : false)) {
      // Delete user was succeed
      response.setHeader("Delete_User", "ok");
    } else {
      response.setHeader("Delete_User", "error");
    }
    response.sendRedirect("index.jsp");
  }
}
