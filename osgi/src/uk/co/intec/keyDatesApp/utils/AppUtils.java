package uk.co.intec.keyDatesApp.utils;

import com.vaadin.server.VaadinServlet;

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

}
