package mta.cats.dao.impl;

import static org.junit.Assert.*;

import java.util.List;

import mta.cats.db.SchemaInitializer;
import mta.cats.enums.DayOfWeek;
import mta.cats.model.Cat;
import mta.cats.model.Feeder;
import mta.cats.model.TopFeeder;
import mta.cats.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FeederDAOTest extends DAOTest {
	@Before 
	public void before() {
		SchemaInitializer.initialize();
	}
	
	@After
	public void after() {
		SchemaInitializer.drop();
	}
	
	@Test
	public void testCRD() {
		FeedersDAO feederDao = FeedersDAO.getInstance();
		UsersDAO userDao = UsersDAO.getInstance();
		CatsDAO catsDao = CatsDAO.getInstance();
		
		Cat cat1 = new Cat("Mitzy", 100F, 200F);
		catsDao.create(cat1);
		
		User user1 = new User("Alexey", "abcd123");
		userDao.create(user1);
		
		User user2 = new User("Eran", "abcd123");
		userDao.create(user2);
		
		Feeder feeder = new Feeder();
		feeder.setCatId(cat1);
		feeder.setUserId(user1);
		feeder.setDayOfWeek(1);
		feederDao.create(feeder);
		
		List<Feeder> feeders = feederDao.selectAll();
		assertEquals(1, feeders.size());
		feeder = feeders.get(0);
		assertEquals(feeder.getDayOfWeek(), DayOfWeek.SUNDAY);
		assertEquals(feeder.getCatId(), cat1.getId());
		assertEquals(feeder.getUserId(), user1.getId());
		assertEquals(feeder, feederDao.selectByCat(cat1).get(0));
		
		//Try to feed same cat on the same day by the same user
		feeder = new Feeder();
		feeder.setCatId(cat1);
		feeder.setUserId(user1);
		feeder.setDayOfWeek(1);
		feederDao.create(feeder);
		
		feeders = feederDao.selectAll();
		// Shouldn't be 2, since you can't add this row
		assertEquals(1, feeders.size());
		
		// Try to feed same cat on different day by different user
		feeder = new Feeder();
		feeder.setCatId(cat1);
		feeder.setUserId(user2);
		feeder.setDayOfWeek(2);
		feederDao.create(feeder);
		
		feeders = feederDao.selectAll();
		// Shouldn't be 2, since you can't add this row
		assertEquals(2, feeders.size());
		
		
		feederDao.delete(feeders.get(0));
		

		feeders = feederDao.selectAll();
		// Shouldn't be 2, since you can't add this row
		assertEquals(1, feeders.size());
		
	}
	
	@Test
	public void testTopFeeder() {
		FeedersDAO feederDao = FeedersDAO.getInstance();
		UsersDAO userDao = UsersDAO.getInstance();
		CatsDAO catsDao = CatsDAO.getInstance();
		
		for (int i = 1; i <= 10; i++) {
			Cat cat = new Cat("Mitzy " + i, 100F, 200F);
			catsDao.create(cat);
		}
		User user1 = new User("Alexey", "abcd123");
		userDao.create(user1);
		
		User user2 = new User("Eran", "abcd123");
		userDao.create(user2);
		
		List<Cat> cats = catsDao.selectAll();
		for (Cat c : cats) {
			Feeder feeder = new Feeder();
			feeder.setUserId(user1);
			feeder.setCatId(c);
			feeder.setDayOfWeek(DayOfWeek.SUNDAY);
			feederDao.create(feeder);
		}
		Feeder feeder = new Feeder();
		feeder.setUserId(user2);
		feeder.setCatId(cats.get(0));
		feeder.setDayOfWeek(DayOfWeek.MONDAY);
		feederDao.create(feeder);
		
		feeder = new Feeder();
		feeder.setUserId(user2);
		feeder.setCatId(cats.get(1));
		feeder.setDayOfWeek(DayOfWeek.MONDAY);
		feederDao.create(feeder);
		
		List<TopFeeder> topFeeder = feederDao.selectTop(5L);
		assertEquals(2, topFeeder.size());
		assertEquals(Long.valueOf(10), topFeeder.get(0).getCount());
		assertEquals(Long.valueOf(2), topFeeder.get(1).getCount());
	}
}
