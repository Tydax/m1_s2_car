package car.tp4.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import car.tp4.bean.Book;
import car.tp4.service.ILibrary;

@WebServlet(
	name = "GetListBooksServlet",
	urlPatterns = {"/fetch_books"}
)
public class GetListBooksServlet extends HttpServlet {


	/** Attribute key for list of books. */
	public static final String ATTR_LIST_BOOKS = "ATTR_LIST_BOOKS";

	private static final long serialVersionUID = 2975847389861520764L;
	private static final String URL_FORWARD = "/list_books.jsp";
	private static final String DEBUG_MSG = "[DEBUG][GetListBooksServlet] Fetching list of books containing:";

	@EJB(name = "LibraryService")
	private ILibrary library;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(final HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		final List<Book> listBooks = this.library.getListOfBooks();
		
		// Debug message
		System.out.println(DEBUG_MSG);
		for (final Book book : listBooks) {
			System.out.println(book);
		}
		
		request.setAttribute(ATTR_LIST_BOOKS, listBooks);
		getServletContext().getRequestDispatcher(URL_FORWARD).forward(request, resp);
	}
}
