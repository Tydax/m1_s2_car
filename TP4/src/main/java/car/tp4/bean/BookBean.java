package main.java.car.tp4.bean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * BookBean stores the information concerning a book.
 */
@Entity
public class BookBean {

	/** The id of the book. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	/** The author of the book. */
	private String author;
	/** The title of the book. */
	private String title;
	/** The year of publication of the book. */
	private int year;

	/**
	 * Constructor generating a random id.
	 */
	public BookBean() {
		this("", "", 0);
	}

	public BookBean(final String author, final String title, final int year) {
		this.author = author;
		this.title = title;
		this.year = year;
	}

	/**
	 * Gets the generated id of the book.
	 * 
	 * @return
	 */
	public int getID() {
		return this.id;
	}

	/**
	 * Gets the author of the book.
	 * 
	 * @return the author.
	 */
	public String getAuthor() {
		return this.author;
	}

	/**
	 * Gets the title of the book.
	 * 
	 * @return the title.
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Gets the year of publication of the book.
	 * 
	 * @return the year of publication.
	 */
	public int getYear() {
		return this.year;
	}

	/**
	 * Sets the author of the book.
	 * 
	 * @param author
	 */
	public void setAuthor(final String author) {
		this.author = author;
	}

	/**
	 * Sets the title of the book.
	 * 
	 * @param title
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/**
	 * Sets the year of publication of the book.
	 * 
	 * @param year
	 */
	public void setYear(final int year) {
		this.year = year;
	}
}
