package mta.cats.service;

import java.io.IOException;
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
import mta.cats.model.TopFeeder;

import com.google.gson.Gson;

public class TopService extends HttpServlet implements IService {
	 private static final long TOP_USERS = 10L;
	private static final Object TOP_FEEDERS_ACTION = "feeders";

	/**
     * @see HttpServlet#HttpServlet()
     */
    public TopService() {
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
		
		if (TOP_FEEDERS_ACTION.equals(action)) {
			List<TopFeeder> feeders = FeedersDAO.getInstance().selectTop(TOP_USERS);
			r.payload = g.toJson(feeders);
		}
		else {
			r.message = "Unknown action: " + action;
		}
		
		response.setContentType("application/json; charset=UTF-8");
		response.getWriter().write(g.toJson(r));
		
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
