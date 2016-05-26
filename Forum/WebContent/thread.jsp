<%@page import="utils.MenuFooter"%>
<%@page import="utils.GetterPost"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%
	StringBuilder content = new StringBuilder();
	MenuFooter menuFooter = new MenuFooter();
	content.append(menuFooter.getMenu(session) + "<div id=\"div_body\">");
	GetterPost getterPost = new GetterPost();
	if(request.getParameter("tid") == null){
		content.append("<span>Please select a forum.</span>");
	}
	else if(!request.getParameter("tid").matches("^\\d+$")){
		content.append("<span>Please select a valid forum.</span>");
	}
	else{
		content.append((session != null && session.getAttribute("id") != null 
				&& session.getAttribute("id").toString().matches("^\\d+$") 
				? "<span><a href=\"ne-post.jsp?tid=" + request.getParameter("tid")
				+ "\">New post</a></span><br>" 
				: "" )+ getterPost.getPostsWeb(Integer.parseInt(request.getParameter("tid")), session));
		
	}
	content.append("</div>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>f</title>
<link rel="stylesheet" type="text/css" href="css/thread.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
	<%= content %>
</body>
</html>