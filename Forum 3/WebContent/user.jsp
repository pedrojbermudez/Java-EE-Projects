<%@page import="utils.MenuFooter"%>
<%@page import="utils.GetterUser"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	StringBuilder webContent = new StringBuilder();
	MenuFooter menuFooter = new MenuFooter();
	String title;
	webContent.append(menuFooter.getMenu(session) + "<div id=\"div_body\">");
	if (request.getParameter("uid") != null && request.getParameter("uid").matches("\\d+")) {
		GetterUser getUser = new GetterUser();
		String[] user = getUser.getUser(Integer.parseInt(request.getParameter("uid")), session);
		title = user[1] + "\'s profile";
		webContent.append(user[0]);
	} else {
		title = "Choose a user";
		webContent.append("<div>Choose a user<div>");
	}
	webContent.append("</div>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/user.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
<title><%=title%></title>
</head>
<body>
	<%=webContent%>
</body>
</html>