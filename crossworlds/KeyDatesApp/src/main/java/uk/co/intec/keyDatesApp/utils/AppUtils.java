package uk.co.intec.keyDatesApp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import com.vaadin.server.VaadinServlet;

import uk.co.intec.keyDatesApp.MainUI;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Application utils with no Domino-specific code.
 *
 */
public class AppUtils {

	public AppUtils() {

	}

	/**
	 * Gets the context parameter dataDbFilePath from web.xml
	 *
	 * @return String path of NSF that stores the data, on this server
	 */
	public static String getDataDbFilepath() {
		return VaadinServlet.getCurrent().getServletContext().getInitParameter("dataDbFilePath");
	}

	/**
	 * Reads a parameter from the browser
	 *
	 * @param paramName
	 *            String parameter to check for
	 * @return String parameter value
	 */
	public static String readParameter(String paramName) {
		final Map<String, String[]> params = MainUI.get().getRequest().getParameterMap();
		if (params.containsKey(paramName)) {
			return params.get(paramName).toString();
		} else {
			return "";
		}
	}

	/**
	 * Converts a date to a standard system-wide format
	 *
	 * @param passedDate
	 *            java.util.Date date passed
	 * @return String formatted to dd/MM/yyyy
	 */
	public static String convertDate(Date passedDate) {
		final SimpleDateFormat DATE_FORMAT_DATE_ONLY = new SimpleDateFormat("dd/MM/yyyy");
		return DATE_FORMAT_DATE_ONLY.format(passedDate);
	}

	/**
	 * Converts a date-time to a standard system-wide format
	 *
	 * @param passedDate
	 *            java.util.Date date passed
	 * @return String formatted to dd/MM/yyyy hh:mm:ss
	 */
	public static String convertDateAndTime(Date passedDate) {
		final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		return DATE_FORMAT.format(passedDate);
	}

}
