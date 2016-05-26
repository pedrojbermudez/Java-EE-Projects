<%@page import="utils.MenuFooter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="utils.GetterUser"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<% 
	String javascript;
	StringBuilder webContent = new StringBuilder();
	MenuFooter menuFooter = new MenuFooter();
	webContent.append(menuFooter.getMenu(session) + "<div id=\"div_body\">");
	if(session != null && Integer.parseInt(session.getAttribute("id").toString()) == 1){
		javascript = "<script type=\"text/javascript\">function setModUser(checkbox) {\n"
				+ "window.alert($(checkbox).prop('checked'));\n"
				+"$.ajax({\n"
				+"type : 'post',\n"
				+"url : 'SetModUser',\n"
				+"data : {\n"
				+"user_id : $(checkbox).val(),\n"
				+"is_mod : $(checkbox).prop('checked') == true ? \"y\" : \"n\"\n"
				+"}\n"
				+"});\n"
				+"}</script>";
		webContent.append(javascript + (new GetterUser()).getUserList(0));
	} else {
		javascript = "";
		webContent.append("<span id=\"span_error\">You can't be here. Please go back.</span>");
	}
	 webContent.append("</div>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<title>Moderator list user</title>
		<link rel="stylesheet" type="text/css" href="css/main.css">
		<script src="http://code.jquery.com/jquery-1.10.2.js"
    		type="text/javascript"></script>
	</head>
	<body>
	<%= webContent  %>
	</body>
</html>