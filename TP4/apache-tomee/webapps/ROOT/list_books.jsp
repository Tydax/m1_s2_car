<%@page import="car.tp4.bean.Book"%>
<%@page import="java.util.List"%>
<%@page import="car.tp4.servlet.GetListBooksServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BookCity — Liste des livres</title>
</head>
<body>
	<div id="body">
		<%
			final List<Book> listBooks = (List<Book>) request.getAttribute(GetListBooksServlet.ATTR_LIST_BOOKS);
			if (listBooks == null) { %>
			
			<div>
				<p>Erreur lors de la récupération des livres.</p>
			</div>
		<%		
			} else if (listBooks.isEmpty()) {
		%>
				<p>Aucun livre disponible.</p>
		<%
			} else {
		%>
			<table>
				<tr>
					<th>Titre</th>
					<th>Auteur</th>
					<th>Date de publication</th>
				</tr>
				<%
				for (final Book book : listBooks) {
				%>
					<tr>
						<td><%= book.getTitle() %></td>
						<td><%= book.getAuthor() %></td>
						<td><%= book.getYear() %></td>
					</tr>					
				<%
				}
				%>			
			</table>
		<%
			}
		%>
	</div>
</body>
</html>