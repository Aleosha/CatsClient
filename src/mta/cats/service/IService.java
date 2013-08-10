package mta.cats.service;

/**
 * Defines interface for all services
 * @author i064039
 *
 */
public interface IService {
	static final String DELETE_ACTION = "delete";
	 static final String UPDATE_ACTION = "update";
	 static final String ID = "id";
	
	 static final String LOCATION_Y = "location-y";
	 static final String LOCATION_X = "location-x";
	 static final String GET_ACTION = "get";
	 static final String ADD_ACTION = "add";
	 static final long serialVersionUID = 1L;
	 static final int ACTION_INDEX = 0;
	 static final int ID_INDEX = 1;
}
