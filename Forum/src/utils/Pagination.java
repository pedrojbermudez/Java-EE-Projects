
package utils;

public class Pagination {

  private int totalElements;
  private int elementsPerPage;

  public Pagination() {
    this(0, 1);
  }

  public Pagination(int totalElements, int elementsPerPage) {
    this.totalElements = totalElements;
    this.elementsPerPage = elementsPerPage;
  }

  public void setTotalElements(int totalElements) {
    this.totalElements = totalElements;
  }

  public void setElementsPerPage(int elementsPerPage) {
    this.elementsPerPage = elementsPerPage;
  }

  /**
   * 
   * @param cp
   *          Current page
   * @param url
   *          current url
   * @return
   */
  public String getPag(int cp, String url, String queryPart) {
    // Establishing total pages.
    int tp = this.totalElements % this.elementsPerPage == 0
        ? this.totalElements / this.elementsPerPage
        : 1 + (this.totalElements / this.elementsPerPage);
    if (this.totalElements == 0 || this.totalElements <= this.elementsPerPage
        || tp <= 1) {
      // Returning nothing
      return "";
    } else {
      // Create pagination
      StringBuilder pag = new StringBuilder();
      url += "?";
      // Checking for p. if it's at the end true, no at the end false.
      boolean pE = true;
      String[] parts = queryPart == null ? null : queryPart.split("&");
      System.out.println(parts.length);
      if (queryPart != null && queryPart.lastIndexOf("p=") > -1) {
        // Splitting query part by &
        int i = 0;
        while (i < parts.length) {
          // It contains p and not at the end of the array
          if (parts[i].contains("p=") && i < parts.length - 1) {
            url += i > 0 ? "&" + parts[parts.length - 1]
                : parts[parts.length - 1];
            pE = true;

          } else if (parts[i].contains("p=") || pE && i == parts.length - 1) {
            url += "&p=";
          } else {
            url += i > 0 ? "&" + parts[i] : parts[i];
          }
          i++;
        }
      } else if (queryPart == null)
        url += "p=";
      else if (queryPart != null && queryPart.lastIndexOf("p=") == -1)
        url += queryPart + "&p=";
      pag.append("<div id=\"div_pagination\">");
      if (cp == 1) {
        // First page
        pag.append("<span>1</span>");
        if (cp + 1 < tp) {
          // Second page
          pag.append("<span><a href=\"" + url + (cp + 1) + "\">" + (cp + 1)
              + "</a></span>");
        }
        if (cp + 2 < tp) {
          // Third page
          pag.append("<span><a href=\"" + url + (cp + 2) + "\">" + (cp + 2)
              + "</a></span>");
        }
        // Last page
        pag.append(
            "<span><a href=\"" + url + (tp) + "\">" + tp + "</a></span>");
      } else if (cp > 1 && cp < tp) {
        // First page
        pag.append("<span><a href=\"" + url + "1" + "\">1</a></span>");
        if (cp - 2 > 1) {
          // Current page - 2
          pag.append("<span><a href=\"" + url + (cp - 2) + "\">" + (cp - 2)
              + "</a></span>");
        }
        if (cp - 1 > 1) {
          // Previous page
          pag.append("<span><a href=\"" + url + (cp - 1) + "\">" + (cp - 1)
              + "</a></span>");
        }
        // Current page
        pag.append("<span>" + cp + "</span>");
        if (cp + 1 < tp) {
          // Current page + 1
          pag.append("<span><a href=\"" + url + (cp + 1) + "\">" + (cp + 1)
              + "</a></span>");
        }
        if (cp + 2 < tp) {
          // Current page + 2
          pag.append("<span><a href=\"" + url + (cp + 2) + "\">" + (cp + 2)
              + "</a></span>");
        }
        // Last page
        pag.append(
            "<span><a href=\"" + url + (tp) + "\">" + tp + "</a></span>");
      } else {
        // Current page is last page
        pag.append("<span><a href=\"" + url + "1" + "\">1</a></span>");
        if (cp - 2 > 1) {
          // Last page - 2
          pag.append("<span><a href=\"" + url + (cp - 2) + "\">" + (cp - 2)
              + "</a></span>");
        }
        if (cp - 1 > 1) {
          // Previous page
          pag.append("<span><a href=\"" + url + (cp - 1) + "\">" + (cp - 1)
              + "</a></span>");
        }
        // Last page
        pag.append("<span>" + tp + "</span>");
      }
      return pag.append("</div>").toString();
    }
  }
}