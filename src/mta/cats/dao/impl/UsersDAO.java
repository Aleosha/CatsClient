package mta.cats.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mta.cats.db.ConnectionManager;
import mta.cats.model.Cat;
import mta.cats.model.User;
import mta.cats.model.converters.CatConverter;
import mta.cats.model.converters.UserConverter;

public class UsersDAO extends AbstractDAO<User> {

	private static final String TABLE = "USERS";
	private static final String DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE ID = ?";
	private static final String SELECT_BY_ID = "SELECT * FROM " + TABLE + " WHERE ID = ?";
	private static final String SELECT_ALL = "SELECT * FROM " + TABLE;
	private static final String INSERT = "INSERT INTO " + TABLE + " (USERNAME, PASSWORD, LOCATION_X, LOCATION_Y, CREATION_DATE) VALUES (?, ?, ?, ?, ?)";
	private static final String UPDATE = "UPDATE " + TABLE + " SET USERNAME = ?, PASSWORD = ?, LOCATION_X = ?, LOCATION_Y = ? WHERE ID = ?";
	
	private static UsersDAO instance = null;
	
	private UsersDAO() {
	}
	
	public static UsersDAO getInstance() {
		if (instance == null) {
			instance = new UsersDAO();
		}
		return instance;
	}

	@Override
	public User select(Long id) {
		ResultSet result = super.select(SELECT_BY_ID, id);
		if (result != null) {
			List<User> users = UserConverter.getInstance().convert(result);
			if (users.size() > 0) {
				return users.get(0);
			}
		}
		return null;
	}

	@Override
	public List<User> selectAll() {
		ResultSet result = super.select(SELECT_ALL);
		if (result != null) {
			return UserConverter.getInstance().convert(result);
		}
		else {
			return new ArrayList<User>();
		}
	}

	@Override
	public int update(User model) {
		if (model == null || model.getId() == null)
			return 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(UPDATE);
			int count = 0;
			stmt.setString(++count, model.getUsername());
			stmt.setString(++count, model.getPassword());
			if (model.getLocationX() != null && model.getLocationY() != null) {
				stmt.setFloat(++count, model.getLocationX());
				stmt.setFloat(++count, model.getLocationY());
			}
			else {
				stmt.setNull(++count, java.sql.Types.FLOAT);
				stmt.setNull(++count, java.sql.Types.FLOAT);
			}
			stmt.setLong(++count, model.getId());
			return stmt.executeUpdate();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeConnection(conn);
		}
		return 0;
	}

	@Override
	public User create(User model) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			if (model == null) {
				return null;
			}
			else if (model.getUsername() == null) {
				return null;
			}
			else if (model.getPassword() == null) {
				return null;
			}
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			
			int count = 0;
			stmt.setString(++count, model.getUsername());
			stmt.setString(++count, model.getPassword());
			if (model.getLocationX() == null || model.getLocationY() == null) {
				stmt.setNull(++count, java.sql.Types.FLOAT);
				stmt.setNull(++count, java.sql.Types.FLOAT);
			}
			else {
				stmt.setFloat(++count, model.getLocationX());
				stmt.setFloat(++count, model.getLocationY());
			}
			
			stmt.setDate(++count, new java.sql.Date(System.currentTimeMillis()));
			stmt.executeUpdate();
			ResultSet keys = stmt.getGeneratedKeys();
			
			// Set the generated id
			if (keys != null) {
				if (keys.next()) {
					model.setId(keys.getLong(1));
				}
			}
			
			return model;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeConnection(conn);
		}
		return null;
	}

	/**
	 * @return Number of deleted rows (should be 1)
	 */
	public int delete(User model) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(DELETE_BY_ID);
			stmt.setLong(1, model.getId());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeConnection(conn);
		}
		return 0;
	}

}
