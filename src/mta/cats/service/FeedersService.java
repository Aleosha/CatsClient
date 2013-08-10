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
import mta.cats.dao.impl.FeedersDAO;
import mta.cats.dao.impl.UsersDAO;
import mta.cats.model.Cat;
import mta.cats.model.Feeder;

import com.google.gson.Gson;

/**
 * Servlet for all feeder actions
 */
public class FeedersService extends Service implements IService {


    /**
     * @see HttpServlet#HttpServlet()
     */
    public FeedersService() {
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
				List<Feeder> feeders = FeedersDAO.getInstance().selectByCat(cat);
				r.payload = g.toJson(feeders);
			}
		}
		else if (ADD_ACTION.equals(action)) {
			createFeeder(request, r);
		}
		// Delete is done by specifying the ID of the feeder, and not the cat or the user
		else if (DELETE_ACTION.equals(action)) {
			try {
				Long feederId = super.getIdParameter(pathParameters);
				Feeder model = new Feeder();
				model.setId(feederId);
				int deleted = FeedersDAO.getInstance().delete(model);
				if (deleted == 0) {
					r.message = "Unable to delete feeder";
				}
				else {
					r.message = "Feeder added";
				}
			}
			catch (NumberFormatException nfe) {
				r.message = "Invalid feeder ID";
				nfe.printStackTrace();
			}
		}
		else {
			r.message = "Unknown action: " + action;
		}
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(g.toJson(r));
		
	}

	private void createFeeder(HttpServletRequest request, ResponseWrapper r) {
		try {
			Long userId = Long.parseLong(request.getParameter("userId"));
			Long catId = Long.parseLong(request.getParameter("catId"));
			Integer dayOfWeek = Integer.parseInt(request.getParameter("dayOfWeek"));
			
			if (dayOfWeek < 1 || dayOfWeek > 7) {
				r.message = "Invalid day of week: " + dayOfWeek;
			}
			else if (UsersDAO.getInstance().select(userId) == null) {
				r.message = "Invalid user ID";
			}
			else if (CatsDAO.getInstance().select(catId) == null) {
				r.message = "Invalid cat ID";
			}
			else {
				Feeder model = new Feeder();
				model.setUserId(userId);
				model.setCatId(catId);
				model.setDayOfWeek(dayOfWeek);
				model = FeedersDAO.getInstance().create(model);
				if (model == null) {
					r.message = "Unable to add feeder";
				}
				else {
					r.message = "Feeder added";
				}
			}
		}
		catch (NumberFormatException nfe) {
			r.message = "Invalid parameter while adding feeder";
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
