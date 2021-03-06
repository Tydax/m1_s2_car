<%@page import="car.tp4.servlet.BasketServlet"%>
<%@page import="java.util.Map"%>
<%@page import="car.tp4.servlet.BasketServlet"%>
<%@page import="car.tp4.bean.Book"%>
<%@page import="java.util.List"%>
<%@page import="car.tp4.servlet.GetListBooksServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>BookCity — Panier</title>
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
		<h1>Contenu du panier</h1>
		<%
			final Map<Book, Integer> basket = (Map<Book, Integer>) request.getAttribute(BasketServlet.ATTR_BASKET_CONTENT);
			if (basket == null) { %>
			
			<div>
				<p>Erreur lors de la récupération du contenu du panier.</p>
			</div>
		<%		
			} else if (basket.isEmpty()) {
		%>
				<p>Le panier est vide.</p>
		<%
			} else {
		%>
			<table>
				<tr>
					<th>Titre</th>
					<th>Auteur</th>
					<th>Date de publication</th>
					<th>Quantité</th>
				</tr>
				<%
				for (final Book book : basket.keySet()) {
				%>
					<tr>
						<td><%= book.getTitle() %></td>
						<td><%= book.getAuthor() %></td>
						<td><%= book.getYear() %></td>
						<td><%= basket.get(book) %></td>
						<td><a class="butt" href="/add_basket?<%= BasketServlet.PARAM_BASKET_ID %>=<%= book.getID() %>">J’en veux plus !</a></td>
						<td><a class="butt" href="/remove_basket?<%= BasketServlet.PARAM_BASKET_ID %>=<%= book.getID() %>">J’en veux moins !</a></td>
					</tr>					
				<%
				}
				%>			
			</table>
		<%
			}
		%>
		
		<a class="butt" href="/valid_basket">Valider le panier</a>
		<a class="butt" href="/empty_basket">Vider le panier</a>
	</div>
</body>
</html>