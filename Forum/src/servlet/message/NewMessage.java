
package servlet.message;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.MessageDB;

public class NewMessage extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = 5094891059386764910L;
  private MessageDB db;

  public void init() {
    db = new MessageDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    if (db.newMessage(request.getParameter("message"),
        Integer.parseInt(request.getParameter("sender")),
        Integer.parseInt(request.getParameter("receiver")))) {
      response.setHeader("New_Message", "ok");
    } else {
      response.setHeader("New_Message", "error");
    }
    // TODO mirar por internet para obtener la url actual.
    try {
      response.sendRedirect("index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
