package car.tp4.bean;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * BookBean stores the information concerning a book.
 */
@Entity
public class Book {

	protected static int generated_id = 0;

	/** The id of the book. */
	@Id
	// @GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;
	/** The author of the book. */
	protected String author;
	/** The title of the book. */
	protected String title;
	/** The year of publication of the book. */
	protected int year;

	/**
	 * Constructor generating a random id.
	 */
	public Book() {
	}

	public Book(final String author, final String title, final int year) {
		this.id = generated_id++;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final String format = "“%s”, %s, %d";
		return String.format(format, this.title, this.author, this.year);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Book other = (Book) obj;
		if (id != other.id)
			return false;
		return true;
	}

}
