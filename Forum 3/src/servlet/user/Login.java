
package servlet.user;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import database.UserDB;
import main.data.User;

public class Login extends HttpServlet {
  private static final long serialVersionUID = 1L;
  private static HttpSession session;
  private UserDB db;

  public void init() {
    db = new UserDB();
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response) {
    // Removing session and login
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
    // Creating session and login
    Map<Boolean, User> user = db.login(request.getParameter("user_name"),
        request.getParameter("password"));
    if (user.isEmpty()) {
      System.out.println("I'm null");
    }
    // Getting session
    session = request.getSession(true);
    if (session.getAttribute("error") != null) {
      session.removeAttribute("error");
    }
    if (user != null && user.containsKey(true)
        && user.get(true).getDeleted() == 0) {
      User getUser = user.get(true);
      session.setAttribute("id", getUser.getUserId());
      session.setAttribute("user_name", getUser.getUserName());
      session.setAttribute("profile_picture", getUser.getProfilePicture());
    } else if (user != null && user.containsKey(true)
        && user.get(true).getDeleted() != 0)
      session.setAttribute("deleted", "Your user was deleted");
    else session.setAttribute("error", "user name or password incorrect");
    try {
      response.sendRedirect("/Forum/index.jsp");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
