package servlet.post;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.PostDB;

public class DeletePost extends HttpServlet{
  private static final long serialVersionUID = 6379133826116209269L;
  private PostDB db;
  
  public void init(){
    db = new PostDB();
  }
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    boolean administrator = Integer
        .parseInt(request.getAttribute("id").toString()) == 0 ? true : false;
    if (administrator && db.deletePost(Integer.parseInt(request.getParameter("id")))) {
      response.setHeader("Delete_Post", "ok");
    } else {
      response.setHeader("Delete_Post", "error");
    }
    try {
      response.sendRedirect("/Forum/index,jsp");
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }
  }
}
