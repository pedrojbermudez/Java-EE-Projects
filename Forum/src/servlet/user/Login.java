
package servlet.user;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.UserDB;

public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static HttpSession session;
  private UserDB db;

  public void init() {
    db = new UserDB();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    session = request.getSession();
    session.removeAttribute("id");
    session.removeAttribute("user_name");
    session.removeAttribute("profile_picture");
    try {
      session.invalidate();
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    Map<Boolean, String[]> user = db.login(
        request.getParameter("user_name"), request.getParameter("password"));
    session = request.getSession(true);
    if(session.getAttribute("error") != null){
      session.removeAttribute("error");
    }
    if (user != null && user.containsKey(true)) {
      String[] getUser = user.get(true);
      session.setAttribute("id", getUser[0]);
      session.setAttribute("user_name", getUser[1]);
      session.setAttribute("profile_picture", getUser[2]);
    } else {
      session.setAttribute("error", "user name or password incorrect");
    }
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
