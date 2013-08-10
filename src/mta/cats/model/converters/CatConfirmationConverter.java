package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import mta.cats.model.CatConfirmation;

public class CatConfirmationConverter implements IConverter<CatConfirmation> {

	private static CatConfirmationConverter instance = null;
	
	private CatConfirmationConverter() {
		
	}
	
	
	@Override
	public List<CatConfirmation> convert(ResultSet rs) {
		List<CatConfirmation> catConfirmations = new ArrayList<CatConfirmation>();
		if (rs != null){
			try {
				while (rs.next()) {
					CatConfirmation catConfirmation = new CatConfirmation();
					
					catConfirmation.setId(rs.getLong("ID"));
					catConfirmation.setCatId(rs.getLong("CAT_ID"));
					catConfirmation.setUserId(rs.getLong("USER_ID"));
					catConfirmation.setConfirmationDate(rs.getDate("CONFIRMATION_DATE"));
					catConfirmations.add(catConfirmation);
				}
			} catch (SQLException e) {
				e.printStackTrace();
				return new ArrayList<CatConfirmation>();
			}
		}
		
		return catConfirmations;
	}


	public static CatConfirmationConverter getInstance() {
		if (instance == null) {
			instance = new CatConfirmationConverter();
		}
		return instance;
	}

}
