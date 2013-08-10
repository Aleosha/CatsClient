package mta.cats.dao.impl;

import static org.junit.Assert.*;

import java.sql.Date;
import java.util.List;

import mta.cats.model.Cat;
import mta.cats.model.CatConfirmation;
import mta.cats.model.Counter;
import mta.cats.model.User;

import org.junit.Test;

public class CatConfirmationDAOTest extends DAOTest {

	@Test
	public void testCR() {
		CatConfirmationsDAO confDao = CatConfirmationsDAO.getInstance();
		UsersDAO userDao = UsersDAO.getInstance();
		CatsDAO catsDao = CatsDAO.getInstance();
		
		Cat cat1 = new Cat("Mitzy", 100F, 200F);
		catsDao.create(cat1);
		
		Cat cat2 = new Cat("Mutzy", 200F, 300F);
		catsDao.create(cat2);
		
		User user1 = new User("Alexey", "abcd123");
		userDao.create(user1);
		
		User user2 = new User("Eran", "abcd123");
		userDao.create(user2);
		
		assertEquals(0, confDao.selectAll().size());
		
		CatConfirmation confirmation = new CatConfirmation();
		confirmation.setCatId(cat1);
		confirmation.setUserId(user1);
		confDao.create(confirmation);
		
		assertEquals(1, confDao.selectAll().size());
		confirmation = confDao.selectAll().get(0);
		Date today = new Date(System.currentTimeMillis());
		assertEquals(today.toString(), confirmation.getConfirmationDate().toString());
		
		assertEquals(Long.valueOf(0), confDao.selectCountByCat(null));
		assertEquals(Long.valueOf(1), confDao.selectCountByCat(cat1));
		assertEquals(Long.valueOf(0), confDao.selectCountByCat(cat2));
	}
	
	@Test
	public void testSelectCounts() {
		CatConfirmationsDAO confDao = CatConfirmationsDAO.getInstance();
		UsersDAO userDao = UsersDAO.getInstance();
		CatsDAO catsDao = CatsDAO.getInstance();
		
		Cat cat1 = new Cat("Mitzy", 100F, 200F);
		catsDao.create(cat1);
		
		Cat cat2 = new Cat("Mutzy", 200F, 300F);
		catsDao.create(cat2);
		
		User user1 = new User("Alexey", "abcd123");
		userDao.create(user1);
		
		User user2 = new User("Eran", "abcd123");
		userDao.create(user2);
		
		CatConfirmation confirmation = new CatConfirmation();
		confirmation.setCatId(cat1);
		confirmation.setUserId(user1);
		confDao.create(confirmation);
		
		confirmation = new CatConfirmation();
		confirmation.setCatId(cat1);
		confirmation.setUserId(user2);
		confDao.create(confirmation);
		
		confirmation = new CatConfirmation();
		confirmation.setCatId(cat2);
		confirmation.setUserId(user1);
		confDao.create(confirmation);
		
		List<Counter> counts = confDao.selectCounts();
		assertEquals(2, counts.size());
		assertEquals("Mitzy", counts.get(0).getName());
		assertEquals(Long.valueOf(2), counts.get(0).getCount());
		
		assertEquals("Mutzy", counts.get(1).getName());
		assertEquals(Long.valueOf(1), counts.get(1).getCount());
	}
}
