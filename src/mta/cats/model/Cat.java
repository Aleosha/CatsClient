package mta.cats.model;

import java.sql.Date;

public class Cat implements IModel {

	@Override
	public String toString() {
		return "Cat [nickname=" + nickname + ", locationX=" + locationX
				+ ", id=" + id + ", creationDate=" + creationDate
				+ ", locationY=" + locationY + "]";
	}

	private String nickname;
	private Float locationX;
	private Long id;
	private Date creationDate;
	private Float locationY;
	private String image;

	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Cat() {
		
	}
	
	public Cat(String nickname, Float x, Float y) {
		this.nickname = nickname;
		this.locationX = x;
		this.locationY = y;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result
				+ ((locationX == null) ? 0 : locationX.hashCode());
		result = prime * result
				+ ((locationY == null) ? 0 : locationY.hashCode());
		result = prime * result
				+ ((nickname == null) ? 0 : nickname.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cat other = (Cat) obj;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (locationX == null) {
			if (other.locationX != null)
				return false;
		} else if (!locationX.equals(other.locationX))
			return false;
		if (locationY == null) {
			if (other.locationY != null)
				return false;
		} else if (!locationY.equals(other.locationY))
			return false;
		if (nickname == null) {
			if (other.nickname != null)
				return false;
		} else if (!nickname.equals(other.nickname))
			return false;
		return true;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Float getLocationX() {
		return locationX;
	}

	public void setLocationX(Float locationX) {
		this.locationX = locationX;
	}

	/**
	 * Allows setting ID only if it wasn't set already
	 * @param id
	 */
	public void setId(Long id) {
		if (this.id == null) {
			this.id = id;
		}
	}
	
	public Long getId() {
		return this.id;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setLocationY(float locationY) {
		this.locationY = locationY;
	}

	public Float getLocationY() {
		return this.locationY;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

}
