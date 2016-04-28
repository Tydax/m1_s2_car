package car.tp4.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import car.tp4.bean.Book;
import car.tp4.bean.Order;

@Stateless(name = "OrderService")
public class OrderService implements IOrders {

	private final List<Order> orders = new ArrayList<Order>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IOrders#getAllOrders()
	 */
	@Override
	public List<Order> getAllOrders() {
		return new ArrayList<Order>(orders);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IOrders#addOrder(java.util.Map)
	 */
	@Override
	public void addOrder(final Map<Book, Integer> basket) {
		this.orders.add(new Order(basket));
	}
}
