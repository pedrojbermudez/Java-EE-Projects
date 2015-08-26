<%@page import="java.util.ArrayList"%>
<%@page import="com.family.accounting.libraries.Database"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="com.family.accounting.libraries.Utils"%>
<%
	Database db = new Database((new Utils()).getDatabasePath());
	ArrayList<String[]> sources = db
			.getSourcesComboBox(Integer.parseInt(session.getAttribute("userId").toString()));
	ArrayList<String[]> categories = db
			.getCategories(Integer.parseInt(session.getAttribute("userId").toString()));
	StringBuilder dateInput = new StringBuilder("<div>Month: <select id=\"month\" name=\"month\">");
	StringBuilder sourceInput = new StringBuilder(
			"Select a money source: <select id=\"sourceId\" name=\"sourceId\">");
	StringBuilder categoryInput = new StringBuilder(
			"Select a category: <select id=\"categoryId\" name=\"categoryId\">");
	int mode = Integer.parseInt(request.getParameter("m"));
	String title;
	String[] movement;
	String name = "";
	String movValue = "";
	String sourceId = "";
	String categoryId = "";
	String oldIncome = "";
	String oldOutgoing = "";
	if (mode == 0) {
		// Mading dateInput
		for (int i = 1; i <= 12; i++) {
			if (i < 10) {
				dateInput.append("<option value=\"0" + i + "\">0" + i + "</option>");
			} else {
				dateInput.append("<option value=\"" + i + "\">" + i + "</option>");
			}

		}
		dateInput.append("</select> Day: <select id=\"day\" name=\"day\">");
		for (int i = 1; i <= 31; i++) {
			if (i < 10) {
				dateInput.append("<option value=\"0" + i + "\">0" + i + "</option>");
			} else {
				dateInput.append("<option value=\"" + i + "\">" + i + "</option>");
			}
		}
		dateInput.append("</select> Year: <select id=\"year\" name=\"year\">");
		for (int i = 1980; i <= 2050; i++) {
			dateInput.append("<option value=\"" + i + "\">" + i + "</option>");
		}
		dateInput.append("</select></div>");
		// Finishing dateInput

		// Mading sourceInput
		for (int i = 0; i < sources.size(); i++) {
			String[] tmp = sources.get(i);
			sourceInput.append("<option value=\"" + tmp[0] + "\">" + tmp[1] + "</option>");
		}
		sourceInput.append("</select>");
		// Finishing sourceInput

		// Mading categoryInput
		for (int i = 0; i < categories.size(); i++) {
			String[] tmp = categories.get(i);
			categoryInput.append("<option value=\"" + tmp[0] + "\">" + tmp[1] + "</option>");
		}
		categoryInput.append("</select>");
		// Finishing categoryInput
		title = "New Movement";
	} else {
		/*
		 String[6] = name[0], movement_date[1](dd/mm/yyyy) or (dd-mm-yyyy), income[2], 
		 outgoing[3], source_id[4], category_id[5]
		*/
		int movId = Integer.parseInt(request.getParameter("mid"));
		int userId = Integer.parseInt(session.getAttribute("userId").toString());
		movement = db.getMovement(movId, userId);
		name = movement[0];
		movValue = Double.parseDouble(movement[2]) > 0 ? movement[2] : "-" + movement[3];
		sourceId = movement[4];
		categoryId = movement[5];
		oldIncome = movement[2];
		oldOutgoing = movement[3];
		// Mading dateInput
		String[] date = movement[1].split("/");
		int year = Integer.parseInt(date[2]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[0]);
		for (int i = 1; i <= 12; i++) {
			if (i == month) {
				if (i < 10) {
					dateInput.append("<option value=\"0" + i + "\" selected>0" + i + "</option>");
				} else {
					dateInput.append("<option value=\"" + i + "\" selected>" + i + "</option>");
				}
			} else {
				if (i < 10) {
					dateInput.append("<option value=\"0" + i + "\">0" + i + "</option>");
				} else {
					dateInput.append("<option value=\"" + i + "\">" + i + "</option>");
				}
			}
		}
		dateInput.append("</select> Day: <select id=\"day\" name=\"day\">");
		for (int i = 1; i <= 31; i++) {
			if (i == day) {
				if (i < 10) {
					dateInput.append("<option value=\"0" + i + "\" selected>0" + i + "</option>");
				} else {
					dateInput.append("<option value=\"" + i + "\" selected>" + i + "</option>");
				}
			} else {
				if (i < 10) {
					dateInput.append("<option value=\"0" + i + "\">0" + i + "</option>");
				} else {
					dateInput.append("<option value=\"" + i + "\">" + i + "</option>");
				}
			}
		}
		dateInput.append("</select> Year: <select id=\"year\" name=\"year\">");
		for (int i = 1980; i <= 2050; i++) {
			if (i == year) {
				dateInput.append("<option value=\"" + i + "\" selected>" + i + "</option>");
			} else {
				dateInput.append("<option value=\"" + i + "\">" + i + "</option>");
			}
		}
		dateInput.append("</select></div>");
		// Finishing dateInput

		// Mading sourceInput
		for (int i = 0; i < sources.size(); i++) {
			String[] tmp = sources.get(i);
			if (tmp[0].equals(sourceId)) {
				sourceInput.append("<option value=\"" + tmp[0] + "\" selected>" + tmp[1] + "</option>");
			} else {
				sourceInput.append("<option value=\"" + tmp[0] + "\">" + tmp[1] + "</option>");
			}
		}
		sourceInput.append("</select>");
		// Finishing sourceInput

		// Mading categoryInput
		for (int i = 0; i < categories.size(); i++) {
			String[] tmp = categories.get(i);
			if (tmp[0].equals(categoryId)) {
				categoryInput.append("<option value=\"" + tmp[0] + "\" selected>" + tmp[1] + "</option>");
			} else {
				categoryInput.append("<option value=\"" + tmp[0] + "\">" + tmp[1] + "</option>");
			}
		}
		categoryInput.append("</select>");
		// Finishing categoryInput
		title = "Edit ";
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="resources/css/general.css">
<title><%=title%></title>
<script type="text/javascript" src="resources/js/general.js"></script>
</head>
<body>
	<div id="div_body">
		<%=new Utils().menu(-1, request)%>
		<%
			if (mode == 0) {
		%>

		<form action="movements" method="post">
			<input type="hidden" name="mode" value="0"> Name: <input
				type="text" name="name"><br><%=dateInput.toString()%>
			Movement value (+number=income, -number=outgoing): <input type="text"
				id="movValue" name="movValue" oninput="isNumber(this)"><br>
			<%=sourceInput.toString()%><br>
			<%=categoryInput.toString()%><br> <input type="submit"
				value="Create" disabled="disabled" id="buttonSE">
		</form>

		<%
			} else {
		%>
		<form action="movements" method="post">
			<input type="hidden" name="mode" value="1"> <input
				type="hidden" name="oldIncome" value="<%=oldIncome%>">
			<input type="hidden" name="oldOutgoing" value="<%=oldOutgoing%>"> <input
				type="hidden" name="movId" value="<%=request.getParameter("mid")%>">
			Name: <input type="text" name="name" value="<%=name%>"><br><%=dateInput.toString()%>
			Movement value (+number=income, -number=outgoing): <input type="text"
				id="movValue" name="movValue" oninput="isNumber(this)"
				value="<%=movValue%>"><br>
			<%=sourceInput.toString()%><br>
			<%=categoryInput.toString()%><br> <input type="submit"
				value="Create" disabled="disabled" id="buttonSE">
		</form>
		<%
			}
		%>
	</div>
</body>
</html>