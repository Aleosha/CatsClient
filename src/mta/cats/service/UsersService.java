package mta.cats.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.cats.dao.impl.UsersDAO;
import mta.cats.model.User;

import com.google.gson.Gson;

/**
 * Servlet for all user actions
 */
public class UsersService extends Service implements IService {

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UsersService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathParameters = ServiceUtils.parsePath(request.getPathInfo());
		
		Gson g = new Gson();
		
		ResponseWrapper r = new ResponseWrapper();
		
		if (pathParameters.length == 0) {
			r.message = "No parameters provided";
		}
		final String action = pathParameters[ACTION_INDEX];
		if (GET_ACTION.equals(action)) {
			// Single user
			if (pathParameters.length > 1) {
				Long userId = getIdParameter(pathParameters);
				User user = UsersDAO.getInstance().select(userId);
				r.payload = g.toJson(user);
			}
		}
		else if (ADD_ACTION.equals(action)) {
			addUser(request, g, r);
		}
		else if (UPDATE_ACTION.equals(action)) {
			updateUser(request, r);
		}
		else if (DELETE_ACTION.equals(action)) {
			Long userId = getIdParameter(pathParameters);
			deleteUser(r, userId);
		}
		else {
			r.message = "Unknown action: " + action;
		}
		
		response.setContentType("appliuserion/json; charset=UTF-8");
		response.getWriter().write(g.toJson(r));
		
	}

	private void deleteUser(ResponseWrapper r, Long userId) {
		User user = new User();
		user.setId(userId);
		
		int deleted = UsersDAO.getInstance().delete(user);
		if (deleted == 0) {
			r.message = "Unable to delete user";
		}
		else {
			r.message = "User successfully deleted";
		}
	}


	
	private void addUser(HttpServletRequest request, Gson g, ResponseWrapper r) {
		User user = new User();
		try {
			if (request.getParameter("username") == null || request.getParameter("username").trim().length() == 0) {
				r.message = "Unable to add a new user, username not provided";
			}
			else if (request.getParameter("password") == null || request.getParameter("password").trim().length() == 0) {
				r.message = "Unable to add a new user, password not provided";
			}
			else {
				user.setUsername(request.getParameter("username"));
				user.setPassword(request.getParameter("password"));
				
				// Parse location, if provided
				parseLocation(request, user);
				
				user = UsersDAO.getInstance().create(user);
				if (user == null) {
					r.message = "Unable to add new user";
				}
				else {
					r.message = "User added";
					r.payload = g.toJson(user);
				}
			}
		}
		catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	/**
	 * Sets location of the user, if it was provided and is correct
	 * For user the location is not mandatory
	 * @param request
	 * @param user
	 */
	private void parseLocation(HttpServletRequest request, User user) {
		if (request.getParameter(LOCATION_X) == null && request.getParameter(LOCATION_Y) != null) {
			try {
				Float locationX = Float.parseFloat(request.getParameter(LOCATION_X));
				user.setLocationX(locationX);
				Float locationY = Float.parseFloat(request.getParameter(LOCATION_Y));
				user.setLocationY(locationY);
			}
			catch (NumberFormatException nfe) {
				user.setLocationX(null);
				user.setLocationY(null);
				nfe.printStackTrace();
			}
		}
	}

	/**
	 * Update user with request data
	 * @param request
	 * @param r
	 */
	private void updateUser(HttpServletRequest request, ResponseWrapper r) {
		User user = new User();
		try {
			if (request.getParameter(ID) == null) {
				r.message = "Unable to add a new user, louserion not provided or incorrect";
			}
			else {
				Long userId = Long.parseLong(request.getParameter(ID));
				user.setId(userId);
				user.setUsername(request.getParameter("username"));
				user.setPassword(request.getParameter("password"));
				parseLocation(request, user);
			
				int updated = UsersDAO.getInstance().update(user);
				if (updated == 0) {
					r.message = "Unable to update user";
				}
				else {
					r.message = "User updated";
					
				}
			}
		}
		catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
