
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import main.data.Forum;

public class MenuFooter {
  private StringBuilder menu;
  private String footer;

  private void setMenu(HttpSession session) {
    menu = new StringBuilder();
    menu.append("<div id=\"div_menu\"><span class=\"span_menu_header\">"
        + "<a href=\"index.jsp\">Home</a></span>");
    ArrayList<Forum> categories = (new GetterCategory()).getCategoryList();
    if (categories == null || categories.size() < 1) {
      menu.append(
          "<span class=\"span_menu_header\">There is not categories.</span>");
    } else {
      for (Forum category : categories) {
        menu.append("<span class=\"span_menu_header\"><a href=\"forum.jsp?cid="
            + category.getForumId() + "\">" + category.getName() + "</a></span>");
      }
    }
    if (session != null && session.getAttribute("id") != null
        && session.getAttribute("id").toString().matches("^\\d+$")
        && Integer.parseInt(session.getAttribute("id").toString()) == 1) {
      menu.append(setAdmin());
    }
    menu.append(login(session) + "</div>");
  }

  private String login(HttpSession session) {
    StringBuilder sb = new StringBuilder();
    if (session == null || session.getAttribute("user_name") == null) {
      sb.append("<div id=\"div_login\"><form id=\"login\" action =\"login/\" "
          + "method=\"POST\">" + "<span class=\"span_input_login\">"
          + "User: <input type=\"text\" required "
          + "name=\"user_name\" id=\"user_name\"></span>"
          + "<span class=\"span_input_login\">"
          + "Password: <input type=\"password\" id=\"password\" "
          + "required name=\"password\">"
          + "</span><span class=\"span_input_login\">"
          + "<input type=\"submit\""
          + "value=\"Log in\"></span></form>"
          + "<span clas=\"span_anchor_login\">"
          + "<a id=\"anchor_new_user\" href=\"ne-user.jsp\">"
          + "New user</a></span>" + "</div>");
    } else {
      sb.append("<div id=\"div_login\"><span class=\"span_menu_user_name\">"
          + "<a href=\"user.jsp?uid=" + session.getAttribute("id") + "\">"
          + session.getAttribute("user_name") + "</a></span>"
          + "<span class=\"span_menu_user_name\"><a href=\"login/\">Logout</a></span></div>");
    }
    return sb.toString();
  }

  private String setAdmin() {
    return "<div id=\"div_menu_cat\"><span class=\"span_menu_cat\">"
        + "<a id=\"anchor_menu_cat\" href=\"category.jsp\">"
        + "Category Management</a></span><span class=\"span_menu_cat\">"
        + "<a id=\"anchor_menu_cat\" href=\"mod-user.jsp\">"
        + "Moderators User List</a></span><span class=\"span_menu_cat\">"
        + "<a id=\"anchor_menu_cat\" href=\"user-list.jsp\">User list"
        + "</a></span></div>";
  }

  public String getMenu(HttpSession session) {
    setMenu(session);
    return menu.toString();
  }

  private void setFooter() {
    footer = "<div id=\"div_footer\">Created by Pedro Bermudez<span></div>";
  }

  public String getFooter() {
    setFooter();
    return footer;
  }
}
