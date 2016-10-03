
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import database.PostDB;
import database.ThreadDB;
import main.data.Post;

public class GetterPost {
  private PostDB db;
  private ArrayList<Post> list;

  public GetterPost() {
    db = new PostDB();
  }
  
  private int setTotal(int threadId){
    return db.getTotalPosts(threadId);
  }
  
  public int getTotal(int threadId){
    return setTotal(threadId);
  }

  private void setPosts(int threadId, int index, int elements) {
    list = db.getPosts(threadId, index, elements);
  }

  /**
   * Return an String array first element for title and second for post
   * 
   * @param threadId
   * @param index
   * @param elements
   * @param session
   * @return
   */
  public String[] getPostsWeb(int threadId, int index, int elements, HttpSession session) {
    setPosts(threadId, index, elements);
    String[] content = new String[2];
    int userId = session != null && session.getAttribute("id") != null
        && session.getAttribute("id").toString().matches("^\\d+$")
            ? Integer.parseInt(session.getAttribute("id").toString()) : -1;
    StringBuilder sb = new StringBuilder();
    int forumId = (new GetterForum()).getForumId(threadId);
    for (Post post : list) {
      sb.append("<div class=\"div_post_outer\">");
      String imgPath = post.getProfilePicture().replace("\\", "/");
      // writing user info
      sb.append("<div class=\"div_user_info\"><span class=\"span_user_name\">"
          + "<a href=\"user.jsp?uid=" + post.getUserId() + "\">" + post.getUserName()
          + "</a></span><br><img src=\"" + imgPath
          + "\" class=\"img_profile_picture\"></div>");
      // writing post content
      sb.append("<div class=\"div_post_content\"><div class=\"div_post_date\">"
          + "<span>Created on: " + post.getCreationDate() + "</span></div><div class=\"div_post\">"
          + "<span class=\"span_post\" id=\"post"+post.getPostId()+"\">" + post.getPost() + "</span></div>"
          + "<div class=\"div_modification_date\">"
          + (post.getModificationDate() != null ? "Modified on: " + post.getModificationDate() : "") + "</div></div>");
      // adding edit anchor
      ArrayList<Integer> moderators = (new GetterUser())
          .getListModUserIds(forumId);
      if (userId == post.getUserId()
          || (session != null && session.getAttribute("id") != null
              && session.getAttribute("id").toString().matches("^\\d+$")
              && Integer.parseInt(session.getAttribute("id").toString()) == 1)
          || (session != null && session.getAttribute("id") != null
              && session.getAttribute("id").toString().matches("^\\d+$")
              && moderators.contains(
                  Integer.parseInt(session.getAttribute("id").toString())))) {
        sb.append(
            "<div class=\"div_post_edit\"><span><a href=\"ne-post.jsp?p="+index+"&tid="
                + threadId + "&fid="+forumId+"&pid=" + post.getPostId()
                + "\">Edit post</a></span></div>");
      }
      sb.append("</div>");
    }
    content[0] = (new ThreadDB()).getThreadTitle(threadId);
    content[1] = sb.toString();
    return content;
  }

  /**
   * 
   * @param postId
   * @return String[2] => post(0), user_id(1)
   */
  public Post getPost(int postId) {
    return db.getPost(postId);
  }
}
