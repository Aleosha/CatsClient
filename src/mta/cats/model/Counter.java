package mta.cats.model;

/**
 * This model represents basic count, displayed as XYZ:123
 * Used to display GROUP BY results
 */
public class Counter {
	private String name;
	private Long count;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
}
