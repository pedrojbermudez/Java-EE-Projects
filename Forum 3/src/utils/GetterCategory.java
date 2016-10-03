
package utils;

import java.util.ArrayList;

import database.ForumDB;
import main.data.Forum;

public class GetterCategory {
  private ForumDB db;
  private ArrayList<Forum> categories;

  public GetterCategory() {
    db = new ForumDB();
  }

  private void setCategory() {
    categories = db.getCategories();
  }

  /**
   * Get categories to use in general
   * 
   * @return ArrayList< String[] > =>  id(0), name(1), description(2)
   */
  public ArrayList<Forum> getCategoryList() {
    setCategory();
    return categories;
  }

  /**
   * 
   * @param categoryId
   * @return String[] => name(0), description(1) 
   */
  public Forum getCategory(int categoryId) {
    return db.getCategory(categoryId);
  }

  /**
   * Return a category to use it in a form
   * 
   * @param selectName
   * @param int
   * @return A HTML select
   */
  public String getCategoryWEBSelect(String selectName, int categoryId) {
    StringBuilder sb = new StringBuilder();
    sb.append("<select name=\"" + selectName + "\" required>");
    ArrayList<Forum> categories = (new ForumDB()).getCategories();
    for (Forum category : categories) {
      if (categoryId == category.getForumId()) {
        sb.append("<option value=\"" + category.getForumId() + "\" selected>"
            + category.getName() + "</option>");
      } else {
        sb.append("<option value=\"" + category.getForumId() + "\">" + category.getName()
            + "</option>");
      }
    }
    return sb.append("</select>").toString();
  }
}
