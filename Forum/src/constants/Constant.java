
package constants;

import java.io.File;

public final class Constant {
  // *********************************************************
  // Change this if it is necessary. Mysql Root user, password database and
  // host.
  public static final String ROOT_USER = "root";
  public static final String ROOT_PASSWORD = "toor";
  public static final String DATABASE_NAME = "forum_db";
  public static final String HOST = "localhost";
  public static final String PORT = "3306";
  // *********************************************************

  public static final String USER_TABLE = "User";
  public static final String MODERATOR_TABLE = "Moderator";
  public static final String FORUM_TABLE = "Forum";
  public static final String THREAD_TABLE = "Thread";
  public static final String POST_TABLE = "Post";
  public static final String MESSAGE_TABLE = "Message";
  public static final String BLOCKED_USER_TABLE = "Blocked_User";
  public static final String SAVE_IMAGE_DIR = "images";
  public static final String PROFILE_PICTURE_DEFAULT = File.separator
      + "images" + File.separator + "default_pp.jpg";
  public static final String ADMIN_EMAIL = "pedrojavierbermudezaraguez@live.com";
  // public static final String ADMIN_EMAIL = "admin@forum.com";

  private Constant() {
    throw new AssertionError();
  }
}
