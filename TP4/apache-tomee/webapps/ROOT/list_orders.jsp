<%@page import="car.tp4.servlet.BasketServlet"%>
<%@page import="car.tp4.bean.Order"%>
<%@page import="car.tp4.servlet.OrdersServlet"%>
<%@page import="car.tp4.bean.Book"%>
<%@page import="java.util.List"%>
<%@page import="car.tp4.servlet.GetListBooksServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BookCity — Liste des commandes</title>
<link href="style.css" rel="stylesheet" type="text/css" >
</head>
<body>
	<nav>
		<ul id="menu">
			<li><a href="/fetch_books">Liste des livres</a></li>
			<li><a href="/add_book">Ajouter un livre</a></li>
			<li><a href="/list_basket">Panier</a></li>
			<li><a href="/fetch_orders">Liste des commandes</a></li>
		</ul>
	</nav>

	<div id="body">
		<h1>Liste des commandes</h1>
		<%
			final List<Order> listOrders = (List<Order>) request.getAttribute(OrdersServlet.ATTR_LIST_ORDERS);
			if (listOrders == null) { %>
			
			<div>
				<p>Erreur lors de la récupération des commandes.</p>
			</div>
		<%		
			} else if (listOrders.isEmpty()) {
		%>
				<p>Aucune commande enregistrée.</p>
		<%
			} else {
		%>
			<table>
				<tr>
					<th>ID</th>
					<th>Contenu</th>
				</tr>
				<%
				for (final Order order : listOrders) {
				%>
					<tr>
						<td><%= order.getId() %></td>
						<td>
							<ul>
								<%
								for (final Book book : order.getContent().keySet()) {
								%>
									<li>“<%= book.getTitle() %>” (<%= book.getYear() %>) de <%= book.getAuthor() %> x <%= order.getContent().get(book) %></li>
								<%
								}
								%>
							</ul>
						</td>
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