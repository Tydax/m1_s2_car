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

/**
 * Inits the library with a few predefined books.
 */
@WebServlet(
	name = "InitLibraryServlet",
	urlPatterns = { "/init_library" }
)
public class InitLibraryServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String DEBUG_MSG = "[DEBUG][InitLibraryServlet] Initialised library with base books: ";

	@EJB(name = "LibraryService")
	private ILibrary library;

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doGet(final HttpServletRequest req, final HttpServletResponse resp)
			throws ServletException, IOException {
		library.initBaseBooks();
		System.out.println(DEBUG_MSG);
		for (final Book book : library.getListOfBooks()) {
			System.out.println(book);
		}
	}
}
