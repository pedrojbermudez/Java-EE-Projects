package servlet.user;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import database.UserDB;

public class BlockUser {
private UserDB db;

public void init(){
  db = new UserDB();
}
  
  public void doPost(HttpServletRequest request, HttpServletResponse response) {
    
  }
}
