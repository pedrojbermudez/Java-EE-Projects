
package constants;

import java.io.File;

public final class Constant {

  public static final String USER_TABLE = "User";
  public static final String MODERATOR_TABLE = "Moderator";
  public static final String FORUM_TABLE = "Forum";
  public static final String THREAD_TABLE = "Thread";
  public static final String POST_TABLE = "Post";
  public static final String MESSAGE_TABLE = "Message";
  public static final String BLOCKED_USER_TABLE = "Blocked_User";
  public static final String SAVE_IMAGE_DIR = "images";
  public static final String PROFILE_PICTURE_DEFAULT = SAVE_IMAGE_DIR
      + File.separator + "default.jpg";
  public static final String ADMIN_EMAIL = "pedrojavierbermudezaraguez@live.com";
  // public static final String ADMIN_EMAIL = "admin@forum.com";

  private Constant() {
    throw new AssertionError();
  }
}
