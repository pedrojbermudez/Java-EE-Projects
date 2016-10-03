
package main.data;

public class ThreadF {
  private int threadId;
  private String name;
  private int forumId;
  private String forumName;
  private int userId;
  private String userName;

  public ThreadF(int threadId, int forumId, String name, int userId) {
    this(threadId, forumId, name, userId, "");
  }

  public ThreadF(int threadId, int forumId, String name, int userId,
      String userName) {
    this(threadId, forumId, name, userId, userName, "");
  }
  
  public ThreadF(int threadId, int forumId, String name, int userId,
      String userName, String forumName){
    this.threadId = threadId;
    this.forumId = forumId;
    this.name = name;
    this.userId = userId;
    this.userName = userName;
    this.forumName = forumName;
  }

  public int getThreadId() {
    return this.threadId;
  }

  public int getForumId() {
    return this.forumId;
  }

  public String getName() {
    return this.name;
  }

  public int getUserId() {
    return this.userId;
  }

  public String getUserName() {
    return this.userName;
  }
  
  public String getForumName() {
    return this.forumName;
  }
}
