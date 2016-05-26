<%@page import="utils.GetterThread"%>
<%@ page import="utils.MenuFooter, utils.GetterForum"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	GetterForum getterForum;
	MenuFooter menuFooter = new MenuFooter();
	String menu = menuFooter.getMenu(session);
	String footer = menuFooter.getFooter();
	StringBuilder forums = new StringBuilder();
	forums.append("<div id=\"div_body\">");
	if (request.getParameter("cid") == null) {
		forums.append("<span>You must select a category.</span>");
	} else if (request.getParameter("cid").matches("^\\d+$")) {
		getterForum = new GetterForum();
		if (session != null && session.getAttribute("id") != null 
			&& session.getAttribute("id").toString().matches("^\\d+$") 
			&& Integer.parseInt(session.getAttribute("id").toString()) == 1 
			&& request.getParameter("fid") == null){
				forums.append("<div id=\"div_nf\"><span id=\"span_nf\"><a href=\"ne-forum.jsp?cid="
						+ request.getParameter("cid") + "\">New Forum</a></span></div><br>");
		} else if (session != null && session.getAttribute("id") != null 
			&& session.getAttribute("id").toString().matches("^\\d+$") 
			&& request.getParameter("fid") != null){
				forums.append("<div id=\"div_nt\"><span id=\"span_nf\"><a href=\"ne-thread.jsp?fid="
						+ request.getParameter("fid") + "\">New Thread</a></span></div><br>");
		}
		forums.append("<div>"+getterForum.getForums(
				Integer.parseInt(request.getParameter("cid")))+ "</div>");
		if(request.getParameter("fid") != null && request.getParameter("fid").matches("^\\d+$")){
			GetterThread getterThread = new GetterThread();
			forums.append("<br>" + getterThread.getThreadsWeb(
					Integer.parseInt(request.getParameter("fid")), session));
		}
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