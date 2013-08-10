package mta.cats.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import mta.cats.db.SchemaInitializer;

/**
 * This class listens to webapplication startup and initialized the DB
 * @author i064039
 *
 */
public class SchemaInitializationListener implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		SchemaInitializer.initialize();
	}

}
