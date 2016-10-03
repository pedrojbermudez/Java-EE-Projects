
package main.data;

public class User {
  private int userId;
  private String userName;
  private String name;
  private String surname;
  private String country;
  private String state;
  private String city;
  private String profilePicture;
  private int isMod;
  private int deleted;

  /**
   * Constructor to create a new moderator user
   * 
   * @param userId
   * @param userName
   */
  public User(int userId, String userName, int deleted) {
    this(userId, userName, "", "", "", "", "","", 1,deleted);
  }
  
  public User(int userId, String userName, String profilePicture, int isMod, int deleted){
    this(userId, userName, "", "", "", "", "", profilePicture, isMod, deleted);
  } 

  public User(int userId, String userName, int isMod, int deleted) {
    this(userId, userName, "", "", "", "", "", "", isMod, deleted);
  }

  public User(int userId, String userName, String name, String surname,
      String country, String state, String city, String profilePicture,
      int isMod, int deleted) {
    this.userId = userId;
    this.userName = userName;
    this.name = name;
    this.surname = surname;
    this.country = country;
    this.state = state;
    this.city = city;
    this.profilePicture = profilePicture;
    this.isMod = isMod;
    this.deleted = deleted;

  }

  public int getUserId() {
    return this.userId;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getName() {
    return this.name;
  }

  public String getSurname() {
    return this.surname;
  }

  public String getCountry() {
    return this.country;
  }

  public String getState() {
    return this.state;
  }

  public String getCity() {
    return this.city;
  }

  public String getProfilePicture() {
    return this.profilePicture;
  }

  public int getIsMod() {
    return this.isMod;
  }

  public int getDeleted() {
    return this.deleted;
  }
}
