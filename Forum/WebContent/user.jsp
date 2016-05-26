<%@page import="utils.MenuFooter"%>
<%@page import="utils.GetterUser"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	StringBuilder webContent = new StringBuilder();
	MenuFooter menuFooter = new MenuFooter();
	String title;
	webContent.append(menuFooter.getMenu(session) + "<div id\"div_body\">");
	if(session != null && session.getAttribute("id") != null 
			&& Integer.parseInt(session.getAttribute("id").toString()) == 1
			&& request.getParameter("mod").equals("true")){
		webContent.append("<div class=\"div_user_content\">"+"</div>");
		title = "Mod list";
	} else if (request.getAttribute("uid") != null 
			&& request.getParameter("uid").matches("\\d+")) {
		GetterUser getUser = new GetterUser();
		String[] user = getUser.getUser(Integer.getInteger(request.getParameter("uid").toString()));
		title = user[2] + "'s profile";
		webContent.append(
			"<div class=\"div_user_content\">User name:<br>" + user[2]
			+ "</div><div class=\"div_user_content\">Avatar:<br>"
			+ "<img src=\"" + user[3] +"\" id=\"img_avatar\"></div>" 
			+ "<div class=\"div_user_content\">Name:<br>" + user[0] 
			+ " "+ user[1] + "</div>" + "<div class=\"div_user_content\">" 
			+ "Ubication: " + user[4] + ", "  + user[5] + ", " + user[6] 
			+ "</div>");
		if (session != null && 
				(Integer.parseInt(request.getParameter("uid")) 
					== Integer.parseInt(session.getAttribute("id").toString()) 
				|| Integer.parseInt(session.getAttribute("id").toString()) 
					== 1)) {
			webContent.append("<button formaction=\"ne-user.jsp?uid=" 
				+ request.getParameter("uid") + "\">Edit</button>");
		}
	} else {
		title = "Choose a user";
	}
	webContent.append("</div>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="stylesheet" type="text/css" href="css/user.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
<title><%= title %></title>
</head>
<body>
	<%= webContent %>
</body>
</html>