package car.tp4.servlet;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import car.tp4.bean.Book;
import car.tp4.bean.Order;
import car.tp4.service.IOrders;

@WebServlet(
	name = "OrdersServlet",
	urlPatterns = { OrdersServlet.URL_FETCH, OrdersServlet.URL_ADD }
)
public class OrdersServlet extends HttpServlet {


	public static final String ATTR_LIST_ORDERS = "attr_list_orders";

	public static final String URL_FETCH = "/fetch_orders";
	public static final String URL_ADD = "/add_order";

	private static final long serialVersionUID = 753621345127662404L;
	private static final String LIST_ORDERS_JSP = "/list_orders.jsp";

	@EJB(name = "OrderService")
	private IOrders orderService;
	
	protected void redirect(final String url, final HttpServletRequest request, HttpServletResponse resp)
			throws ServletException, IOException {
		getServletContext().getRequestDispatcher(url).forward(request, resp);
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
		final String path = request.getServletPath();
		
		if (URL_ADD.equals(path)) {
			// Add a new order
			@SuppressWarnings("unchecked")
			final Map<Book, Integer> basket = (Map<Book, Integer>) request.getAttribute(BasketServlet.ATTR_BASKET_CONTENT);
			
			if (basket != null) {
				this.orderService.addOrder(basket);
			}
			
			redirect(BasketServlet.URL_LIST, request, resp);
			return;
		} else if (URL_FETCH.equals(path)) {
			final List<Order> orders = this.orderService.getAllOrders();
			request.setAttribute(ATTR_LIST_ORDERS, orders);
			redirect(LIST_ORDERS_JSP, request, resp);
		}
	}
}
