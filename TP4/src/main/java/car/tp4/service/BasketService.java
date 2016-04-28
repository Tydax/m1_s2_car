package car.tp4.service;

import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import car.tp4.bean.Book;

@Stateful(name = "BasketServive")
public class BasketService implements IBasket {

	@EJB(name = "LibraryService")
	private ILibrary library;
	
	/** The basket containing the book associated with a quantity. */
	private final Map<Book, Integer> basket = new HashMap<Book, Integer>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#addToBasket(car.tp4.bean.Book)
	 */
	@Override
	public void addToBasket(int id) {
		final Book book = this.library.getBookByID(id);
		
		if (book != null) {
			final int qty = this.basket.get(book) != null
						  ? this.basket.get(book)
						  : 0;
			this.basket.put(book, qty + 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#removeFromBasket(car.tp4.bean.Book)
	 */
	@Override
	public void removeFromBasket(final int id) {
		final Book book = this.library.getBookByID(id);
		
		if (book != null) {
			final Integer qty = this.basket.get(book);
			if (qty != null) {
				this.basket.put(book, qty - 1);
				this.basket.remove(id, 0); // Remove only if equals 0;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#validateBasket()
	 */
	@Override
	public void validateBasket() {
		// TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#getBasketContent()
	 */
	@Override
	public Map<Book, Integer> getBasketContent() {
		return new HashMap<Book, Integer>(this.basket);
	}

}
