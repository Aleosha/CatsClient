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
import mta.cats.model.Feeder;
import mta.cats.model.TopFeeder;
import mta.cats.model.User;
import mta.cats.model.converters.CatConverter;
import mta.cats.model.converters.FeederConverter;
import mta.cats.model.converters.TopFeederConverter;
import mta.cats.model.converters.UserConverter;

public class FeedersDAO extends AbstractDAO<Feeder> {

	private static final String TABLE = "FEEDERS";
	private static final String SELECT_TOP = "SELECT COUNT(1) as CNT, u.ID, USERNAME, IMAGE FROM " + TABLE + " f " +
			"JOIN USERS u on (f.USER_ID = u.ID) " +
			"GROUP BY u.ID, USERNAME, IMAGE " +
			"ORDER BY COUNT(1) DESC LIMIT 0, ?";
	private static final String SELECT_ALL = "SELECT * FROM " + TABLE;
	private static final String INSERT = "INSERT INTO FEEDERS (CAT_ID, USER_ID, DAY_OF_WEEK) VALUES (?, ?, ?)";
	private static final String SELECT_BY_CAT_ID = "SELECT * FROM " + TABLE + " WHERE CAT_ID + ?";
	private static final String DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE ID = ?";
	private static FeedersDAO instance = null;
	
	private FeedersDAO() {
		
	}

	@Override
	public Feeder select(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Feeder> selectAll() {
		ResultSet result = super.select(SELECT_ALL);
		if (result != null) {
			return FeederConverter.getInstance().convert(result);
		}
		return null;
	}

	@Override
	/**
	 * Disallow updating feeder
	 * Create and delete the row must be done
	 */
	public int update(Feeder model) {
		return 0;
	}

	@Override
	public Feeder create(Feeder model) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			if (model == null) {
				return null;
			}
			else if (model.getCatId() == null) {
				return null;
			}
			else if (model.getUserId() == null) {
				return null;
			}
			else if (model.getDayOfWeek() == null) {
				return null;
			}
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			
			int count = 0;
			stmt.setLong(++count, model.getCatId());
			stmt.setLong(++count, model.getUserId());
			stmt.setInt(++count, model.getDayOfWeek().ordinal());
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

	@Override
	public int delete(Feeder model) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(DELETE_BY_ID);
			stmt.setLong(1, model.getId());
			return stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeConnection(conn);
		}
		return 0;
	}

	public static FeedersDAO getInstance() {
		if (instance  == null) {
			instance = new FeedersDAO();
		}
		return instance;
	}

	/**
	 * Returns all feeders for given cat
	 * @param cat
	 * @return
	 */
	public List<Feeder> selectByCat(Cat cat) {
		if (cat != null) {
			ResultSet result = super.select(SELECT_BY_CAT_ID, cat.getId());
			if (result != null) {
				List<Feeder> feeders = FeederConverter.getInstance().convert(result);
				return feeders;
			}
		}
		return null;
		
	}

	/**
	 * Returns data about best feeders
	 * The function counts number of feedings, and not number of unique cats feeded
	 * If user X feeds cat Y two days a week, will return 2 for the user
	 * @param count
	 * @return
	 */
	public List<TopFeeder> selectTop(Long count) {
		ResultSet result = super.select(SELECT_TOP, count);
		if (result != null) {
			List<TopFeeder> feeders = TopFeederConverter.getInstance().convert(result);
			return feeders;
		}
		return new ArrayList<TopFeeder>();
	}

}
