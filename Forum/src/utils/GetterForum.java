
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import database.ForumDB;
import database.UserDB;

public class GetterForum {
  private StringBuilder sbForums;

  /**
   * Return a div container with all forums and its moderators.
   * 
   * @param categoryId
   * @return String with the entire list of forum.
   */

  private String setForums(int categoryId, HttpSession session, HttpServletRequest request) {
    sbForums = new StringBuilder();
    sbForums.append("<div id=\"div_forum_list\">");
    ForumDB db = new ForumDB();
    ArrayList<String[]> dbForums = db.getForums(categoryId);
    if (dbForums.size() >= 1) {
      for (String[] forum : dbForums) {
        ArrayList<String[]> dbModUsers = (new UserDB())
            .getModUsers(Integer.parseInt(forum[0]));
        int totalModUsers = dbModUsers.size();
        String edit = session != null && session.getAttribute("id") != null
            && session.getAttribute("id").toString().matches("^\\d+$")
            && Integer.parseInt(session.getAttribute("id").toString()) == 1
                ? "<span class=\"span_edit\"><a href=\"ne-forum.jsp?cid="
                    + categoryId + "&fid=" + forum[0] + "\">Edit</a></span>"
                : "";
        sbForums.append(
            "<div><span class=\"span_forum_name\"><a href=\"forum.jsp?cid="
                + categoryId + "&fid=" + forum[0] + "\">" + forum[1]
                + "</a></span>" + edit + "<br>");
        int i = 0;
        for (String[] modUser : dbModUsers) {
          String sign;
          String mods = "<span>Moderators: </span>";
          if (i == totalModUsers - 1) {
            sign = ".";
          } else {
            sign = ", ";
          }
          sbForums.append(
              mods + "<span class=\"span_mod_list\"><a class=\"anchor_mod_list\""
                  + " href=\"user.jsp?uid=" + modUser[0] + "\">" + modUser[1]
                  + "</a>" + sign + "</span>");
          i++;
        }
        sbForums.append("</div>");
      }

    } else {
      sbForums.append("There is not some forum");
    }
    return sbForums.append("</div>").toString();
  }

  /**
   * Get a list of forums with a category id given.
   * 
   * @param categoryId
   * @return
   */
  public String getForums(int categoryId, HttpSession session, HttpServletRequest request) {
    setForums(categoryId, session, request);
    return sbForums.toString();
  }

  /**
   * 
   * @param forumId
   * @return String[4] => id(0), name(1), description(2), forum_id(3)
   */
  public String[] getForum(int forumId) {
    return setForum(forumId);
  }

  private String[] setForum(int forumId) {
    return (new ForumDB()).getForum(forumId);
  }

  /**
   * Get all forums as a HTML select tag
   * 
   * @param forumId
   * @return
   */
  public String getForumList(int forumId, String selectName) {
    return setForumList(forumId, selectName);
  }

  private String setForumList(int forumId, String selectName) {
    sbForums = new StringBuilder();
    ArrayList<String[]> list = (new ForumDB()).getForums(); // id(0) name (1)
    sbForums.append("<select name=\"" + selectName + "\">");
    for (String[] forum : list) {
      if (forumId == Integer.parseInt(forum[0])) {
        sbForums.append("<option selected value=\"" + forum[0] + "\">"
            + forum[1] + "</option>");
      } else if (Integer.parseInt(forum[2]) == -1) {
        sbForums.append("<option disabled>----" + forum[1] + "----</option>");
      } else {
        sbForums.append(
            "<option value=\"" + forum[0] + "\">" + forum[1] + "</option>");
      }
    }
    return sbForums.append("</select>").toString();
  }

  public int getForumId(int threadId) {
    return setForumId(threadId);
  }

  public int setForumId(int threadId) {
    return (new ForumDB()).getForumId(threadId);
  }
}