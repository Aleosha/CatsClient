package mta.cats.dao.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import mta.cats.db.SchemaInitializer;
import mta.cats.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UsersDAOTest {
	@Before 
	public void before() {
		SchemaInitializer.initialize();
	}
	
	@After
	public void after() {
		SchemaInitializer.drop();
	}
	
	@Test 
	public void testCRUD() {
		try {
			UsersDAO usersDao = UsersDAO.getInstance();
			
			assertEquals(0, usersDao.selectAll().size());
			User user = new User();
			user.setUsername("Alexey");
		
			user.setPassword("abcd123");
			usersDao.create(user);
		
			user.getCreationDate();
			
			List<User> users = usersDao.selectAll();
			assertEquals(1, users.size());
			user = users.get(0);
			assertEquals("Alexey", user.getUsername());
			user = usersDao.select(user.getId());
			assertEquals("Alexey", user.getUsername());
			
			user = new User();
			user.setUsername("Alexey 2");
			user.setPassword("abcd1234");
			user.setLocationX(100.32F);
			user.setLocationY(200.58F);
			usersDao.create(user);
			
			users = usersDao.selectAll();
			assertEquals(2, users.size());
			user = users.get(1);
			assertEquals(new Float(100.32F), user.getLocationX());
			user = users.get(0);
			usersDao.delete(user);
			users = usersDao.selectAll();
			assertEquals(1, users.size());
			user = users.get(0);
			assertEquals("Alexey 2", user.getUsername());
			
			usersDao.delete(user);
			
			users = usersDao.selectAll();
			assertEquals(0, users.size());
		}
		finally {
			 List<User> users = UsersDAO.getInstance().selectAll();
			 for (User user : users) {
				 UsersDAO.getInstance().delete(user);
			 }
		}
	}
	
	@Test
	public void testUpdate() {
		UsersDAO usersDao = UsersDAO.getInstance();
		
		assertEquals(0, usersDao.selectAll().size());
		User user = new User();
		user.setUsername("Alexey");
		user.setLocationX(234F);
		user.setLocationY(567F);
		user.setPassword("abc");
		usersDao.create(user);
		List<User> users = usersDao.selectAll();
		assertEquals(1, users.size());
		user = users.get(0);
		assertEquals("Alexey", user.getUsername());
		
		user.setUsername("Mutzy");
		user.setLocationX(123F);
		user.setLocationY(345F);
		int updated = usersDao.update(user);
		assertEquals(1, updated);
		
		users = usersDao.selectAll();
		user = users.get(0);
		assertEquals("Mutzy", user.getUsername());
		assertTrue (new Float(123).equals(user.getLocationX()));
		assertTrue (new Float(345).equals(user.getLocationY()));
	}

}
