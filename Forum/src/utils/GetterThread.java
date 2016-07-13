
package utils;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import database.ThreadDB;

public class GetterThread {
  private ThreadDB db;
  private ArrayList<String[]> threads;

  public GetterThread() {
    db = new ThreadDB();
  }
  
  private int setTotalThreads(int forumId){
    return db.getTotalThreads(forumId);
  }
  
  public int getTotalThreads(int forumId){
    return setTotalThreads(forumId);
  }

  private void setThreads(int forumId, int index, int totalElements) {
    threads = db.getThreadList(forumId, index, totalElements);
  }

  public String getThreadsWeb(int forumId, int index, int totalElements, HttpSession session) {
    setThreads(forumId, index, totalElements);
    StringBuilder sb = new StringBuilder();
    if (threads == null || threads.size() == 0) {
      sb.append(
          "<div class=\"div_thread\"><span>There are not threads.</span></div>");
    } else {
      String edit;
      ArrayList<Integer> moderators = (new GetterUser())
          .getListModUserIds(forumId);
      for (String[] thread : threads) {
        if (session != null && session.getAttribute("id") != null
            && session.getAttribute("id").toString().matches("^\\d+$")
            && (Integer
                .parseInt(session.getAttribute("id").toString()) == Integer
                    .parseInt(thread[2])
                || Integer.parseInt(session.getAttribute("id").toString()) == 1
                || moderators
                    .contains(session.getAttribute("id").toString()))) {
          edit = "<div class=\"div_edit\"><a href=\"ne-thread.jsp?tid="
              + thread[0] + "\">Edit</a></div>";
        } else {
          edit = "";
        }
        sb.append("<div class=\"div_thread\">" + edit
            + "<span class=\"span_thread_name\">" + "<a href=\"thread.jsp?tid="
            + thread[0] + "\">" + thread[1] + "</a></span><br>"
            + "<span class=\"span_author\">Author: <a href=\"user.jsp?uid="
            + thread[2] + "\">" + thread[3] + "</span></div>");
      }
    }
    return sb.toString();
  }

  /**
   * Get a thread
   * 
   * @param threadId
   * @param session
   * @return String[3] => name[0] forum_id[1] author[2]
   */
  public String[] getThread(int threadId) {
    return db.getThread(threadId);
  }

  private void set30Threads() {
    threads = db.get30Threads();
  }

  public String get30Threads() {
    StringBuilder sb = new StringBuilder();
    set30Threads(); // 6 => thread_id(0), thread_name(1), forum_id(2),
                    // forum_name(3), user_id(4), user_name(5)
    if (threads.size() == 0) {
      return sb
          .append(
              "<div class=\"div_thread_recent\">Threre are not threads.</div>")
          .toString();
    } else {

    }
    for (String[] thread : threads) {
      sb.append("<div class=\"div_thread_recent\">"
          + "<div class=\"div_thread_recent_inner\">"
          + "<a href=\"forum.jsp?fid=" + thread[2] + "\">" + thread[3]
          + ":</a></div><div class=\"div_thread_recent_inner\">"
          + "<a href=\"thread.jsp?tid=" + thread[0] + "\">" + thread[1]
          + "</a><div></div>");
    }
    return sb.toString();
  }
}
