
package database;

import java.io.File;


public final class Constant {

  protected static final String USER_TABLE = "User";
  protected static final String MODERATOR_TABLE = "Moderator";
  protected static final String FORUM_TABLE = "Forum";
  protected static final String THREAD_TABLE = "Thread";
  protected static final String POST_TABLE = "Post";
  protected static final String MESSAGE_TABLE = "Message";
  protected static final String BLOCKED_USER_TABLE = "Blocked_User";
  protected static final String FRIEND_TABLE = "Friend";
  protected static final String GROUP_TABLE = "Groups";
  
  
  public static final String SAVE_IMAGE_DIR = "images";
  public static final String PROFILE_PICTURE_DEFAULT = SAVE_IMAGE_DIR
      + File.separator + "default.jpg";
  protected static final String ADMIN_EMAIL = "pedrojavierbermudezaraguez@live.com";
  // public static final String ADMIN_EMAIL = "admin@forum.com";

  // Forum table fields
  protected static String FORUM_FORUM_ID_FIELD_NAME = "forum_id";
  protected static String FORUM_DESCRIPTION_FIELD_NAME = "description";
  protected static String FORUM_NAME_FIELD_NAME = "name";
  protected static String FORUM_CATEGORY_ID_FIELD_NAME = "category_id";

  // Moderator table fields
  protected static String MODERATOR_MOD_ID_FIELD_NAME = "mod_id";
  protected static String MODERATOR_USER_ID_FIELD_NAME = "user_id";
  protected static String MODERATOR_FORUM_ID_FIELD_NAME = "forum_id";

  // Message table fields
  protected static String MESSAGE_MESSAGE_ID_FIELD_NAME = "message_id";
  protected static String MESSAGE_MESSAGE_FIELD_NAME = "message";
  protected static String MESSAGE_SENDER_FIELD_NAME = "sender";
  protected static String MESSAGE_RECEIVER_FIELD_NAME = "receiver";
  protected static String MESSAGE_CREATION_DATE_FIELD_NAME = "creation_date";
  protected static String MESSAGE_GROUP_ID_FIELD_NAME = "group_id";

  // Thread table fields
  protected static String THREAD_THREAD_ID_FIELD_NAME = "thread_id";
  protected static String THREAD_FORUM_ID_FIELD_NAME = "forum_id";
  protected static String THREAD_NAME_FIELD_NAME = "name";
  protected static String THREAD_USER_ID_FIELD_NAME = "user_id";

  // Post table fields
  protected static String POST_POST_ID_FIELD_NAME = "post_id";
  protected static String POST_USER_ID_FIELD_NAME = "user_id";
  protected static String POST_THREAD_ID_FIELD_NAME = "thread_id";
  protected static String POST_POST_FIELD_NAME = "post";
  protected static String POST_CREATION_DATE_FIELD_NAME = "creation_date";
  protected static String POST_MODIFICATION_DATE_FIELD_NAME = "modification_date";
  protected static String POST_DELETED_FIELD_NAME = "deleted";

  // User table fields
  protected static String USER_USER_ID_FIELD_NAME = "user_id";
  protected static String USER_NAME_FIELD_NAME = "name";
  protected static String USER_SURNAME_NAME_FIELD_NAME = "surname";
  protected static String USER_EMAIL_FIELD_NAME = "email";
  protected static String USER_COUNTRY_FIELD_NAME = "country";
  protected static String USER_STATE_FIELD_NAME = "state";
  protected static String USER_CITY_FIELD_NAME = "city";
  protected static String USER_USER_NAME_FIELD_NAME = "user_name";
  protected static String USER_PASSWORD_FIELD_NAME = "password";
  protected static String USER_PROFILE_PICTURE_FIELD_NAME = "profile_picture";
  protected static String USER_IS_MOD_FIELD_NAME = "is_mod";
  protected static String USER_DELETED_FIELD_NAME = "deleted";
  
  // Blocked table fields
  protected static String BLOCKED_BLOCK_ID_FIELD_NAME = "block_id";
  protected static String BLOCKED_USER_ID_FIELD_NAME = "user_id";
  protected static String BLOCKED_BLOCKED_USER_ID_FIELD_NAME = "blocked_user_id";
  
  // Friend table fields
  protected static String FRIEND_FRIEND_ID_FIELD_NAME = "friend_id";
  protected static String FRIEND_USER_ID_FIELD_NAME = "user_id";
  protected static String FRIEND_FRIEND_USER_ID_FIELD_NAME = "friend_user_id";
  
  // Group table fields
  protected static String GROUP_GROUP_ID_FIELD_NAME = "group_id";
  protected static String GROUP_NAME_FIELD_NAME = "name";
  protected static String GROUP_FRIENDS_ONLY_FIELD_NAME = "friends_only";
  
 
  private Constant() {
    throw new AssertionError();
  }
}
