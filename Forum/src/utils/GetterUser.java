
package utils;

import java.util.ArrayList;

import database.UserDB;

public class GetterUser {

  private StringBuilder sbUser;

  public GetterUser() {
  }

  private void setModUsers(int forumId) {
    sbUser = new StringBuilder();
    UserDB userDB = new UserDB();
    ArrayList<String[]> users = userDB.getModUsers();
    sbUser.append("<select name=\"forum_mod_users\" multiple required>");
    if (users.size() == 0) {
      sbUser.append("<option>No moderator users</option>");
    }
    for (String[] user : users) {
      if (Integer.parseInt(user[0]) == forumId) {
        sbUser.append("<option value=\"" + user[0] + "\" selected>" + user[1]
            + "</option>");
      } else {
        sbUser.append(
            "<option value=\"" + user[0] + "\">" + user[1] + "</option>");
      }
    }
    sbUser.append("</select>");
  }

  /**
   * Return a list with moderator users the format is a select for a form.
   * 
   * @return
   */
  public String getModUsers(int forumId) {
    setModUsers(forumId);
    return sbUser.toString();
  }

  /**
   * 
   * @param forumId
   * @return id(0), user_id(1), user_name(2)
   */
  public ArrayList<String[]> getListModUsers(int forumId) {
    return setListModUsers(forumId);
  }

  private ArrayList<String[]> setListModUsers(int forumId) {
    return (new UserDB()).getModUsers(forumId);
  }

  /**
   * 
   * @param userId
   * @return name(0), surname(1), user_name(2), profile_image(3), country(4),
   *         state(5), city(6)
   */
  public String[] getUser(int userId) {
    return (new UserDB()).getUser(userId);
  }

  /**
   * Get all user for using it on moderator list user
   * @param numberPage
   * @return
   */
  public String getUserList(int numberPage) {
    setUserList(numberPage);
    return sbUser.toString();
  }

  private void setUserList(int numberPage) {
    sbUser = new StringBuilder();
    ArrayList<String[]> listUser = (new UserDB()).getUsers();
    String checkboxName = "mod_user";
    sbUser.append("<form action=\"\" method=\"POST\">");
    for (String[] user : listUser) {
      if (Integer.parseInt(user[2]) > 0) {
        sbUser.append("<div class=\"div_user\"><a href=\"user.jsp?uid="
            + user[0] + "\">" + user[1] + " </a><input type=\"checkbox\""
            + " checked value=\""+user[0]+"\" name=\"" + checkboxName 
            + "\" onchange=\"setModUser(this)\"></div>");
      } else {
        sbUser.append("<div class=\"div_user\"><a href=\"user.jsp?uid="
            + user[0] + "\">" + user[1] + " </a><input type=\"checkbox\""
            + " value=\""+user[0]+"\" name=\"" + checkboxName 
            + "\" onchange=\"setModUser(this)\"></div>");
      }
    }
    sbUser.append("</form>");
  }
}
