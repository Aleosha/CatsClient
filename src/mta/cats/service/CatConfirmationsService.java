package mta.cats.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import mta.cats.dao.impl.CatConfirmationsDAO;
import mta.cats.dao.impl.CatsDAO;
import mta.cats.dao.impl.FeedersDAO;
import mta.cats.dao.impl.UsersDAO;
import mta.cats.model.Cat;
import mta.cats.model.CatConfirmation;
import mta.cats.model.Feeder;

import com.google.gson.Gson;

/**
 * Servlet for all feeder actions
 */
public class CatConfirmationsService extends Service implements IService {


    /**
     * @see HttpServlet#HttpServlet()
     */
    public CatConfirmationsService() {
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
			// Feeder for cat
			if (pathParameters.length > 1) {
				Cat cat = new Cat();
				cat.setId(super.getIdParameter(pathParameters));
				Long count = CatConfirmationsDAO.getInstance().selectCountByCat(cat);
				r.payload = g.toJson(count);
			}
			else {
				r.payload = g.toJson(CatConfirmationsDAO.getInstance().selectCounts());
			}
		}
		else if (ADD_ACTION.equals(action)) {
			addConfirmation(request, r);
		}
		else {
			r.message = "Unknown action: " + action;
		}
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(g.toJson(r));
		
	}

	/**
	 * Add new confirmation
	 * @param request
	 * @param r
	 */
	private void addConfirmation(HttpServletRequest request, ResponseWrapper r) {
		try {
			Long userId = Long.parseLong(request.getParameter("userId"));
			Long catId = Long.parseLong(request.getParameter("catId"));
			
			if (UsersDAO.getInstance().select(userId) == null) {
				r.message = "Invalid user ID";
			}
			else if (CatsDAO.getInstance().select(catId) == null) {
				r.message = "Invalid cat ID";
			}
			else {
				CatConfirmation model = new CatConfirmation();
				model.setUserId(userId);
				model.setCatId(catId);
				
				model = CatConfirmationsDAO.getInstance().create(model);
				if (model == null) {
					r.message = "Unable to add confirmation";
				}
				else {
					r.message = "Confirmation added";
				}
			}
		}
		catch (NumberFormatException nfe) {
			r.message = "Invalid parameter while adding confirmation";
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
