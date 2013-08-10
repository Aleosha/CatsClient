package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mta.cats.model.User;

/**
 * Convert result set to list of user entities
 * @author i064039
 *
 */
public class UserConverter implements IConverter<User>  {

	private UserConverter() {
		
	}
	
	private static UserConverter instance = null;
	
	public static UserConverter getInstance() {
		if (instance == null) {
			instance = new UserConverter();
		}
		
		return instance;
	}
	
	@Override
	public List<User> convert(ResultSet rs) {
		List<User> users = new ArrayList<User>();
		if (rs != null){
			try {
				while (rs.next()) {
					User user = new User();
					
					user.setId(rs.getLong("ID"));
					user.setUsername(rs.getString("USERNAME"));
					user.setImage(rs.getString("IMAGE"));
					user.setLocationX(rs.getFloat("LOCATION_X"));
					user.setLocationY(rs.getFloat("LOCATION_Y"));
					user.setPassword(rs.getString("PASSWORD"));
					users.add(user);
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<User>();
			}
		}
		
		return users;
	}

}
