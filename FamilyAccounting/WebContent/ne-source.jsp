<%@page import="com.family.accounting.libraries.Utils"%>
<%@page import="com.family.accounting.libraries.Database"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	int mode = Integer.parseInt(request.getParameter("m"));
	String title;
	if (mode == 0) {
		title = "New source";
	} else {
		title = "Edit " + session.getAttribute("userName").toString();
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="resources/css/general.css">
<script type="text/javascript" src="resources/js/general.js"></script>
</head>
<body>
	<div id="div_body">
		<%
			int sourceId = request.getParameter("sid") == null ? -1 : Integer.parseInt(request.getParameter("sid"));
			String menu = (new Utils()).menu(-1, request);
			if (mode == 0) {
		%>
		<%=menu%>
		<div>
			<form action="sources" method="post">
				<input type="hidden" name="mode" value="0">Name: <input
					type="text" name="name"><br>Total: <input type="text"
					name="total" oninput="isNumber(this)"> <input type="submit"
					value="Send" disabled="disabled" id="buttonSE">
			</form>
		</div>
		<%
			} else {
				Database db = new Database((new Utils()).getDatabasePath());
				String[] source = db.getSource(sourceId, Integer.parseInt(session.getAttribute("userId").toString()));
		%>
		<%=menu%>
		<div>
			<form action="sources" method="post">
				<input type="hidden" name="sourceId" value="<%=sourceId%>">
				<input type="hidden" name="mode" value="1">Name: <input
					type="text" name="name" value="<%=source[0]%>"><br>Total:
				<input type="text" name="total" value="<%=source[1]%>"
					oninput="isNumber(this)"><br> <input type="submit"
					value="Edit" disabled="disabled" id="buttonSE">
			</form>
		</div>
		<%
			}
		%>
	</div>
</body>
</html>