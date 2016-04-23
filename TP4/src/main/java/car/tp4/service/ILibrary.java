package main.java.car.tp4.service;

import java.util.List;

import javax.ejb.Local;

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
}
