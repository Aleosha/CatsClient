package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mta.cats.model.Cat;

public class CatConverter implements IConverter<Cat> {

	static CatConverter instance;
	
	private CatConverter(){
	}
	
	
	/**
	 * This method doesn't throw exceptions, and will return empty array 
	 */
	@Override
	public List<Cat> convert(ResultSet rs) {
		List<Cat> cats = new ArrayList<Cat>();
		if (rs != null){
			try {
				while (rs.next()) {
					Cat cat = new Cat();
					
					cat.setId(rs.getLong("ID"));
					cat.setNickname(rs.getString("NICKNAME"));
					cat.setImage(rs.getString("IMAGE"));
					cat.setLocationX(rs.getFloat("LOCATION_X"));
					cat.setLocationY(rs.getFloat("LOCATION_Y"));
					cat.setCreationDate(rs.getDate("CREATION_DATE"));
					cats.add(cat);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<Cat>();
			}
		}
		
		return cats;
	}

	
	/**
	 * The most naive way to get singleton
	 */
	public static IConverter<Cat> getInstance() {
		if (instance == null) {
			instance = new CatConverter();
		}
	
		return instance;
	}

}
