package car.tp4.service;

import java.util.List;
import java.util.Map;

import javax.ejb.Local;

import car.tp4.bean.Book;
import car.tp4.bean.Order;

@Local
public interface IOrders {
	
	/**
	 * Gets all orders.
	 * 
	 * @return
	 */
	List<Order> getAllOrders();

	/**
	 * Adds a new basket as an order in the base.
	 * 
	 * @param order
	 */
	void addOrder(final Map<Book, Integer> basket);
}
