<%@page import="java.util.ArrayList"%>
<%@ page import="utils.GetterUser"%>
<%@ page import="utils.MenuFooter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	// TODO javascript for check password and user name
	session = request.getSession();
	String title;
	StringBuilder webContent = new StringBuilder();
	GetterUser getUser = new GetterUser();
	MenuFooter menuFooter = new MenuFooter();
	webContent.append(menuFooter.getMenu(session) + "<br><div id=\"div_body\">");
	if (session != null && session.getAttribute("id") != null) {
		if (request.getParameter("uid") != null && request.getParameter("uid").matches("^\\d+$")) {
			// Editing user
			if (Integer.parseInt(session.getAttribute("id").toString()) == Integer
					.parseInt(request.getParameter("uid"))
					|| Integer.parseInt(session.getAttribute("id").toString()) == 1) {
				String[] user = getUser.getUser(Integer.parseInt(session.getAttribute("id").toString()),
						session);
				String[] getEditUser = getUser.getEditUser(Integer.parseInt(request.getParameter("uid")),
						session);
				webContent.append(getEditUser[0]);
				title = "Edit user: " + getEditUser[1];
			} else {
				title = "Wrong place";
				webContent.append("You can't be here.");
			}
			// No uid
		} else {
			title = "Click an user";
		}
		// New user
	} else {
		title = "New User";
		webContent.append("<form action=\"new-user/\" method=\"POST\" enctype=\"multipart/form-data\">"
				+ "User name: <input required  name=\"user_user_name\" type=\"text\" onchange=\"checkUser()\">"
				+ "<br>Password: <input required type=\"password\" name=\"user_password\" id=\"password\" onchange=\"checkPassword()\">"
				+ "<br>Repeat password: <input required type=\"password\" id=\"repeatPassword\" onchange=\"checkPassword()\">"
				+ "<br>E-mail: <input required type=\"email\" name=\"user_email\" onchange=\"checkEmail()\">"
				+ "<br>Name: <input type=\"text\" name=\"user_name\">"
				+ "<br>Surname: <input type=\"text\" name=\"user_surname\">"
				+ "<br>Country: <input type=\"text\" name=\"user_country\">"
				+ "<br>State: <input type=\"text\" name=\"user_state\">"
				+ "<br>City: <input type=\"text\" name=\"user_city\">"
				+ "<br>Profile picture: <input type=\"file\" accept=\"images/*\" "
				+ "name=\"user_profile_picture\">" + "<br><input type=\"submit\" value=\"New\"></form>");
	}
	webContent.append("</div><br>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title><%=title%></title>
<link rel="stylesheet" type="text/css" href="css/user.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body onload="setPath()">
	<script type="text/javascript">
		// set a path value and let safe when the web content load.
		var path;
		function setPath(){
			path = document.getElementById("path").value;
		}
		
		function deleteUploadFile(){
			document.getElementById("picture").value = "";
			document.getElementById("path").value = path;
		}
		
		function deleteCurrentFile(){
			document.getElementById("div_current_pìc").style.display = "none";
			document.getElementById("path").value = "";
		}
		
		function checkingPath() {
			if (document.getElementById("picture").files.value != null
					|| document.getElementById("picture").value != ""){
				document.getElementById("path").value = "";
			}
			else {
				document.getElementById("path").value = path;
			}
		}
	</script>
	<%=webContent%>
</body>
</html>