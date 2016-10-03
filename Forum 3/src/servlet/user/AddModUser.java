
package servlet.user;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.UserDB;

public class AddModUser extends HttpServlet {

  /**
   * 
   */
  private static final long serialVersionUID = -1937931775039459787L;

  public void doPost(HttpServletRequest request,
      HttpServletResponse response) {
    String[] modUsers = request.getParameterValues("mod_user");
    UserDB db = new UserDB();
    for(String modUser : modUsers){
       db.newModUser(Integer.parseInt(modUser));
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
