package mta.cats.model;

/**
 * Represents result of feeder top - count of feeded cats and the feeder details
 *
 */
public class TopFeeder {

	private Long count;
	private User user;
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
}
