<%@page import="java.util.ArrayList"%>
<%@ page import="utils.MenuFooter, utils.GetterCategory"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();

	MenuFooter menuFooter = new MenuFooter();
	String menu = menuFooter.getMenu(session);
	String footer = menuFooter.getFooter();
	StringBuilder categories = new StringBuilder();
	categories.append("<div id=\"div_body\">");
	if (session != null && session.getAttribute("id") != null 
			&& session.getAttribute("id").toString().matches("^\\d+$") 
			&& Integer.parseInt(session.getAttribute("id").toString()) == 1) {
		GetterCategory getterCat = new GetterCategory();
		ArrayList<String[]> categoryList = getterCat.getCategoryList();
		categories.append("<div><span><a href=\"ne-cat.jsp\">New Category</a></span></div>");
		for (String[] cat : categoryList) {
			categories.append("<span><a href=\"ne-cat.jsp?cid=" 
				+ cat[0] + "\">" + cat[1] + "</a></span><br>");
		}
	} else {
		categories.append("<span>You can't be here.</span>");
	}
	categories.append("</div>");
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
	<%="<br>" + categories + "<br>"%>
	<%=footer%>
</body>
</html>