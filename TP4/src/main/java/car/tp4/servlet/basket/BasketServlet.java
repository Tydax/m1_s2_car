package car.tp4.servlet.basket;

import java.io.IOException;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import car.tp4.bean.Book;
import car.tp4.service.IBasket;

@WebServlet(name = "BasketServlet", urlPatterns = { "/add_basket", "/remove_basket", "/list_basket" })
public class BasketServlet extends HttpServlet {


	private static final long serialVersionUID = -4515170226417744394L;

	/** Parameter key for book id used to add a book to the basket */
	public static final String PARAM_BASKET_ID = "param_basket_id";
	/** Attribute key for basket content. */
	public static final String ATTR_BASKET_CONTENT = "attr_basket_content";

	private static final String URL_LIST = "/list_basket";
	private static final String URL_REMOVE = "/remove_basket";
	private static final String URL_ADD = "/add_basket";

	private static final String URL_REDIRECT = "/fetch_books";
	private static final String URL_LIST_JSP = "/list_basket.jsp";

	@EJB(name = "BasketService")
	protected IBasket basket;

	protected void redirect(final String url, final HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		getServletContext().getRequestDispatcher(url).forward(request, resp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.servlet.http.HttpServlet#service(javax.servlet.http.
	 * HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void service(final HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		final String path = request.getServletPath();
		
		// Adding or removing item from basket
		if (URL_ADD.equals(path) || URL_REMOVE.equals(path)) {
			final String param_id = request.getParameter(PARAM_BASKET_ID);
			// Check for error
			if (param_id == null) {
				redirect(URL_REDIRECT, request, resp);
				return;
			}
			
			// Checking for parse error
			final Integer id;
			try {
				id = Integer.parseInt(param_id);
			} catch (final NumberFormatException exc) {
				redirect(URL_REDIRECT, request, resp);
				return;
			}
			
			// Adding or removing?
			if (URL_ADD.equals(path)) {
				this.basket.addToBasket(id);
			} else {
				this.basket.removeFromBasket(id);
				redirect(URL_LIST, request, resp);
			}
		} else if (URL_LIST.equals(path)) { // Listing basket
			final Map<Book, Integer> basketContent = this.basket.getBasketContent();
			request.setAttribute(ATTR_BASKET_CONTENT, basketContent);
			redirect(URL_LIST_JSP, request, resp);
			return;
		}

		redirect(URL_REDIRECT, request, resp);
	}
}
