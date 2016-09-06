<%@page import="utils.GetterUser"%>
<%@page import="utils.GetterForum"%>
<%@page import="utils.GetterThread"%>
<%@page import="java.util.ArrayList"%>
<%@ page import="utils.MenuFooter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	String title;
	StringBuilder webContent = new StringBuilder();
	MenuFooter menuFooter = new MenuFooter();
	webContent.append(menuFooter.getMenu(session) + "<div id=\"div_body\">");
	if (session != null && session.getAttribute("id") != null
			&& session.getAttribute("id").toString().matches("^\\d+$")) {
		if (request.getParameter("tid") != null && request.getParameter("tid").matches("^\\d+$")) {
			String[] thread = (new GetterThread())
					.getThread(Integer.parseInt(request.getParameter("tid").toString()));
			ArrayList<Integer> moderators = (new GetterUser()).getListModUserIds(
					(new GetterForum()).getForumId(Integer.parseInt(request.getParameter("tid"))));
			if (Integer.parseInt(thread[2]) == Integer.parseInt(session.getAttribute("id").toString())
					|| 1 == Integer.parseInt(session.getAttribute("id").toString())
					|| moderators.contains(Integer.parseInt(session.getAttribute("id").toString()))) {
				title = "Edit thread: " + thread[0];
				String admin;
				if (Integer.parseInt(session.getAttribute("id").toString()) == 1) {
					admin = (new GetterForum()).getForumList(Integer.parseInt(thread[1]), "forum_id");
				} else {
					admin = "<input type=\"hidden\" name=\"forum_id\" value=\"" + thread[1] + "\">";
				}
				webContent.append("<form action=\"edit-thread/\" method=\"POST\">"
						+ "<input type=\"hidden\" name=\"thread_id\" value=\"" + request.getParameter("tid")
						+ "\">" + "Name: <input type=\"text\" value=\"" + thread[0]
						+ "\" required name=\"thread_name\"" + "<br>" + admin
						+ "<input type=\"submit\" value=\"Edit\"></form>");
			} else {
				title = "Wrong Place";
				webContent.append("<span id=\"span_error\">You can't be here.</span>");
			}
		} else {
			title = "New Thread";
			if (request.getParameter("fid") != null && request.getParameter("fid").matches("^\\d+$")) {
				webContent.append("<form action=\"new-thread/\" method=\"POST\">"
						+ "<input type=\"hidden\" name=\"thread_forum_id\" value=\""
						+ request.getParameter("fid") + "\">"
						+ "Name: <input type=\"text\" name=\"thread_name\" required><br>"
						+ "Post:<br><textarea name=\"thread_post\" required></textarea>"
						+ "<br><input type=\"submit\" value=\"New Thread\"></form>");
			}
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