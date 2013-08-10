package mta.cats.model;

/**
 * Row of DB table
 * @author i064039
 *
 */
public interface IModel {

	/**
	 * Every model must have a unique primary key in our system
	 * @return
	 */
	Long getId();
}
