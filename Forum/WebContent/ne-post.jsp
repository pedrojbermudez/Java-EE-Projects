<%@page import="utils.GetterForum"%>
<%@page import="utils.GetterThread"%>
<%@page import="utils.GetterPost"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="utils.GetterUser"%>
<%@ page import="utils.MenuFooter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	String title;
	StringBuilder webContent = new StringBuilder();
	GetterUser user = new GetterUser();
	MenuFooter menuFooter = new MenuFooter();
	webContent.append(menuFooter.getMenu(session) + "<br><div id=\"div_body\">");
	if (session != null && session.getAttribute("id") != null
			&& session.getAttribute("id").toString().matches("^\\d+$") && request.getParameter("tid") != null
			&& request.getParameter("tid").matches("^\\d+$")) {
		String tid = "<input type=\"hidden\" name=\"post_thread_id\" value=\"" + request.getParameter("tid")
				+ "\">";
		if (request.getParameter("pid") != null && request.getParameter("pid").matches("^\\d+$")) {
			GetterPost getPost = new GetterPost();
			String[] post = getPost.getPost(Integer.parseInt(request.getParameter("pid")));
			ArrayList<String[]> moderators = (new GetterUser()).getListModUsers(
					(new GetterForum()).getForumId(Integer.parseInt(request.getParameter("tid"))));
			if (Integer.parseInt(session.getAttribute("id").toString()) == Integer.parseInt(post[1])
					|| Integer.parseInt(session.getAttribute("id").toString()) == 1
					|| moderators.contains(Integer.parseInt(session.getAttribute("id").toString()))) {
				webContent.append("<form action=\"edit-post/\" method=\"POST\">"
						+ "<input type=\"hidden\" value=\"" + request.getParameter("pid")
						+ "\" name=\"post_id\">" + tid + "<textarea required name=\"post_post\">" + post[0]
						+ "</textarea>" + "<br><input type=\"submit\" value=\"Edit\"></form>");
				title = "Edit Post";
			} else {
				title = "Wrong place";
				webContent.append("<span>You can't be here.</span>");
			}
		} else {
			title = "New Post";
			webContent.append("<form action=\"new-post/\" method=\"POST\">"
					+ "Post: <textarea type=\"text\" required name=\"post_post\"></textarea>" + tid
					+ "<br><input type=\"submit\" value=\"New\"></form>");
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