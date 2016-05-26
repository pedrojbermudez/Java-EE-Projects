
package utils;

import java.util.ArrayList;

import database.ForumDB;

public class GetterCategory {
  private ForumDB db;
  private ArrayList<String[]> categories;

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
  public ArrayList<String[]> getCategoryList() {
    setCategory();
    return categories;
  }

  /**
   * 
   * @param categoryId
   * @return String[] => name(0), description(1) 
   */
  public String[] getCategory(int categoryId) {
    return db.getCategory(categoryId);
  }

  /**
   * Return a category to use it in a form
   * 
   * @param selectName
   * @param int
   * @return A HTML select
   */
  public String getCategoryWEBSelect(String selectName, int id) {
    StringBuilder sb = new StringBuilder();
    setCategory();
    sb.append("<select name=\"" + selectName + "\"");
    for (String[] cat : categories) {
      if (id == Integer.parseInt(cat[0])) {
        sb.append("<option selected value=\"" + cat[0] + "\">" + cat[1]
            + "</option>");
      } else {
        sb.append("<option value=\"" + cat[0] + "\">" + cat[1] + "</option>");
      }
    }
    return sb.append("</select>").toString();
  }

  public String[] getCategoryWEB(int id) {
    return db.getCategory(id);
  }
}
