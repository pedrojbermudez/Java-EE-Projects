<%@page import="utils.MenuFooter"%>
<%@page import="utils.GetterThread"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	MenuFooter menuFooter = new MenuFooter();
	StringBuilder content = new StringBuilder();
	content.append(menuFooter.getMenu(session) + "<div id=\"div_body\">" 
			+ (new GetterThread()).get30Threads() 
			+ "</div>" + menuFooter.getFooter() );
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<link rel="stylesheet" type="text/css" href="css/main.css">
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Main page</title>
	</head>
	<body>
		<%= content %>
	</body>
</html>