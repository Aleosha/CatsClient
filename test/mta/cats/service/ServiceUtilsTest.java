package mta.cats.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ServiceUtilsTest {

	@Test
	public void testParsePath() {
		// Just don't fail
		String [] path = ServiceUtils.parsePath(null);
		assertEquals(1, path.length);
		path = ServiceUtils.parsePath("");
		assertEquals(1, path.length);
		path = ServiceUtils.parsePath("/");
		assertEquals(1, path.length);
		path = ServiceUtils.parsePath("//");
		assertEquals(1, path.length);
		
		path = ServiceUtils.parsePath("get");
		path = 	ServiceUtils.parsePath("get/");
		path = 	ServiceUtils.parsePath("get//");
		path = ServiceUtils.parsePath("get/123");
		path = 	ServiceUtils.parsePath("get/123/");
		path = 	ServiceUtils.parsePath("get/aBc/");
		
		// Test with more parameters
		path = 	ServiceUtils.parsePath("get/123/put");
		
		assertEquals(3, path.length);
		assertEquals("get", path[0]);
		assertEquals("123", path[1]);
		assertEquals("put", path[2]);
		// Test for delimiters
		path = 	ServiceUtils.parsePath("/get/123/put/");
		
		assertEquals(3, path.length);
		assertEquals("get", path[0]);
		assertEquals("123", path[1]);
		assertEquals("put", path[2]);
	}

}
