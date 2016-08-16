<%@page import="java.util.Enumeration"%>
<%@page import="utils.MenuFooter"%>
<%@page import="utils.GetterThread"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
  MenuFooter menuFooter = new MenuFooter();
  StringBuilder content = new StringBuilder();
  if (session != null && session.getAttribute("error") != null) {
    // Session attribute has error. In that case user name or password was typed wrong
    content.append("<script type=\"text/javascript\">"
        + "window.alert(\"User name or pasword incorrect.\")</script>");
    session.removeAttribute("error");
  } else if (session != null && session.getAttribute("deleted") != null) {
    // User was deleted by administrator
    content.append("<script type=\"text/javascript\">" + "window.alert(\""
        + session.getAttribute("deleted").toString() + "\")</script>");
    session.removeAttribute("deleted");
  }
  content.append(menuFooter.getMenu(session) + "<br><div id=\"div_body\">"
      + (new GetterThread()).get30Threads() + "</div><br>"
      + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" type="text/css" href="css/main.css">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Main page</title>
</head>
<body>
	<%=content%>
</body>
</html>