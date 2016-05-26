<%@ page import="utils.GetterUser"%>
<%@ page import="utils.GetterCategory"%>
<%@ page import="utils.MenuFooter"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
	session = request.getSession();
	String title;
	StringBuilder webContent = new StringBuilder();
	GetterCategory category = new GetterCategory();
	GetterUser user = new GetterUser();
	MenuFooter menuFooter = new MenuFooter();
	webContent.append(menuFooter.getMenu(session) + "<br><div id=\"div_body\"><div id=\"div_content\">");
	if (session.getAttribute("id") != null && session.getAttribute("id").toString().matches("^\\d+$")
			&& Integer.parseInt(session.getAttribute("id").toString()) == 1) {
		String name;
		String category_id;
		String description;
		String submit;
		String action;
		if (request.getParameter("cid") != null && request.getParameter("cid").matches("^\\d+$")) {
			category = new GetterCategory();
			String[] cat = category.getCategoryWEB(Integer.parseInt(request.getParameter("cid")));
			name = cat[0];
			description = cat[1];
			title = "Edit " + name;
			submit = "Edit";
			action = "/Forum/edit-category";
			category_id = "<input type=\"hidden\" name=\"category_id\" value=\"" + request.getParameter("cid")
					+ "\">";
		} else {
			title = "New Category";
			name = "";
			category_id = "";
			description = "";
			action = "/Forum/new-category";
			submit = "New Category";
		}
		webContent.append("<form action=\"" + action + "/\" method=\"POST\">" + category_id
				+ "Name:<br><input type=\"text\" value=\"" + name + "\" required name=\"category_name\""
				+ " placeholder=\"Introduce a name for the new category\"><br>"
				+ "Description:<br><textarea rows=\"4\" cols=\"50\" type=\"text\" "
				+ "name=\"category_description\" "
				+"placeholder=\"Enter the description of the category...\">" + description
				+ "</textarea><br><br><input type=\"submit\" value=\"" + submit + "\"></form>");
	} else {
		title = "Wrong place";
		webContent.append("<span id=\"span_error\">You cannot be here.</span>");
	}
	webContent.append("</div></div><br>" + menuFooter.getFooter());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title></title>
<link rel="stylesheet" type="text/css" href="css/new_edit_cat.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
</head>
<body>
	<%=webContent%>
</body>
</html>