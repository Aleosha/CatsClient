package mta.cats.model;

import java.sql.Date;

public class User implements IModel {

	private Long id;
	private String username;
	private String password;
	private Float locationX;
	private Float locationY;
	private String image;
	private Date creationDate;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	public User() {
		
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password="
				+ password + ", locationX=" + locationX + ", locationY="
				+ locationY + ", image=" + image + ", creationDate="
				+ creationDate + "]";
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Float getLocationX() {
		return locationX;
	}
	public void setLocationX(Float locationX) {
		this.locationX = locationX;
	}
	public Float getLocationY() {
		return locationY;
	}
	public void setLocationY(Float locationY) {
		this.locationY = locationY;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public Date getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
