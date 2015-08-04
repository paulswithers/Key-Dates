package uk.co.intec.keyDatesApp.utils;

import org.openntf.domino.utils.DominoUtils;

import com.vaadin.server.VaadinServlet;

public class AppUtils {

	public AppUtils() {

	}

	/**
	 * Gets the context parameter dataDbFilePath
	 *
	 * @return String path of NSF that stores the data, on this server
	 */
	public static String getDataDbFilepath() {
		return VaadinServlet.getCurrent().getServletContext().getInitParameter("dataDbFilePath");
	}

	public static void handleException(Throwable t) {
		DominoUtils.handleException(t);
	}

}
