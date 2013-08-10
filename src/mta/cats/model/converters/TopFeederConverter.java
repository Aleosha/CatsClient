package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mta.cats.model.TopFeeder;
import mta.cats.model.User;

public class TopFeederConverter implements IConverter<TopFeeder> {

	private static TopFeederConverter instance = null;
	private TopFeederConverter() {
		
	}
	
	public static TopFeederConverter getInstance() {
		if (instance == null) {
			instance = new TopFeederConverter();
		}
		return instance;
	}
	@Override
	public List<TopFeeder> convert(ResultSet rs) {
		List<TopFeeder> feeders = new ArrayList<TopFeeder>();
		if (rs != null){
			try {
				while (rs.next()) {
					User user = new User();
					TopFeeder feeder = new TopFeeder();
					
					user.setId(rs.getLong("ID"));
					user.setUsername(rs.getString("USERNAME"));
					user.setImage(rs.getString("IMAGE"));
					feeder.setCount(rs.getLong("CNT"));
					feeder.setUser(user);
					feeders.add(feeder);
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<TopFeeder>();
			}
		}
		
		return feeders;
	}

}
