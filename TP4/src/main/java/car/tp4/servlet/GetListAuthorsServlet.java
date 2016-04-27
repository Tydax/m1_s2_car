package car.tp4.servlet;

import java.io.IOException;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import car.tp4.service.ILibrary;

/**
 * Gets the list of authors in the library.
 */
@WebServlet(
	name = "GetListAuthorsServlet",
	urlPatterns = {"/fetch_authors"}
)
public class GetListAuthorsServlet extends HttpServlet {

	private static final long serialVersionUID = 4909686845829712653L;

	private static final String ATTR_LIST_AUTHORS = "attr_list_authors";

	@EJB(name = "LibraryService")
	private ILibrary library;

	@Override
	public void init() throws ServletException {
		// TODO Auto-generated method stub
		super.init();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse response) throws ServletException, IOException {
		final List<String> listAuthors = library.getListOfAuthors();
		req.setAttribute(ATTR_LIST_AUTHORS, listAuthors);
	}
}
