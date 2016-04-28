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
	public void add(final int id) {
		final Book book = this.library.getBookByID(id);

		if (book != null) {
			final int qty = this.basket.get(book) != null ? this.basket.get(book) : 0;
			this.basket.put(book, qty + 1);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#removeFromBasket(car.tp4.bean.Book)
	 */
	@Override
	public void remove(final int id) {
		final Book book = this.library.getBookByID(id);

		if (book != null) {
			final Integer qty = this.basket.get(book);
			if (qty != null) {
				// Remove if no quantity is left
				if (qty == 1) {
					this.basket.remove(book);
				} else {
					this.basket.put(book, qty - 1);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#validateBasket()
	 */
	@Override
	public void validate() {
		// TODO
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#getBasketContent()
	 */
	@Override
	public Map<Book, Integer> getContent() {
		return new HashMap<Book, Integer>(this.basket);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.IBasket#emptyBasket()
	 */
	@Override
	public void empty() {
		this.basket.clear();
	}

}
