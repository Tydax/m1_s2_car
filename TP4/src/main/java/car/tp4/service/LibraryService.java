package main.java.car.tp4.service;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import main.java.car.tp4.bean.BookBean;

@Stateless(name="LibraryService")
public class LibraryService implements ILibrary {

	/** The list of all the books in the library. */
	private final List<BookBean> library = new ArrayList<BookBean>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.car.tp4.service.ILibrary#initBaseBooks()
	 */
	public void initBaseBooks() {
		this.library.add(new BookBean("Alexandre MOEVI", "Never lucky: The guide", 2015));
		this.library.add(new BookBean("Alexandre MOEVI", "Guide complet sur Twitch, ou le bottom sans fin", 2010));
		this.library.add(new BookBean("Samuel HYM", "Atteindre un niveau de conscience supérieur via Haskell", 2008));
		this.library.add(new BookBean("Armand BOUR", "Devenir végétalien et garder ses amis", 2016));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see main.java.car.tp4.service.ILibrary#getListOfAuthors()
	 */
	public List<String> getListOfAuthors() {
		final List<String> authors = new ArrayList<String>();
		
		for (final BookBean book : this.library) {
			final String author = book.getAuthor();
			if (!authors.contains(author)) {
				authors.add(author);
			}
		}
		
		return authors;
	}

}
