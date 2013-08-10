package mta.cats.db;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionManager {

	private static ConnectionManager instance = null;
	
	Properties prop = null;
	
	private ConnectionManager() {
		prop = new Properties();
		try {
			prop.load(ConnectionManager.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static ConnectionManager getInstance() {
		if (instance == null) {
			instance = new ConnectionManager();
		}
		
		return instance;
	}

	public Connection getConnection()
	{
		try {
	        Class.forName(prop.getProperty("db.driver"));
	    } catch (ClassNotFoundException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    } 
		try {
			// Initialize with local
			String dbHost = prop.getProperty("db.host");
			String dbName = prop.getProperty("db.name");
			String username = prop.getProperty("db.username");
			String password = prop.getProperty("db.password");
			
			String vcap = System.getenv("VCAP_SERVICES");
			// Cloud
			if (vcap != null) {
				System.out.println("Retrieving Cloud connection parameters");
				JSONObject jsonObject = new JSONObject(vcap);
				JSONArray jsonArray = jsonObject.getJSONArray("mysql-5.1");
				jsonObject = jsonArray.getJSONObject(0);
				jsonObject = jsonObject.getJSONObject("credentials");
				dbHost 	=  jsonObject.getString("host");
				dbName 	=  jsonObject.getString("name");
				username =  jsonObject.getString("username");
				password =  jsonObject.getString("password");
			}

			return DriverManager.getConnection(String.format("jdbc:mysql://%s/%s?user=%s&password=%s", dbHost, 
					dbName, username, password));
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		//TODO check for nulls?
		return null;
	}
	
	/**
	 * This method was created to avoid repeating try/catch in various finalization parts of the code
	 * @param stmt
	 */
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				if (!conn.isClosed()) {
					conn.close();
					conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method was created to avoid repeating try/catch in various finalization parts of the code
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				if (!stmt.isClosed()) {
					stmt.close();
					stmt = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
