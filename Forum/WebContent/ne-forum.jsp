<%@page import="java.util.ArrayList"%>
<%@page import="utils.GetterForum"%>
<%@ page import="utils.GetterUser"%>
<%@ page import="utils.GetterCategory"%>
<%@ page import="utils.MenuFooter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	String title;
	StringBuilder webContent = new StringBuilder();
	MenuFooter menuFooter = new MenuFooter();
	webContent.append(menuFooter.getMenu(session) + "<br><div id=\"div_body\">");
	if (session != null && session.getAttribute("id") != null
			&& Integer.parseInt(session.getAttribute("id").toString()) == 1
			&& request.getParameter("cid") != null && request.getParameter("cid").matches("^\\d$")) {
		// TODO make changes about category select 
		GetterCategory category = new GetterCategory();
		GetterUser user = new GetterUser();
		if (request.getParameter("fid") != null && request.getParameter("fid").matches("^\\d+$")) {
				GetterForum forum = new GetterForum();
				String[] fm = forum.getForum(Integer.parseInt(request.getParameter("fid")));
				title = "Edit " + fm[1];
				webContent.append("<form action=\"edit-forum/\" method=\"POST\">"
						+ "<input type=\"hidden\" name=\"forum_forum_id\" value=\"" 
						+ request.getParameter("fid") + "\">"
						+ "Name: <input type=\"text\" value=\"" + fm[1] 
						+ "\" required name=\"forum_name\""
						+ " placeholder=\"Introduce a name for the forum\"><br>"
						+ "Description: <textarea type=\"text\" "
						+"name=\"forum_description\">" + fm[2]+ "</textarea><br>" 
						+ "Moderators: " + user.getModUsers(
								Integer.parseInt(request.getParameter("fid"))) 
						+ "<br>Category: "
						+ category.getCategoryWEBSelect("forum_category_id", Integer.parseInt(fm[0]))
						+ "<br><input type=\"submit\" value=\"Edit forum\"></form>");
			} else if (request.getParameter("cid") != null && request.getParameter("cid").matches("^\\d+$")) {
				title = "New Forum";
				webContent.append("<form action=\"new-forum/\" method=\"POST\">"
						+ "Name: <input type=\"text\" value=\"\" required name=\"forum_name\""
						+ " placeholder=\"Introduce a name for the forum\"><br>"
						+ "Description: <input type=\"text\" value=\"\" name=\"forum_description\"><br>"
						+ "Moderator: " + user.getModUsers(-1)
						+ "<br><input type=\"hidden\" name=\"forum_category_id\" value=\""
						+ request.getParameter("cid") + "\"> "
						+ "<br><input type=\"submit\" value=\"New Forum\"></form>");
			} else {
				title = "No category";
				webContent.append("<span id=\"span_error\">You must select a category.</span>");
			}
	} else {
		title = "Wrong place";
		webContent.append("<span id=\"span_error\">You cannot be here.</span>");
	}
	webContent.append("</div><br>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="css/new_edit_forum.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
	<%=webContent%>
</body>
</html>