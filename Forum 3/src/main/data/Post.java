
package main.data;

public class Post {
  private int postId;
  private int userId;
  private int threadId;
  private String post;
  private String creationDate;
  private String modificationDate;
  private String userName;
  private String profilePicture;

  public Post(int postId, int userId, int threadId, String post){
    this(postId, userId, threadId, post, "", "", "", "");
  }
  
  public Post(int postId, int userId, int threadId, String post,
      String creationDate, String modificationDate, 
      String userName, String profilePicture) {
    this.postId = postId;
    this.userId = userId;
    this.threadId = threadId;
    this.post = post;
    this.creationDate = creationDate;
    this.modificationDate = modificationDate;
    this.userName = userName;
    this.profilePicture = profilePicture;

  }

  public int getPostId() {
    return this.postId;
  }

  public int getThreadId() {
    return this.threadId;
  }

  public int getUserId() {
    return this.userId;
  }

  public String getPost() {
    return this.post;
  }

  public String getCreationDate() {
    return this.creationDate;
  }

  public String getModificationDate() {
    return this.modificationDate;
  }

  public String getUserName() {
    return this.userName;
  }
  
  public String getProfilePicture() {
    return this.profilePicture;
  }
}
