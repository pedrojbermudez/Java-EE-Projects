package com.family.accounting.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.family.accounting.libraries.*;

public class Sources extends HttpServlet {
  /**
   * 
   */
  private Utils utils;
  private Database db;
  private static int numberElements;
  private int numberPage;
  private static final long serialVersionUID = 1L;
  private int userId;
  private int mode;
  private int totalElements;
  private int menuMode;
  private HttpSession session;

  public void init() throws ServletException {
    ServletConfig config = getServletConfig();
    numberElements = Integer
        .parseInt(config.getInitParameter("numberElements"));
    numberPage = Integer.parseInt(config.getInitParameter("numberPage"));
    userId = Integer.parseInt(config.getInitParameter("userId"));
    mode = Integer.parseInt(config.getInitParameter("mode"));
    menuMode = 0;
    utils = new Utils();
    db = new Database(utils.getDatabasePath());
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    // **********************************************
    // variables and setters.
    // **********************************************
    session = request.getSession(true);
    userId = session.getAttribute("userId") == null ? -1
        : Integer.parseInt(session.getAttribute("userId").toString());
    totalElements = db.countRowSource(userId);
    numberPage = request.getParameter("p") != null
        ? Integer.parseInt(request.getParameter("p")) - 1 : numberPage;
    ArrayList<String[]> sources = db.getSources(numberPage * numberElements,
        numberElements, userId);
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    StringBuilder htmlCode = new StringBuilder();

    // **********************************************
    // making the html code.
    // **********************************************
    htmlCode.append("<HTML><HEAD><TITLE>Money Sources</TITLE>\n"
        + "\n<link rel=\"stylesheet\" "
        + "type=\"text/css\" href=\"resources/css/general.css\">\n"
        + "<script type=\"text/javascript\" src=\"resources/js/source.js\"></script>"
        + "<script type=\"text/javascript\" src=\"resources/js/general.js\"></script>"
        + "</HEAD>\n<BODY><div id=\"div_body\">"
        + utils.menu(menuMode, request));
    if (sources != null) {
      htmlCode.append("<div id=\"div_inner_body\"><table><tr><td class=\"td_header\">Name</td>"
          + "<td class=\"td_header\">Total</td></tr>");
      for (int i = 0; i < sources.size(); i++) {
        htmlCode.append("<tr>\n");
        String[] strArr = sources.get(i);
        String id = "";
        for (int j = 0; j < strArr.length; j++) {
          if (j == 0) {
            id = strArr[0];
          } else if (j < strArr.length - 1) {
            htmlCode.append("<td class=\"td_data\">" + strArr[j] + "</td>\n");
          } else {
            htmlCode.append("<td class=\"td_data\">" + strArr[j]
                + "</td><td><div class=\"div_edit\"><a href=\"ne-source.jsp?m=1&sid="
                + id + "\"><img class=\"img_menu\" "
                + "src=\"resources/images/edit.png\"></a></div></td><td><div "
                + "class=\"div_delete\"><input type=\"image\" class=\"img_menu\" "
                + "src=\"resources/images/delete.png\" onclick=\"deleteSource("
                + id + ")\"></div></td></tr>");
          }
        }
      }
      htmlCode
          .append("</table>" + utils.pagination(totalElements, (numberPage + 1),
              numberElements, request.getRequestURL().toString()));
    } else if (sources == null) {
      if (userId != -1) {
        htmlCode.append("<div id=\"div_inner_body\">There are no money sources");
      } else {
        htmlCode.append("<div id=\"div_inner_body\">Please login to watch all money sources.");
      }
    }
    htmlCode.append("</div></div></BODY></HTML>");
    out.println(utils.getDocType() + htmlCode.toString());
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    mode = Integer.parseInt(request.getParameter("mode"));
    session = request.getSession(false);
    userId = session.getAttribute("userId") == null ? -1
        : Integer.parseInt(session.getAttribute("userId").toString());
    String name = request.getParameter("name");
    double total = request.getParameter("total") == null ? 0
        : Double.parseDouble(request.getParameter("total"));
    int sourceId = request.getParameter("sourceId") == null ? -1
        : Integer.parseInt(request.getParameter("sourceId"));
    switch (mode) {
      case 0: // create mode
        db.newSource(name, total, userId);
        mode = -1;
        break;
      case 1: // edit mode
        db.editSource(sourceId, name, total, userId);
        mode = -1;
        break;
      case 2: // delete mode
        db.deleteSource(sourceId, userId);
        mode = -1;
        break;
    }
    doGet(request, response);
  }

}
