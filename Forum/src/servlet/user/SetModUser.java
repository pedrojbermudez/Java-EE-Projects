
package servlet.user;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.UserDB;

/**
 * Servlet implementation class SetModUser
 */
@WebServlet("/SetModUser")
public class SetModUser extends HttpServlet {
  private static final long serialVersionUID = 1L;

  /**
   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
   *      response)
   */
  protected void doPost(HttpServletRequest request,
      HttpServletResponse response) {
    if (request.getSession() != null && Integer
        .parseInt(request.getSession().getAttribute("id").toString()) == 1) {
      // User logged was administrator
      // Changing normal user to moderator user
      (new UserDB()).setModUserList(
          Integer.parseInt(request.getParameter("user_id")),
          request.getParameter("is_mod").equals("y") ? true : false);
    }
  }

}
