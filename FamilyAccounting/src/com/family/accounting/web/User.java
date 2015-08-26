package com.family.accounting.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.family.accounting.libraries.Database;
import com.family.accounting.libraries.Utils;

public class User extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private Database db;
  private Utils utils;
  private HttpSession session;

  public void init() {
    utils = new Utils();
    db = new Database(utils.getDatabasePath());
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    session = request.getSession(true);
    response.setContentType("text/html");
    String name = request.getParameter("name");
    String surname = request.getParameter("surname");
    String email = request.getParameter("email");
    String password = request.getParameter("password");
    String userName = request.getParameter("userName");
    String userId = session.getAttribute("userId") != null
        ? session.getAttribute("userId").toString() : "-1";
    int mode = Integer.parseInt(request.getParameter("mode"));
    switch (mode) {
      case 0: // create user
        db.newUser(name, surname, email, userName, password);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "sources");
        break;
      case 1: // edit user
        db.editUser(Integer.parseInt(userId), userName, surname, email);
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "sources");
        break;
      case 2: // delete user
        db.deleteUser(Integer.parseInt(userId));
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "sources");
        break;
      case 3: // login
        String[] user = db.getUser(userName, password);
        if (user != null) {
          session.setAttribute("userId", user[0]);
          session.setAttribute("userName", user[1]);
          response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
          response.setHeader("Location", "sources");
        } else {
          PrintWriter out = response.getWriter();
          out.println("<html><head></head><body><script>window.alert("
              + "\"User or password incorrect\"); history.back()</script></body></html>");
        }
        break;
      case 4: // logout
        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.invalidate();
        response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
        response.setHeader("Location", "sources");
        break;
    }

  }
}
