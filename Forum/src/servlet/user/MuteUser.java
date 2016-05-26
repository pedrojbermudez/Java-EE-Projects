
package servlet.user;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.UserDB;

public class MuteUser extends HttpServlet {
  UserDB db;

  public void init() {
    db = new UserDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
  }
}
