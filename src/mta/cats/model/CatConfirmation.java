package mta.cats.model;

import java.sql.Date;

/**
 * 
 * @author i064039
 *
 */
public class CatConfirmation implements IModel {

	Long id;
	Long catId;
	Long userId;
	Date confirmationDate;
	
	@Override
	public Long getId() {
		return this.id;
	}
	
	public Long getCatId() {
		return catId;
	}
	public void setCatId(Long catId) {
		this.catId = catId;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Date getConfirmationDate() {
		return confirmationDate;
	}
	public void setConfirmationDate(Date confirmationDate) {
		this.confirmationDate = confirmationDate;
	}
	public void setId(Long id) {
		this.id = id;
	}

	public void setCatId(Cat cat) {
		if (cat != null)
			this.catId = cat.getId();
	}

	public void setUserId(User user) {
		if (user != null)
			this.userId = user.getId();
	}
}
