package mta.cats.dao.impl;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import java.util.List;

import mta.cats.db.SchemaInitializer;
import mta.cats.model.Cat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CatsDAOTest extends DAOTest {

	

	
	@Test 
	public void testCRUD() {
		CatsDAO catsDao = CatsDAO.getInstance();
		
		assertEquals(0, catsDao.selectAll().size());
		Cat cat = new Cat();
		cat.setNickname("Mitzy");
		cat.setLocationX(100.34F);
		cat.setLocationY(200.45F);
		catsDao.create(cat);
	
		cat.getCreationDate();
		
		List<Cat> cats = catsDao.selectAll();
		assertEquals(1, cats.size());
		cat = cats.get(0);
		assertEquals("Mitzy", cat.getNickname());
		
		cat = new Cat();
		cat.setNickname("Flufy");
		cat.setLocationX(100.32F);
		cat.setLocationY(200.58F);
		catsDao.create(cat);
		
		cats = catsDao.selectAll();
		assertEquals(2, cats.size());
		cat = cats.get(0);
		
		catsDao.delete(cat);
		cats = catsDao.selectAll();
		assertEquals(1, cats.size());
		cat = cats.get(0);
		assertEquals("Flufy", cat.getNickname());
		
		catsDao.delete(cat);
		
		cats = catsDao.selectAll();
		assertEquals(0, cats.size());
	}
	
	@Test
	public void testUpdate() {
		CatsDAO catsDao = CatsDAO.getInstance();
		
		assertEquals(0, catsDao.selectAll().size());
		Cat cat = new Cat();
		cat.setNickname("Mitzy");
		cat.setLocationX(234F);
		cat.setLocationY(567F);
		catsDao.create(cat);
		List<Cat> cats = catsDao.selectAll();
		assertEquals(1, cats.size());
		cat = cats.get(0);
		assertEquals("Mitzy", cat.getNickname());
		
		cat.setNickname("Mutzy");
		cat.setLocationX(123F);
		cat.setLocationY(345F);
		int updated = catsDao.update(cat);
		assertEquals(1, updated);
		
		cats = catsDao.selectAll();
		cat = cats.get(0);
		assertEquals("Mutzy", cat.getNickname());
		assertTrue (new Float(123).equals(cat.getLocationX()));
		assertTrue (new Float(345).equals(cat.getLocationY()));
	}
	
	@Test
	public void testSelectInArea() {
		CatsDAO catsDao = CatsDAO.getInstance();
		
		assertEquals(0, catsDao.selectAll().size());
		Cat cat = new Cat();
		cat.setNickname("Mitzy");
		cat.setLocationX(234F);
		cat.setLocationY(567F);
		catsDao.create(cat);
		
		List<Cat> cats = catsDao.selectInArea(234F, 567F);
		
		assertEquals(1, cats.size());
		
		cat = new Cat();
		cat.setNickname("Mitzy 2");
		cat.setLocationX(233F);
		cat.setLocationY(568F);
		catsDao.create(cat);
		
		cats = catsDao.selectInArea(234F, 567F);
		
		assertEquals(2, cats.size());
		
		// Cat outside radius X
		cat = new Cat();
		cat.setNickname("Mitzy 3");
		cat.setLocationX(203F);
		cat.setLocationY(568F);
		catsDao.create(cat);
		
		cats = catsDao.selectInArea(234F, 567F);
		
		assertEquals(2, cats.size());
		
		// Cat outside radius Y
		cat = new Cat();
		cat.setNickname("Mitzy 4");
		cat.setLocationX(233F);
		cat.setLocationY(668F);
		catsDao.create(cat);
		
		cats = catsDao.selectInArea(234F, 567F);
		
		assertEquals(2, cats.size());
		
		// Radius X
		cats = catsDao.selectInArea(233F, 567F);
		
		assertEquals(2, cats.size());
		
		// Radius Y
		cats = catsDao.selectInArea(233F, 568F);
		
		assertEquals(2, cats.size());
	}
}
