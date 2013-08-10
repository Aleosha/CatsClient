package mta.cats.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mta.cats.db.ConnectionManager;
import mta.cats.index.Indexer;
import mta.cats.model.Cat;
import mta.cats.model.converters.CatConverter;

/**
 * DAO for cats
 */
public class CatsDAO extends AbstractDAO<Cat> {

	private static final String SELECT_IN_AREA = "SELECT * FROM CATS WHERE (LOCATION_X BETWEEN ? AND ?) AND (LOCATION_Y BETWEEN ? AND ?)";
	private static final String TABLE = "CATS";
	private static final String DELETE_BY_ID = "DELETE FROM " + TABLE + " WHERE ID = ?";
	private static final String SELECT_BY_ID = "SELECT * FROM " + TABLE + " WHERE ID = ?";
	private static final String SELECT_ALL = "SELECT * FROM " + TABLE;
	private static final String UPDATE = "UPDATE " + TABLE + " SET NICKNAME = ?, LOCATION_X = ?, LOCATION_Y = ? WHERE ID = ?";
	private static final String INSERT = "INSERT INTO " + TABLE + " (NICKNAME, LOCATION_X, LOCATION_Y, CREATION_DATE) VALUES (?, ?, ?, ?)";
	private static CatsDAO instance;
	
	private CatsDAO() {
		super();
	}
	
	
	public static CatsDAO getInstance() {
		if (instance == null) {
			instance = new CatsDAO();
		}
		
		return instance;
	}
	
	/**
	 * Will return null if cat wasn't found or more than one cat was found
	 */
	public Cat select(Long id) {
		
		if (id != null) {
			// Check if this cat was already indexed
			Cat cat = Indexer.getInstance().getCat(id);
			if (cat != null) {
				return cat;
			}
			
			// Get the cat from DB
			ResultSet result = super.select(SELECT_BY_ID, id);
			if (result != null) {
				List<Cat> cats = CatConverter.getInstance().convert(result);
				if (cats.size() != 0) {
					cat = cats.get(0);
					Indexer.getInstance().indexCat(cat);
					return cat;
				}
			}
		}
		return null;
	}

	/**
	 * Returns all cats in DB
	 */
	@Override
	public List<Cat> selectAll() {
		ResultSet result = super.select(SELECT_ALL);
		if (result != null) {
			return CatConverter.getInstance().convert(result);
		}
		else {
			return new ArrayList<Cat>();
		}
	}

	/**
	 * Updates a cat by its ID
	 * This method is not optimized, and updates all fields of the cat (instead of only changed)
	 * @return Number of updated cats
	 */
	@Override
	public int update(Cat model) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(UPDATE);
			int count = 0;
			stmt.setString(++count, model.getNickname());
			stmt.setFloat(++count, model.getLocationX());
			stmt.setFloat(++count, model.getLocationY());
			stmt.setLong(++count, model.getId());
			int updated = stmt.executeUpdate();
			
			if (updated > 0) {
				Indexer.getInstance().removeCat(model);
			}
			return updated;

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

	/**
	 * Create new cat
	 * @return Model with ID from DB if succeeded, null otherwise
	 */
	@Override
	public Cat create(Cat model) {
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			if (model == null) {
				return null;
			}
			else if (model.getLocationX() == null) {
				return null;
			}
			else if (model.getLocationY() == null) {
				return null;
			}
			else if (model.getNickname() == null) {
				return null;
			}
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS);
			
			int count = 0;
			stmt.setString(++count, model.getNickname());
			stmt.setFloat(++count, model.getLocationX());
			stmt.setFloat(++count, model.getLocationY());
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
	 * @return Number of deleted rows (should be 1)
	 */
	public int delete(Cat model) {
		if (model == null || model.getId() == null)
			return 0;
		Connection conn = null;
		PreparedStatement stmt = null;
		try {
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.prepareStatement(DELETE_BY_ID);
			stmt.setLong(1, model.getId());
			int deleted = stmt.executeUpdate();
			
			// Invalidate index
			if (deleted > 0) {
				Indexer.getInstance().removeCat(model);
			}
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


	/**
	 * Currently we are not using a circular radius, but a rectangle
	 * In the future, we could use circular radius
	 * @param locationX
	 * @param locationY
	 * @return
	 */
	public List<Cat> selectInArea(Float locationX, Float locationY) {
		Float radius = 1F;
		ResultSet result = super.select(SELECT_IN_AREA, 
				locationX - radius , locationX + radius, locationY - radius, locationY + radius);
		if (result != null) {
			List<Cat> cats = CatConverter.getInstance().convert(result);
			return cats;
		}
		return new ArrayList<Cat>();
	}
}
