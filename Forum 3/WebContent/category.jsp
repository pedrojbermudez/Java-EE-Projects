<%@page import="java.util.ArrayList"%>
<%@ page import="utils.MenuFooter, utils.GetterCategory"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();

	MenuFooter menuFooter = new MenuFooter();
	String menu = menuFooter.getMenu(session);
	String footer = menuFooter.getFooter();
	StringBuilder webContent = new StringBuilder();
	webContent.append("<div id=\"div_body\">");
	if (session != null && session.getAttribute("id") != null 
			&& session.getAttribute("id").toString().matches("^\\d+$") 
			&& Integer.parseInt(session.getAttribute("id").toString()) == 1) {
		ArrayList<String[]> categoryList = (new GetterCategory()).getCategoryList();
		webContent.append("<div><span><a href=\"ne-cat.jsp\">New Category</a></span></div>");
		for (String[] cat : categoryList) {
			webContent.append("<span><a href=\"ne-cat.jsp?cid=" 
				+ cat[0] + "\">" + cat[1] + "</a></span><br>");
		}
	} else {
		webContent.append("<span>You can't be here.</span>");
	}
	webContent.append("</div>");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Forums List</title>
<link rel="stylesheet" type="text/css" href="css/forum.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
	<%=menu%>
	<%="<br>" + webContent + "<br>"%>
	<%=footer%>
</body>
</html>