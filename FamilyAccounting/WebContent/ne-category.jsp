<%@page import="com.family.accounting.libraries.Database"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.family.accounting.libraries.Utils"%>
<%
	int mode = Integer.parseInt(request.getParameter("m"));
	String title;
	if (mode == 0) {
		title = "New Category";
	} else {
		title = "Edit " + session.getAttribute("userName").toString();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="resources/css/general.css">
<title><%=title%></title>
<script type="text/javascript" src="resources/js/user.js"></script>
<script type="text/javascript" src="resources/js/md5.js"></script>
</head>
<body>
	<div id="div_body">
		<%=new Utils().menu(-1, request)%>
		<%
			if (mode == 0) {
		%>

		<form action="categories" method="post">
			<input type="hidden" name="mode" value="0">Name: <input
				type="text" name="name"><br> <input type="submit"
				value="Create" id="buttonSE">
		</form>

		<%
			} else {
				String categoryId = request.getParameter("cid");
				String name = new Database((new Utils()).getDatabasePath()).getCategory(Integer.parseInt(categoryId),
						Integer.parseInt(session.getAttribute("userId").toString()));
		%>
		<form action="categories" method="post">
			<input type="hidden" name="mode" value="1"> <input
				type="hidden" name="categoryId" value="<%=categoryId%>">
			Name: <input type="text" name="name" value="<%=name%>"><br>
			<input type="submit" value="Edit" id="buttonSE">
		</form>
		<%
			}
		%>
	</div>
</body>
</html>