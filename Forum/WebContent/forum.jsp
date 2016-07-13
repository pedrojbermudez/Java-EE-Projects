<%@page import="utils.Pagination"%>
<%@page import="utils.GetterThread"%>
<%@ page import="utils.MenuFooter, utils.GetterForum"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	GetterForum getterForum;
	MenuFooter menuFooter = new MenuFooter();
	String menu = menuFooter.getMenu(session);
	String footer = menuFooter.getFooter();
	StringBuilder forums = new StringBuilder();
	// Getting the current page
	int cp = request.getParameter("p") != null ? Integer.parseInt(request.getParameter("p")) : 1;
	forums.append("<div id=\"div_body\">");
	// no category
	if (request.getParameter("cid") == null) {
		forums.append("<span>You must select a category.</span>");
		// correct category
	} else if (request.getParameter("cid").matches("^\\d+$") && request.getParameter("fid") == null) {
		getterForum = new GetterForum();
		if (session != null && session.getAttribute("id") != null
				&& session.getAttribute("id").toString().matches("^\\d+$")
				&& Integer.parseInt(session.getAttribute("id").toString()) == 1
				&& request.getParameter("fid") == null) {
			forums.append("<div id=\"div_nf\"><span id=\"span_nf\"><a href=\"ne-forum.jsp?cid="
					+ request.getParameter("cid") + "\">New Forum</a></span></div><br>");
		}
		forums.append(
				"<div>" + getterForum.getForums(Integer.parseInt(request.getParameter("cid")), session, request)
						+ "</div>");
	} else if (request.getParameter("cid").matches("^\\d+$") && request.getParameter("fid").matches("^\\d+$")) {
		if (session != null && session.getAttribute("id") != null
				&& session.getAttribute("id").toString().matches("^\\d+$")
				&& request.getParameter("fid") != null) {
			forums.append("<div id=\"div_nt\"><span id=\"span_nf\"><a href=\"ne-thread.jsp?fid="
					+ request.getParameter("fid") + "\">New Thread</a></span></div><br>");
		}
		GetterThread getterThread = new GetterThread();
		forums.append("<br>"
				+ getterThread.getThreadsWeb(Integer.parseInt(request.getParameter("fid")), cp, 25, session)
				+ (new Pagination(
						(new GetterThread()).getTotalThreads(Integer.parseInt(request.getParameter("fid"))),
						25)).getPag(cp, request.getRequestURL().toString(), request.getQueryString()));
	} else {
		forums.append("<span>You must select a category.</span>");
	}
	forums.append("</div>");
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
	<%="<br>" + forums + "<br>"%>
	<%=footer%>
</body>
</html>