package car.tp4.servlet;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import car.tp4.bean.Book;
import car.tp4.service.ILibrary;

@WebServlet(
	name = "AddBookServlet",
	urlPatterns = {"/add_book"}
)
public class AddBookServlet extends HttpServlet {

	private static final long serialVersionUID = -4017177679459672051L;

	/** Parameter key for author. */
	public static final String PARAM_BOOK_AUTHOR = "param_book_author";
	/** Parameter key for title. */
	public static final String PARAM_BOOK_TITLE = "param_book_title";
	/** Parameter key for publication year. */
	public static final String PARAM_BOOK_YEAR = "param_book_year";
	/** Attribute key for error. */
	public static final String ATTR_ERROR = "attr_error";
	
	private static final String MSG_EMPTY_INPUT = "Un champ est vide.";
	private static final String MSG_NUMBER_FORMAT = "L’année n’est pas un nombre.";
	private static final String DEBUG_MSG = "[DEBUG][AddBookServlet] Added book '%' to library.";
	
	@EJB(name = "LibraryService")
	private ILibrary library;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse resp)
			throws ServletException, IOException {
		final String param_author = request.getParameter(PARAM_BOOK_AUTHOR);
		final String param_title = request.getParameter(PARAM_BOOK_TITLE);
		final String param_year = request.getParameter(PARAM_BOOK_YEAR);
		
		// Checking that form input is not empty 
		if (param_author.isEmpty() || param_title.isEmpty() || param_year.isEmpty()) {
			request.setAttribute(ATTR_ERROR, MSG_EMPTY_INPUT);
			getServletContext().getRequestDispatcher("/add_book_form.jsp").forward(request, resp);
			return;
		}
		
		// Watch out for error when parsing number
		final int year;
		try {
			year = Integer.parseInt(param_year);
		} catch (final NumberFormatException exc) {
			request.setAttribute(ATTR_ERROR, MSG_NUMBER_FORMAT);
			getServletContext().getRequestDispatcher("/add_book_form.jsp").forward(request, resp);
			return;
		}
		
		// Adding book
		final Book book = new Book(param_author, param_title, year);
		this.library.addBook(book);
		System.out.println(String.format(DEBUG_MSG, book));
		
		getServletContext().getRequestDispatcher("/index.jsp").forward(request, resp);
	}
}
