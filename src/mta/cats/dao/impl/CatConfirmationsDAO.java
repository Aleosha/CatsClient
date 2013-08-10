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
import mta.cats.model.CatConfirmation;
import mta.cats.model.Counter;
import mta.cats.model.converters.CatConfirmationConverter;
import mta.cats.model.converters.CounterConverter;

public class CatConfirmationsDAO extends AbstractDAO<CatConfirmation> {

	private static final String TABLE = "CAT_CONFIRMATIONS";
	private static final String SELECT_BY_ID = "SELECT * FROM " + TABLE + " WHERE ID = ?";
	private static final String SELECT_ALL = "SELECT * FROM " + TABLE;
	private static final String INSERT = "INSERT INTO " + TABLE + " (CAT_ID, USER_ID, CONFIRMATION_DATE) VALUE (?, ?, ?)";
	private static final String SELECT_COUNT_BY_CAT = "SELECT COUNT(1) FROM " + TABLE + " WHERE CAT_ID = ?";
	private static final String SELECT_COUNTS = "SELECT CAT_ID as ID, NICKNAME as NAME, COUNT(1) as CNT FROM " + TABLE + " cc" +
												" JOIN CATS c ON (c.ID = cc.CAT_ID) " +
												" GROUP BY CAT_ID, NICKNAME ORDER BY COUNT(1) DESC";
	private static CatConfirmationsDAO instance = null;
	
	private CatConfirmationsDAO() {
		
	}
	
	public static CatConfirmationsDAO getInstance() {
		if (instance == null) {
			instance = new CatConfirmationsDAO();
		}
		return instance;
	}

	/**
	 * Get confirmation by ID
	 */
	public CatConfirmation select(Long id) {
		ResultSet result = super.select(SELECT_BY_ID, id);
		if (result != null) {
			List<CatConfirmation> cats = CatConfirmationConverter.getInstance().convert(result);
			if (cats.size() != 0) {
				return cats.get(0);
			}
		}
		return null;
	}

	/**
	 * Return all confirmations
	 */
	@Override
	public List<CatConfirmation> selectAll() {
		ResultSet result = super.select(SELECT_ALL);
		if (result != null) {
			return CatConfirmationConverter.getInstance().convert(result);
		}
		else {
			return new ArrayList<CatConfirmation>();
		}
	}

	@Override
	/**
	 * Disallow update
	 */
	public int update(CatConfirmation model) {
		return 0;
	}

	/**
	 * Create new confirmation
	 */
	@Override
	public CatConfirmation create(CatConfirmation model) {
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
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			
			int count = 0;
			stmt.setLong(++count, model.getCatId());
			stmt.setLong(++count, model.getUserId());
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

	
	@Override
	/**
	 * Disallow delete
	 */
	public int delete(CatConfirmation model) {
		return 0;
	}

	/**
	 * Counts the number of confirmations for specific cat
	 * @param cat
	 * @return
	 */
	public Long selectCountByCat(Cat cat) {
		if (cat != null) {
			ResultSet result = super.select(SELECT_COUNT_BY_CAT, cat.getId());
			if (result != null) {
				try {
					if (result.next()) {
						return result.getLong(1);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return 0L;
	}
	
	public List<Counter> selectCounts() {
		ResultSet result = super.select(SELECT_COUNTS);
		if (result != null) {
			return CounterConverter.getInstance().convert(result);
		}
		else {
			return new ArrayList<Counter>();
		}
	}

}
