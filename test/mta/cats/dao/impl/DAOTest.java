package mta.cats.dao.impl;

import mta.cats.db.SchemaInitializer;

import org.junit.After;
import org.junit.Before;

public class DAOTest {
	@Before 
	public void before() {
		SchemaInitializer.initialize();
	}
	
	@After
	public void after() {
		SchemaInitializer.drop();
	}
}
