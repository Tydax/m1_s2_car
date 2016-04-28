package car.tp4.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import car.tp4.bean.Book;

@Stateless(name = "LibraryService")
public class LibraryService implements ILibrary {

	/** The list of all the books in the library. */
	private final List<Book> library = new ArrayList<Book>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.car.tp4.service.ILibrary#initBaseBooks()
	 */
	public void initBaseBooks() {
		this.library.add(new Book("Alexandre MOEVI", "Never lucky: The guide", 2015));
		this.library.add(new Book("Alexandre MOEVI", "Guide complet sur Twitch, ou le bottom sans fin", 2010));
		this.library.add(new Book("Samuel HYM", "Atteindre un niveau de conscience supérieur via Haskell", 2008));
		this.library.add(new Book("Armand BOUR", "Devenir végétalien et garder ses amis", 2016));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.car.tp4.service.ILibrary#getListOfAuthors()
	 */
	public List<String> getListOfAuthors() {
		final List<String> authors = new ArrayList<String>();

		for (final Book book : this.library) {
			final String author = book.getAuthor();
			if (!authors.contains(author)) {
				authors.add(author);
			}
		}

		return authors;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.car.tp4.service.ILibrary#addBook(main.java.car.tp4.bean.
	 * BookBean)
	 */
	public void addBook(final Book book) {
		this.library.add(book);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.car.tp4.service.ILibrary#getListOfBooks()
	 */
	public List<Book> getListOfBooks() {
		return new ArrayList<Book>(this.library);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see car.tp4.service.ILibrary#getBookByID(int)
	 */
	@Override
	public Book getBookByID(int id) {
		Book book = null;

		for (final Book b : this.library) {
			if (b.getID() == id) {
				book = b;
				break;
			}
		}

		return book;
	}
}
