
package servlet.message;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.MessageDB;

public class DeleteMessage extends HttpServlet {
  /**
   * 
   */
  private static final long serialVersionUID = -4983840981441393425L;
  private MessageDB db;

  public void init() {
    db = new MessageDB();
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    if(db.deleteMessage(Integer.parseInt(request.getParameter("message_id")))){
      response.setHeader("Delete_Message", "ok");
    } else{
      response.setHeader("Delete_Message", "error");
    }
    // TODO mirar por internet para obtener la url actual.
    try {
      response.sendRedirect("index.jsp");
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
  }
}
