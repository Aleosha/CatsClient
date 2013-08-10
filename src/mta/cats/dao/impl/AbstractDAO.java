package mta.cats.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import mta.cats.dao.IDAO;
import mta.cats.db.ConnectionManager;
import mta.cats.model.converters.CatConverter;

public abstract class AbstractDAO<T> implements IDAO<T> {
	
	protected AbstractDAO() {
	}
	
	Connection getConnection() throws SQLException {
		// Did you remember to give grant all privileges on *.* to catsfeeder_user@localhost identified by 'password'?
		return ConnectionManager.getInstance().getConnection();
	}

	/**
	 * 
	 * @param string
	 * @param parameters - Only Long, Float, String accepted
	 * @return
	 */
	public ResultSet select(String string, Object... parameters) {
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		Connection conn = null;
		try {
			conn = getConnection();
			statement = conn.prepareStatement(string);
			
			if (parameters != null) {
				setParameters(statement, parameters);
			}
			
			statement.execute();
			resultSet = statement.getResultSet();

			return resultSet;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			//Don't close connection here, result set needs it open later
		}
		return null;
	}

	private void setParameters(PreparedStatement statement, Object[] parameters) throws SQLException {
		int parameterCount = 0;
		for (Object p : parameters) {
			if (p == null) {
				//TODO what to do here?
			}
			if (p instanceof Long) {
				statement.setLong(++parameterCount, (Long) p);
			} 
			else if (p instanceof Float) {
				statement.setFloat(++parameterCount, (Float) p);
			} 
			else if (p instanceof String) {
				statement.setString(++parameterCount, (String) p);
			} 
			else {
				//TODO print something?
			}
		}
	}
}
