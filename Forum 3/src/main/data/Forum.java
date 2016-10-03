
package main.data;

public class Forum {
  private String name;
  private String description;
  private int forumId;
  private int categoryId;

  /**
   * Constructor for a new category
   * 
   * @param name
   * @param description
   * @param forumId
   */
  public Forum(String name, String description, int forumId) {
    this(name, description, forumId, -1);
  }
  
  /**
   * Constructor for a new forum
   * @param name
   * @param description
   * @param forumId
   * @param categoryId
   */
  public Forum(String name, String description, int forumId, int categoryId) {
    this.name = name;
    this.description = description;
    this.forumId = forumId;
    this.categoryId = categoryId;
  }

  /**
   * Return name
   * @return
   */
  public String getName() {
    return this.name;
  }

  /**
   * Return description
   * @return
   */
  public String getDescription() {
    return this.description;
  }

  /**
   * Return forum id
   * @return
   */
  public int getForumId() {
    return this.forumId;
  }

  /**
   * Return category id
   * @return
   */
  public int getCategoryId() {
    return this.categoryId;
  }

  public String toString() {
    return "name => " + this.name + " | description => " + this.description
        + " | forum id => " + this.forumId + " | category id => "
        + this.categoryId;
  }

}
