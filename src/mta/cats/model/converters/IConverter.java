package mta.cats.model.converters;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import mta.cats.model.IModel;

public interface IConverter<T> {
	/**
	 * Fills model object with result from DB
	 * @param rs
	 * @return
	 * @throws SQLException 
	 */
	List<T> convert(ResultSet rs);
}
