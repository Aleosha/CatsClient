package mta.cats.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Creates all the needed tables in the database
 * Called either from startup listener or from the tests initializer
 * @author i064039
 *
 */
public class SchemaInitializer {

	private static final String CATS_TABLE = "CREATE TABLE IF NOT EXISTS CATS " +
			"(ID INT PRIMARY KEY auto_increment, " +
			"NICKNAME VARCHAR(100) NOT NULL, " +
			"IMAGE VARCHAR(200), " +
			"DESCRIPTION VARCHAR(200), " +
			"LOCATION_X FLOAT NOT NULL, " +
			"LOCATION_Y FLOAT NOT NULL, " +
			"CREATION_DATE DATE NOT NULL)";
	private static final String USERS_TABLE = "CREATE TABLE IF NOT EXISTS USERS " +
			"(ID INT PRIMARY KEY auto_increment, " +
			"USERNAME VARCHAR(100) NOT NULL, " +
			"PASSWORD VARCHAR(100) NOT NULL, " +
			"IMAGE VARCHAR(200), " +
			"LOCATION_X FLOAT, " +
			"LOCATION_Y FLOAT, " +
			"CREATION_DATE DATE NOT NULL)";
	private static final String FEEDERS_TABLE = "CREATE TABLE IF NOT EXISTS FEEDERS " +
			"(ID INT PRIMARY KEY auto_increment, " +
			"CAT_ID INT NOT NULL, " +
			"USER_ID INT NOT NULL, " +
			"DAY_OF_WEEK SMALLINT NOT NULL)";
	private static final String FEEDERS_UK = "CREATE UNIQUE INDEX FEEDERS_UK ON FEEDERS (CAT_ID, USER_ID, DAY_OF_WEEK)";
	private static final String CAT_CONFIRMATIONS_TABLE = "CREATE TABLE IF NOT EXISTS CAT_CONFIRMATIONS " +
			"(ID INT PRIMARY KEY auto_increment, " +
			"CAT_ID INT NOT NULL, " +
			"USER_ID INT NOT NULL, " +
			"CONFIRMATION_DATE DATE NOT NULL)";
	private static final String CAT_CONFIRMATIONS_INDEX = "CREATE INDEX CAT_CONFIRMATIONS_I ON CAT_CONFIRMATIONS (CAT_ID)";
	private static final String CATS_LOCATIONS_INDEX = "CREATE INDEX CATS_LOCATIONS_I ON CATS (LOCATION_X, LOCATION_Y)";

	/**
	 * Initialized the database tables
	 */
	public static void initialize() {
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.createStatement();
			stmt.execute(CATS_TABLE);
			stmt.execute(USERS_TABLE);
			stmt.execute(FEEDERS_TABLE);
			stmt.execute(CAT_CONFIRMATIONS_TABLE);
			stmt.execute(CAT_CONFIRMATIONS_INDEX);
			stmt.execute(CATS_LOCATIONS_INDEX);
			stmt.execute(FEEDERS_UK);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeConnection(conn);			
		}
	}
	
	/**
	 * Drop all tables in the database
	 */
	public static void drop() {
		Connection conn = null;
		Statement stmt = null;
		try
		{
			conn = ConnectionManager.getInstance().getConnection();
			stmt = conn.createStatement();
			stmt.execute("DROP TABLE CATS");
			stmt.execute("DROP TABLE USERS");
			stmt.execute("DROP TABLE FEEDERS");
			stmt.execute("DROP TABLE CAT_CONFIRMATIONS");
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			ConnectionManager.closeStatement(stmt);
			ConnectionManager.closeConnection(conn);			
		}
	}
}
