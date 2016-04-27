package car.tp4.service;

import java.util.List;

import javax.ejb.Local;

import car.tp4.bean.Book;

@Local
public interface ILibrary {

	/**
	 * Initialises a list of base books.
	 */
	void initBaseBooks();

	/**
	 * Gets the list of all the registered authors in the base.
	 * 
	 * @return a list containing all the authors.
	 */
	List<String> getListOfAuthors();
	
	/**
	 * Adds a book to the library.
	 * @param book The book to add.
	 */
	void addBook(final Book book);
	
	/**
	 * Gets the list of books registered in the library.
	 * @return The list of books registered.
	 */
	List<Book> getListOfBooks();
}
