<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>BookCity — Saisir un nouveau livre</title>
</head>
<body>
	<%
		String author = request.getParameter("author");
		String title = "";
		String year = "";
		if (author != null) {
			title = request.getParameter("title");
			year = request.getParameter("year");
			out.print("Auteur : " + author + "<br/>");
			out.print("Titre : " + title + "<br/>");
			out.print("Ann&eacute; : " + year + "<br/><p/>");
		} else {
			author = "";
		}
	%>
	<div id="body">
		<form action="form.jsp">
			<table>
				<tr>
					<td>Auteur :</td>
					<td><input type="text" name="author" value="<%=author%>"></td>
				</tr>
				<tr>
					<td>Titre :</td>
					<td><input type="text" name="title" value="<%=title%>" /></td>
				</tr>
				<tr>
					<td>Année :</td>
					<td><input type="text" name="year" value="<%=year%>" /></td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" /></td>
				</tr>
			</table>
		</form>
	</div>
</body>
</html>
