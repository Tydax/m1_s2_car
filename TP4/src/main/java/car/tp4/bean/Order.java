package car.tp4.bean;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.Id;

public class Order {

	protected static int generated_id = 0;
	
	/** The id of the book. */
	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
	protected int id;
	
	protected Map<Book, Integer> content;

	public Order(final Map<Book, Integer> basket) {
		this.id = generated_id++;
		this.content = new HashMap<Book, Integer>(basket);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Map<Book, Integer> getContent() {
		return new HashMap<Book, Integer>(content);
	}	
}
