
package servlet.user;

import java.io.IOException;
import javax.servlet.ServletException;
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
    System.out.println("valor de user_id " + request.getParameter("user_id"));
    System.out.println("valor de is_mod : " + request.getParameter("is_mod"));
    if (request.getSession() != null && Integer
        .parseInt(request.getSession().getAttribute("id").toString()) == 1) {
      (new UserDB()).setModUserList(
          Integer.parseInt(request.getParameter("user_id")),
          request.getParameter("is_mod").equals("y") ? true : false);
    }
  }

}
