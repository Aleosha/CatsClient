package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mta.cats.model.Feeder;
import mta.cats.model.User;

public class FeederConverter implements IConverter<Feeder> {
	static FeederConverter instance;
	
	private FeederConverter(){
	}
	
	
	/**
	 * This method doesn't throw exceptions, and will return empty array 
	 */
	@Override
	public List<Feeder> convert(ResultSet rs) {
		List<Feeder> feeders = new ArrayList<Feeder>();
		if (rs != null){
			try {
				while (rs.next()) {
					Feeder feeder = new Feeder();
					
					feeder.setId(rs.getLong("ID"));
					feeder.setCatId(rs.getLong("CAT_ID"));
					feeder.setUserId(rs.getLong("USER_ID"));
					feeder.setDayOfWeek(rs.getInt("DAY_OF_WEEK"));
					feeders.add(feeder);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<Feeder>();
			}
		}
		
		return feeders;
	}

	
	/**
	 * The most naive way to get singleton
	 */
	public static IConverter<Feeder> getInstance() {
		if (instance == null) {
			instance = new FeederConverter();
		}
		return instance;
	}

}
