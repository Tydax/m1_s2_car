package car.tp4.service;

import java.util.Map;

import javax.ejb.Local;

import car.tp4.bean.Book;

@Local
public interface IBasket {

	/**
	 * Adds a book to the basket. Adds one to the quantity if it is already
	 * there.
	 * @param id
	 */
	void addToBasket(final int id);

	/**
	 * Takes one from the quantity of the specified book. Removes the book from
	 * the basket if the quantity was 1.
	 * 
	 * @param id
	 *            The book to remove.
	 */
	void removeFromBasket(final int id);

	/**
	 * Validates the baskets and register an order.
	 */
	void validateBasket();
	

	/**
	 * Gets the content of the basket, books associated with their quantity.
	 * 
	 * @return
	 */
	Map<Book, Integer> getBasketContent();
}
