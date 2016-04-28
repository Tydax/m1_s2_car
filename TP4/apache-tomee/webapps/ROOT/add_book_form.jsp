<%@page import="car.tp4.servlet.AddBookServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BookCity — Saisir un nouveau livre</title>
</head>
<body>
	<%
		String author = request.getParameter(AddBookServlet.PARAM_BOOK_AUTHOR);
		String title = request.getParameter(AddBookServlet.PARAM_BOOK_TITLE);
		String year = request.getParameter(AddBookServlet.PARAM_BOOK_YEAR);
		
		if (author == null) {
			author = "";
		}
		
		if (title == null) {
			title = "";
		}
		
		if (year == null) {
			year = "";
		}
		
		// Checking for error
		final String errMsg = (String) request.getAttribute(AddBookServlet.ATTR_ERROR);
		if (errMsg != null) { %>
			<div class="error">
				<p><%= errMsg %></p>
			</div>
	<%
		}
	%>
	<div id="body">
		<form action="/add_book" method="POST">
			<table>
				<tr>
					<td>Auteur :</td>
					<td><input type="text" name="<%= AddBookServlet.PARAM_BOOK_AUTHOR %>" value="<%=author%>"></td>
				</tr>
				<tr>
					<td>Titre :</td>
					<td><input type="text" name="<%= AddBookServlet.PARAM_BOOK_TITLE %>" value="<%=title%>" /></td>
				</tr>
				<tr>
					<td>Année :</td>
					<td><input type="text" name="<%= AddBookServlet.PARAM_BOOK_YEAR %>" value="<%=year%>" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
