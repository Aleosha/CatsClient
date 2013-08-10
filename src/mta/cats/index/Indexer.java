package mta.cats.index;

import java.io.IOException;
import java.sql.Date;
import java.util.Map;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import mta.cats.db.ConnectionManager;
import mta.cats.model.Cat;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * This class wraps Redis client 
 * @author i064039
 *
 */
public class Indexer {

	private static final String LOCATION_Y = "locationY";
	private static final String LOCATION_X = "locationX";
	private static final String CREATION_DATE = "creation_date";
	private static final String NICKNAME = "nickname";
	private static final String ID = "id";
	private static final String CAT_PREFIX = "cat:";
	static Indexer instance = null;
	private Properties prop;
	private Jedis jedis;
	
	private Indexer() {
		prop = new Properties();
		try {
			prop.load(ConnectionManager.class.getClassLoader().getResourceAsStream("config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		String host = prop.getProperty("redis.host");
		Integer port = Integer.parseInt(prop.getProperty("redis.port"));
		String password = null;
		String vcap = System.getenv("VCAP_SERVICES");
		if (vcap != null) {
			System.out.println("Retrieving Redis configuration from Cloud");
			JSONObject jsonObject;
			try {
				jsonObject = new JSONObject(vcap);
				JSONArray jsonArray = jsonObject.getJSONArray("redis-2.2");
				jsonObject = jsonArray.getJSONObject(0);
				jsonObject = jsonObject.getJSONObject("credentials");
				host 	=  jsonObject.getString("host");
				port 	=  Integer.parseInt(jsonObject.getString("port"));
				password = jsonObject.getString("password");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JedisShardInfo info = new JedisShardInfo(host, port);
		if (password != null)
			info.setPassword(password);
		jedis = new Jedis(info);
		
	
	}
	
	public static Indexer getInstance() {
		if (instance == null) {
			instance = new Indexer();
		}
		
		return instance;
	}

	public String get(String key) {
		return jedis.get(key);
	}

	public void set(String key, String value) {
		jedis.set(key, value);
	}

	/**
	 * Indexes cat object from DB
	 * @param cat
	 */
	public void indexCat(Cat cat) {
		if (cat != null && cat.getId() != null) {
			Long id = cat.getId();
			if (id != null) {
				String key = CAT_PREFIX+id;
				jedis.hset(key, ID, Long.toString(cat.getId()));
				jedis.hset(key, NICKNAME, cat.getNickname());
				if (cat.getCreationDate() != null) {
					jedis.hset(key, CREATION_DATE, cat.getCreationDate().toString());
				}
				jedis.hset(key, LOCATION_X, Float.toString(cat.getLocationX()));
				jedis.hset(key, LOCATION_Y, Float.toString(cat.getLocationY()));
			}
		}
	}

	/**
	 * Get cat from index
	 * Returns null if cat is not found
	 * @param id
	 * @return
	 */
	public Cat getCat(Long id) {
		try {
			Map<String, String> catFields = jedis.hgetAll(CAT_PREFIX+id);
			if (catFields != null && catFields.size() > 0) {
				Cat cat = new Cat(); 
				cat.setId(Long.parseLong(catFields.get(ID)));
				cat.setNickname(catFields.get(NICKNAME));
				if (catFields.get(CREATION_DATE) != null) {
					cat.setCreationDate(Date.valueOf(catFields.get(CREATION_DATE)));
				}
				cat.setLocationX(Float.parseFloat(catFields.get(LOCATION_X)));
				cat.setLocationY(Float.parseFloat(catFields.get(LOCATION_Y)));
				
				return cat;
			}
		}
		catch (JedisConnectionException jce) {
			jce.printStackTrace();
		}
		return null;
	}

	/**
	 * Removes cat from index
	 * @param model
	 */
	public void removeCat(Cat model) {
		if (model != null && model.getId() != null) {
			jedis.del(CAT_PREFIX+model.getId());
		}
	}

}
