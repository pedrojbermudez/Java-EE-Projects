
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import database.PostDB;

public class GetterPost {
  private PostDB db;
  private ArrayList<String[]> list;

  public GetterPost() {
    db = new PostDB();
  }

  private void setPosts(int threadId) {
    list = db.getPosts(threadId);
  }

  public String getPostsWeb(int threadId, HttpSession session) {
    setPosts(threadId);
    int userId = session != null && session.getAttribute("id") != null
        && session.getAttribute("id").toString().matches("^\\d+$")
            ? Integer.parseInt(session.getAttribute("id").toString()) : -1;
    StringBuilder sb = new StringBuilder();
    int forumId = (new GetterForum()).getForumId(threadId);
    for (String[] post : list) {
      String imgPath = post[6].replace("\\", "/");
      sb.append("<div class=\"div_outer\"><div class=\"div_user_info\">"
          + "<span class=\"span_user_name\"><a href=\"user.jsp?uid=" + post[1]
          + "\">" + post[5] + "</a></span><br><img src=\"" + imgPath
          + "\" class=\"img_profile_picture\"></div>"
          + "<div class=\"div_post_content\"><div class=\"div_post_date\">"
          + post[3] + "</div><div class=\"div_post\">"
          + "<span class=\"span_post\">" + post[2] + "</span></div>"
          + "</div><div class=\"div_modification_date\">"
          + (post[4] != null ? post[4] : "") + "</div></div>");
      ArrayList<String[]> moderators = (new GetterUser())
          .getListModUsers(forumId);
      if (userId == Integer.parseInt(post[1])
          || (session != null && session.getAttribute("id") != null
              && session.getAttribute("id").toString().matches("^\\d+$")
              && Integer.parseInt(session.getAttribute("id").toString()) == 1)
          || (session != null && session.getAttribute("id") != null
              && session.getAttribute("id").toString().matches("^\\d+$")
              && moderators.contains(
                  Integer.parseInt(session.getAttribute("id").toString())))) {
        sb.append(
            "<div class=\"div_post_edit\"><span><a href=\"ne-post.jsp?tid="
                + threadId + "&pid=" + post[0]
                + "\">Edit post</a></span></div>");
      }
    }
    return sb.toString();
  }

  /**
   * 
   * @param postId
   * @return String[2] => post(0), user_id(1)
   */
  public String[] getPost(int postId) {
    return db.getPost(postId);
  }
}
