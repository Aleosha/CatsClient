package mta.cats.index;
import static org.junit.Assert.*;

import java.sql.Date;

import mta.cats.dao.impl.CatsDAO;
import mta.cats.dao.impl.DAOTest;
import mta.cats.model.Cat;

import org.junit.Test;

import redis.clients.jedis.Jedis;
public class IndexTest extends DAOTest {
	
	@Test
	public void testJedis() {
		//Connecting to Redis on localhost
		  Jedis indexer = new Jedis("localhost");
		  //adding a new key
		  
		  indexer.set("key", "value");
		  //getting the key value
		  assertEquals("value", indexer.get("key"));
		  
		  assertNull(indexer.get("non_existing"));
	 
	}
	
	@Test
	public void testIndexer() {
		Indexer indexer = Indexer.getInstance();
		
		indexer.get("value");
		
		indexer.set("key", "value");
		  //getting the key value
		  assertEquals("value", indexer.get("key"));
		  
		  assertNull(indexer.get("non_existing"));
		  
	}
	
	@Test
	public void testCatIndexer() {
		CatsDAO dao = CatsDAO.getInstance();
		Cat cat = new Cat();
		cat.setLocationX(100F);
		cat.setLocationY(200F);
		cat.setNickname("Mitzy");
		
		cat = dao.create(cat);
		
		Indexer.getInstance().indexCat(cat);
		
		Cat catIndexed = Indexer.getInstance().getCat(cat.getId());
		assertEquals(cat, catIndexed);
	}
	
	@Test 
	public void testCatIndexInvalidation() {
		CatsDAO dao = CatsDAO.getInstance();
		Cat cat = new Cat();
		cat.setLocationX(100F);
		cat.setLocationY(200F);
		cat.setNickname("Mitzy");
		
		cat = dao.create(cat);
		
		Long id = cat.getId();
		assertNotNull(dao.select(id));
		dao.delete(cat);
		assertNull(dao.select(id));
	}
}
