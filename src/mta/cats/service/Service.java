package mta.cats.service;

import javax.servlet.http.HttpServlet;

public abstract class Service extends HttpServlet implements IService {

	/**
	 * Returns entity ID from the parsed request parameters
	 * @param pathParameters - parsed parameters 
	 * @return ID if it's parsed, null otherwise
	 */
	protected Long getIdParameter(String[] pathParameters) {
		try {
			if (pathParameters.length + 1 < ID_INDEX) {
				return null;
			}
			return Long.parseLong(pathParameters[ID_INDEX]);
		}
		catch (NumberFormatException nre) {
			nre.printStackTrace();
			return null;
		}
	}
}
