
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import database.UserDB;
import main.data.User;

public class GetterUser {

  private StringBuilder sbUser;

  public GetterUser() {
  }

  private int setTotalUser() {
    return (new UserDB()).getTotalUsers();
  }

  public int getTotalUser() {
    return setTotalUser();
  }

  private void setModUsers(int forumId) {
    sbUser = new StringBuilder();
    UserDB userDB = new UserDB();
    ArrayList<User>modUsers = userDB.getModUsers();
    ArrayList<Integer> idModUser = userDB.getModUserIds(forumId);
    sbUser.append("<select name=\"forum_mod_users\" multiple required>");
    if (modUsers.size() == 0) {
      sbUser.append("<option>No moderator users</option>");
    } else {
      for (User user : modUsers) {
        if (forumId != -1 && idModUser.contains(user.getUserId())) {
          sbUser.append("<option value=\"" + user.getUserId() + "\" selected>" + user.getUserName()
              + "</option>");
        } else {
          sbUser.append(
              "<option value=\"" + user.getUserId() + "\">" + user.getUserName() + "</option>");
        }
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
  public ArrayList<User> getListModUsers(int forumId) {
    return setListModUsers(forumId);
  }

  private ArrayList<User> setListModUsers(int forumId) {
    return (new UserDB()).getModUsers(forumId);
  }

  /**
   * 
   * @param forumId
   * @return id(0), user_id(1), user_name(2)
   */
  public ArrayList<Integer> getListModUserIds(int forumId) {
    return setListModUserIds(forumId);
  }

  private ArrayList<Integer> setListModUserIds(int forumId) {
    return (new UserDB()).getModUserIds(forumId);
  }

  /**
   * Return the user chosen as view.
   * 
   * @param userId
   * @param session
   * @return name(0), surname(1), user_name(2), profile_picture(3), country(4),
   *         state(5), city(6)
   */
  public String[] getUser(int userId, HttpSession session) {
    return setUser(userId, session);
  }

  /**
   * See getUser function for more info.
   * 
   * @param userId
   * @param session
   * @return
   */
  private String[] setUser(int userId, HttpSession session) {
    User user = (new UserDB()).getUser(userId);
    StringBuilder userContent = new StringBuilder();
    userContent.append("<div class=\"div_user_content\">"
        + "<span class=\"span_user_name\">User name: " + user.getUserName() + "</span>"
        + "</div><div class=\"div_user_content\">"
        + "<span class=\"span_avatar\">Avatar: </span>" + "<img src=\""
        + user.getProfilePicture() + "\" id=\"img_avatar\"></div>"
        + "<div class=\"div_user_content\"><span class=\"span_name\">"
        + "Name, Surname: " + user.getName() + " " + user.getSurname() + "</span></div>"
        + "<div class=\"div_user_content\">"
        + "<span class=\"span_location\">Location: " + user.getCountry() + ", " + user.getState()
        + ", " + user.getCity() + "</span></div>");
    if (session.getAttribute("id") != null
        && (userId == Integer.parseInt(session.getAttribute("id").toString())
            || Integer.parseInt(session.getAttribute("id").toString()) == 1)) {
      userContent.append("<a href=\"ne-user.jsp?uid=" + userId
          + "\"><button>Edit</button></a>");
    }
    String[] webContent = { userContent.toString(), user.getUserName() };
    return webContent;
  }

  /**
   * The complete form with all user's data.
   * 
   * @param userId
   * @param session
   * @return form(0); title(1)
   */
  public String[] getEditUser(int userId, HttpSession session) {
    return setEditUser(userId, session);
  }

  private String[] setEditUser(int userId, HttpSession session) {
    StringBuilder userContent = new StringBuilder();
    String title;
    if (userId == Integer.parseInt(session.getAttribute("id").toString())
        || Integer.parseInt(session.getAttribute("id").toString()) == 1) {
      // Returning the user
      // name(0), surname(1), user_name(2), profile_image(3), country(4),
      // state(5), city(6)
      User user = (new UserDB()).getUser(userId);
      userContent.append("<form action=\"edit-user/\" method=\"POST\" "
          + "enctype=\"multipart/form-data\">"
          + "<input type=\"hidden\" value=\"" + user.getProfilePicture()
          + "\" id=\"path\" name=\"path\">" + "<input type=\"hidden\" value=\""
          + userId + "\" name=\"user_id\">Name: <input type=\"text\" "
          + "name=\"user_name\" value=\"" + user.getName() + "\">"
          + "<br>Surname: <input type=\"text\" name=\"user_surname\" value=\""
          + user.getSurname() + "\">"
          + "<br>Country: <input type=\"text\" name=\"user_country\" value=\""
          + user.getCountry() + "\">"
          + "<br>State: <input type=\"text\" name=\"user_state\" value=\""
          + user.getState() + "\">"
          + "<br>City: <input type=\"text\" name=\"user_city\" value=\""
          + user.getCity() + "\">"
          + "<br>Current profile pic: <div id=\"div_current_pìc\">"
          + "<img id=\"img_avatar\" src=\"" + user.getProfilePicture() + "\"></div>"
          + "<button type=\"button\" onclick=\"deleteCurrentFile()\">"
          + "Delete current picture</button>"
          + "<br>Profile picture: <input type=\"file\" onchange=\"checkingPath()\" accept=\"images/*\" "
          + "name=\"user_profile_picture\" id=\"picture\">"
          + "<button type=\"button\" onclick=\"deleteUploadFile()\">"
          + "Delete upload picture</button>"
          + "<br><input type=\"submit\" value=\"Edit\"></form>");
      title = user.getUserName();
    } else {
      userContent.append("<div class=\"div_user_content\">"
          + "<span class=\"span_user_content\">You can't be here.</span>"
          + "<div>");
      title = "Wrong place";
    }
    String[] webContent = { userContent.toString(), title };
    return webContent;
  }

  /**
   * Get all user for using it on moderator list user
   * 
   * @param numberPage
   * @return
   */
  public String getSetModUserList(int numberPage, int index,
      int totalElements) {
    setSetModUserList(numberPage, index, totalElements);
    return sbUser.toString();
  }

  private void setSetModUserList(int numberPage, int index, int totalElements) {
    sbUser = new StringBuilder();
    ArrayList<User> listUser = (new UserDB()).getUsers(index,
        totalElements);
    String checkboxName = "mod_user";
    sbUser.append("<form action=\"\" method=\"POST\">");
    for (User user : listUser) {
      if (user.getIsMod() > 0) {
        sbUser.append("<div class=\"div_user\"><a href=\"user.jsp?uid="
            + user.getUserId() + "\">" + user.getUserName() + " </a><input type=\"checkbox\""
            + " checked value=\"" + user.getUserId() + "\" name=\"" + checkboxName
            + "\" onchange=\"setModUser(this)\"></div>");
      } else {
        sbUser.append("<div class=\"div_user\"><a href=\"user.jsp?uid="
            + user.getUserId() + "\">" + user.getUserName() + " </a><input type=\"checkbox\""
            + " value=\"" + user.getUserId() + "\" name=\"" + checkboxName
            + "\" onchange=\"setModUser(this)\"></div>");
      }
    }
    sbUser.append("</form>");
  }

  /**
   * Get all user for using it on moderator list user
   * 
   * @param numberPage
   * @return
   */
  public String getSetDeletedUserList(int numberPage, int index,
      int totalElements) {
    setSetDeletedUserList(numberPage, index, totalElements);
    return sbUser.toString();
  }

  private void setSetDeletedUserList(int numberPage, int index,
      int totalElements) {
    sbUser = new StringBuilder();
    ArrayList<User> listUser = (new UserDB()).getUsers(index,
        totalElements);
    String checkboxName = "deleted";
    sbUser.append("<form action=\"\" method=\"POST\">");
    for (User user : listUser) {
      if (user.getDeleted() > 0) {
        sbUser.append("<div class=\"div_user\"><a href=\"user.jsp?uid="
            + user.getUserId() + "\">" + user.getUserName() + " </a><input type=\"checkbox\""
            + " checked value=\"" + user.getUserId() + "\" name=\"" + checkboxName
            + "\" onchange=\"setDeletedUser(this)\"></div>");
      } else {
        sbUser.append("<div class=\"div_user\"><a href=\"user.jsp?uid="
            + user.getUserId() + "\">" + user.getUserName() + " </a><input type=\"checkbox\""
            + " value=\"" + user.getUserId() + "\" name=\"" + checkboxName
            + "\" onchange=\"setDeletedUser(this)\"></div>");
      }
    }
    sbUser.append("</form>");
  }

}
