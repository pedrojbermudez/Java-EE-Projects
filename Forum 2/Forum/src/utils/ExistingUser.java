package utils;

import database.UserDB;

public class ExistingUser {
  
  private boolean checkUser(String user, String email){
    return (new UserDB()).existingUserName(user);
  }
  
  public boolean getCheckUser(String user, String email){
    return checkUser(user, email);
  }
  
}
