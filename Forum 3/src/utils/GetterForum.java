
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import database.ForumDB;
import database.UserDB;
import main.data.Forum;
import main.data.User;

public class GetterForum {
  private StringBuilder sbForums;

  /**
   * Return a div container with all forums and its moderators.
   * 
   * @param categoryId
   * @return String with the entire list of forum.
   */

  private String setForums(int categoryId, HttpSession session,
      HttpServletRequest request) {
    sbForums = new StringBuilder();
    sbForums.append("<div id=\"div_forum_list\">");
    ForumDB db = new ForumDB();
    ArrayList<Forum> dbForums = db.getForums(categoryId);
    if (dbForums.size() >= 1) {
      for (Forum forum : dbForums) {
        ArrayList<User> dbModUsers = (new UserDB())
            .getModUsers(forum.getForumId());
        int totalModUsers = dbModUsers.size();
        String edit = session != null && session.getAttribute("id") != null
            && session.getAttribute("id").toString().matches("^\\d+$")
            && Integer.parseInt(session.getAttribute("id").toString()) == 1
                ? "<span class=\"span_edit\"><a href=\"ne-forum.jsp?cid="
                    + categoryId + "&fid=" + forum.getForumId()
                    + "\">Edit</a></span>"
                : "";
        sbForums.append(
            "<div><span class=\"span_forum_name\"><a href=\"forum.jsp?cid="
                + categoryId + "&fid=" + forum.getForumId() + "\">"
                + forum.getName()
                + "</a></span><br><span class=\"span_forum_name\">"
                + forum.getDescription() + "</span>" + edit + "<br>");
        int i = 0;
        for (User modUser : dbModUsers) {
          String sign;
          String mods = "<span>Moderators: </span>";
          if (i == totalModUsers - 1) {
            sign = ".";
          } else {
            sign = ", ";
          }
          sbForums.append(
              mods + "<span class=\"span_mod_list\"><a class=\"anchor_mod_list\""
                  + " href=\"user.jsp?uid=" + modUser.getUserId() + "\">" + modUser.getUserName()
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
  public String getForums(int categoryId, HttpSession session,
      HttpServletRequest request) {
    setForums(categoryId, session, request);
    return sbForums.toString();
  }

  /**
   * 
   * @param forumId
   * @return String[4] => id(0), name(1), description(2), forum_id(3)
   */
  public Forum getForum(int forumId) {
    return setForum(forumId);
  }

  private Forum setForum(int forumId) {
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
    ArrayList<Forum> list = (new ForumDB()).getForums(); // id(0) name (1)
    sbForums.append("<select name=\"" + selectName + "\">");
    for (Forum forum : list) {
      if (forumId == forum.getForumId()) {
        sbForums.append("<option selected value=\"" + forum.getForumId() + "\">"
            + forum.getName() + "</option>");
      } else if (forum.getCategoryId() == -1) {
        sbForums.append("<option disabled>----" + forum.getName() + "----</option>");
      } else {
        sbForums.append(
            "<option value=\"" + forum.getForumId() + "\">" + forum.getName() + "</option>");
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