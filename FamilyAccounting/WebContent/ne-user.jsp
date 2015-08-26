<%@page import="com.family.accounting.libraries.Database"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.family.accounting.libraries.Utils"%>
<%
	int mode = Integer.parseInt(request.getParameter("m"));
	String title;
	if (mode == 0) {
		title = "New user";
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

		<form action="user" method="post">
			<input type="hidden" name="mode" value="0">Name: <input
				type="text" name="name"><br>Surname: <input type="text"
				name="surname"><br>Email: <input type="text"
				name="email"><br>User name: <input type="text"
				name="userName"><br>Password: <input type="password"
				name="password" onkeyup="checkPassword()" id="password"><br>Repeat
			password: <input type="password" name="repeatPassword"
				onkeyup="checkPassword()" id="repeatPassword"><br> <input
				type="submit" value="Create" disabled="disabled" id="buttonSE">
		</form>

		<%
			} else {
				String[] user = new Database((new Utils()).getDatabasePath())
						.getUser(Integer.parseInt(session.getAttribute("userId").toString()));
				String name = user[0];
				String surname = user[1];
				String email = user[2];
		%>
		<form action="user" method="post">
			<input type="hidden" name="mode" value="1">Name: <input
				type="text" name="name" value="<%=name%>"><br>Surname:
			<input type="text" name="surname" value="<%=surname%>"><br>Email:
			<input type="text" name="email" value="<%=email%>"><br> <input type="submit"
				value="Edit" id="buttonSE">
		</form>
		<%
			}
		%>
	</div>
</body>
</html>