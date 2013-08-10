package mta.cats.service;

public class ServiceUtils {

	public static final String PATH_DELIMITER = "/";
	
	/**
	 * Gets REST path and parses its parameters
	 * @param pathInfo
	 * @return parameters as an array
	 */
	public static String[] parsePath(String pathInfo) {
		if (pathInfo == null)
			// We would like to avoid returning null
			return new String[1];
		
		pathInfo = pathInfo.toLowerCase();
		// Remove the leading and the ending delimiters
		if (pathInfo.startsWith(PATH_DELIMITER))
			pathInfo = pathInfo.substring(1);
		if (pathInfo.endsWith(PATH_DELIMITER))
			pathInfo = pathInfo.substring(0, pathInfo.length() - 1);
		return pathInfo.split(PATH_DELIMITER);
	}

}
