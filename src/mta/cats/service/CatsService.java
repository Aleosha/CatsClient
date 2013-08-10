package mta.cats.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.cats.dao.impl.CatsDAO;
import mta.cats.model.Cat;

import com.google.gson.Gson;

/**
 * Servlet for all cat actions
 */

public class CatsService extends Service implements IService {

	 static final String NICKNAME = "nickname";   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CatsService() {
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
			// Single cat
			if (pathParameters.length > 1) {
				Long catId = getIdParameter(pathParameters);
				if (catId == null) {
					r.message = "Invalid cat ID";
				}
				else {
					Cat cat = CatsDAO.getInstance().select(catId);
					r.payload = g.toJson(cat);
				}
			}
			// Cats around location
			else {
				if (request.getParameter(LOCATION_X) == null || request.getParameter(LOCATION_Y) == null)  {
					r.message = "Unable to retrieve cats, location not provided or incorrect";
				}
				else {
					try
					{
						Float locationX = Float.parseFloat(request.getParameter(LOCATION_X));
						Float locationY = Float.parseFloat(request.getParameter(LOCATION_Y));
						
						List<Cat> cats = CatsDAO.getInstance().selectInArea(locationX, locationY);
						
						r.payload = g.toJson(cats);
					}
					catch (NumberFormatException nfe) {
						r.message = "Incorrect location provided";
						nfe.printStackTrace();
					}
				}
			}
		}
		else if (ADD_ACTION.equals(action)) {
			addCat(request, g, r);
		}
		else if (UPDATE_ACTION.equals(action)) {
			updateCat(request, r);
		}
		else if (DELETE_ACTION.equals(action)) {
			Long catId = getIdParameter(pathParameters);
			deleteCat(r, catId);
		}
		else {
			r.message = "Unknown action: " + action;
		}
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(g.toJson(r));
		
	}

	private void deleteCat(ResponseWrapper r, Long catId) {
		Cat cat = new Cat();
		cat.setId(catId);
		
		int deleted = CatsDAO.getInstance().delete(cat);
		if (deleted == 0) {
			r.message = "Unable to delete cat";
		}
		else {
			r.message = "Cat successfully deleted";
		}
	}


	private void addCat(HttpServletRequest request, Gson g, ResponseWrapper r) {
		Cat cat = new Cat();
		try {
			if (request.getParameter(LOCATION_X) == null || request.getParameter(LOCATION_Y) == null)  {
				r.message = "Unable to add a new cat, location not provided or incorrect";
			}
			else if (request.getParameter(NICKNAME) == null || request.getParameter(NICKNAME).trim().length() == 0) {
				r.message = "Unable to add a new cat, nickname not provided";
			}
			else {
				Float locationX = Float.parseFloat(request.getParameter(LOCATION_X));
				Float locationY = Float.parseFloat(request.getParameter(LOCATION_Y));
				cat.setNickname(request.getParameter(NICKNAME));
				cat.setLocationX(locationX);
				cat.setLocationY(locationY);
			
				cat = CatsDAO.getInstance().create(cat);
				if (cat == null) {
					r.message = "Unable to add new cat";
				}
				else {
					r.message = "Cat added";
					r.payload = g.toJson(cat);
				}
			}
		}
		catch (NumberFormatException nfe) {
			r.message = "Invalid parameter received";
			nfe.printStackTrace();
		}
	}

	/**
	 * Update cat with request data
	 * @param request
	 * @param r
	 */
	private void updateCat(HttpServletRequest request, ResponseWrapper r) {
		Cat cat = new Cat();
		try {
			if (request.getParameter(ID) == null) {
				r.message = "Unable to add a new cat, location not provided or incorrect";
			}
			else if (request.getParameter(LOCATION_X) == null || request.getParameter(LOCATION_Y) == null)  {
				r.message = "Unable to add a new cat, location not provided or incorrect";
			}
			else if (request.getParameter(NICKNAME) == null || request.getParameter(NICKNAME).trim().length() == 0) {
				r.message = "Unable to add a new cat, nickname not provided";
			}
			else {
				Float locationX = Float.parseFloat(request.getParameter(LOCATION_X));
				Float locationY = Float.parseFloat(request.getParameter(LOCATION_Y));
				Long catId = Long.parseLong(request.getParameter(ID));
				String nickname = request.getParameter(NICKNAME);
				cat.setId(catId);
				cat.setNickname(nickname);
				cat.setLocationX(locationX);
				cat.setLocationY(locationY);
			
				int updated = CatsDAO.getInstance().update(cat);
				if (updated == 0) {
					r.message = "Unable to update cat";
				}
				else {
					r.message = "Cat updated";
					
				}
			}
		}
		catch (NumberFormatException nfe) {
			r.message = "Invalid number received";
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
