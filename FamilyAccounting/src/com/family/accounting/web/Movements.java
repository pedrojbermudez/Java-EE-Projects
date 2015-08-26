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

public class Movements extends HttpServlet {
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
    menuMode = 1;
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
    totalElements = db.countRowMovements(userId);
    numberPage = request.getParameter("p") != null
        ? Integer.parseInt(request.getParameter("p")) - 1 : numberPage;
    ArrayList<String[]> movements = request.getParameter("cid") == null
        ? db.getMovements(numberPage * numberElements, numberElements, userId)
        : db.getMovements(numberPage * numberElements, numberElements, userId,
            Integer.parseInt(request.getParameter("cid")));
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    StringBuilder htmlCode = new StringBuilder();
    // **********************************************
    // making the html code.
    // **********************************************
    htmlCode.append(
        "<HTML><HEAD><TITLE>Movements</TITLE>\n" + "\n<link rel=\"stylesheet\" "
            + "type=\"text/css\" href=\"resources/css/general.css\">\n"
            + "<script type=\"text/javascript\" src=\"resources/js/movement.js\"></script>"
            + "<script type=\"text/javascript\" src=\"resources/js/general.js\"></script>"
            + "</HEAD>\n<BODY><div id=\"div_body\">"
            + utils.menu(menuMode, request));
    if (movements != null) {
      htmlCode.append(
          "<div id=\"div_inner_body\"><table><tr><td class=\"td_header\">Name</td>"
              + "<td class=\"td_header\">Money source name</td>"
              + "<td class=\"td_header\">Category name</td>"
              + "<td class=\"td_header\">Movement date</td>"
              + "<td class=\"td_header\">Income</td>"
              + "<td class=\"td_header\">Outgoing</td></tr>\n");
      for (int i = 0; i < movements.size(); i++) {
        String[] movArr = movements.get(i);
        // adding name
        htmlCode.append("<tr><td class=\"td_data\">" + movArr[1] + "</td>");
        // adding source name and link
        htmlCode.append("</td><td class=\"td_data\"><a href=\"sources?sid="
            + movArr[5] + "\">" + movArr[6] + "</a></td>");
        // adding category name and link.
        htmlCode.append("<td class=\"td_data\"><a href=\"movements?cid="
            + movArr[7] + "\">" + movArr[8] + "</a></td>");
        // adding date
        htmlCode.append("<td class=\"td_data\">" + movArr[2] + "</td>");
        // adding income
        htmlCode.append("<td class=\"td_data\">" + movArr[3] + "</td>");
        // adding outgoing
        htmlCode.append("<td class=\"td_data\">" + movArr[4] + "</td>");
        // adding edit movement.
        htmlCode.append(
            "<td><div class=\"div_edit\"><a href=\"ne-movement.jsp?m=1&mid="
                + movArr[0] + "\"><img class=\"img_menu\" "
                + "src=\"resources/images/edit.png\"></a></div></td>");
        // adding delete movement
        htmlCode.append("<td><div "
            + "class=\"div_delete\"><input type=\"image\" class=\"img_menu\" "
            + "src=\"resources/images/delete.png\" onclick=\"deleteMovement("
            + movArr[0] + ", " + movArr[5] + ", " + movArr[3] + "," + movArr[4]
            + ")\"></div></td></tr>");
      }
      htmlCode
          .append("</table>" + utils.pagination(totalElements, (numberPage + 1),
              numberElements, request.getRequestURL().toString()));

    } else if (movements == null) {
      if (userId != -1) {
        htmlCode.append("<div id=\"div_inner_body\">There are no movements.");
      } else {
        htmlCode.append(
            "<div id=\"div_inner_body\">You must be logged to watch all movements.");
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
    int movId = request.getParameter("movId") == null ? -1
        : Integer.parseInt(request.getParameter("movId"));
    String name = request.getParameter("name");
    String day = request.getParameter("day") == null ? ""
        : request.getParameter("day");
    String month = request.getParameter("month") == null ? ""
        : request.getParameter("month");
    String year = request.getParameter("year") == null ? ""
        : request.getParameter("year");
    int categoryId = request.getParameter("categoryId") == null ? -1
        : Integer.parseInt(request.getParameter("categoryId"));
    double income = 0;
    double outgoing = 0;
    double oldOutgoing = request.getParameter("oldOutgoing") == null ? -1
        : Double.parseDouble(request.getParameter("oldOutgoing"));
    double oldIncome = request.getParameter("oldIncome") == null ? -1
        : Double.parseDouble(request.getParameter("oldIncome"));
    ;
    int sourceId = request.getParameter("sourceId") == null ? -1
        : Integer.parseInt(request.getParameter("sourceId"));
    if (mode == 0 || mode == 1) {
      if (request.getParameter("movValue") != null) {
        if (Double.parseDouble(request.getParameter("movValue")) > 0) {
          income = Double.parseDouble(request.getParameter("movValue"));
          outgoing = 0;
        } else if (Double.parseDouble(request.getParameter("movValue")) < 0) {
          outgoing = Double.parseDouble(request.getParameter("movValue")) * -1;
          income = 0;
        }
      }
    }
    switch (mode) {
      case 0: // create mode
        db.newMovement(name, (year + "/" + month + "/" + day), income, outgoing,
            sourceId, userId, categoryId);
        mode = -1;
        break;
      case 1: // edit mode
        db.editMovement(movId, sourceId, name, (year + "/" + month + "/" + day),
            income, outgoing, oldIncome, oldOutgoing, userId, categoryId);
        mode = -1;
        break;
      case 2: // delete mode
        db.deleteMovement(movId, sourceId, userId, oldIncome, oldOutgoing);
        mode = -1;
        break;
    }
    doGet(request, response);
  }

}
