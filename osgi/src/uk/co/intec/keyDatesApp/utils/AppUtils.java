package uk.co.intec.keyDatesApp.utils;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.vaadin.server.VaadinServlet;

public class AppUtils {

	public AppUtils() {

	}

	/**
	 * Gets the current user session<br/>
	 * <b>NOTE:</b> Ensure code using this has already called
	 * startDominoThread()
	 *
	 * @return org.openntf.domino.Session current user's Session
	 */
	public static Session getUserSession() {
		return Factory.getSession(SessionType.CURRENT);
	}

	/**
	 * Gets the data database based on the context parameter dataDbFilePath<br/>
	 * <b>NOTE:</b> Ensure code using this has already called
	 * startDominoThread()
	 *
	 * @return org.openntf.domino.Database NSF that stores the data
	 */
	public static Database getDataDb() {
		Database retVal_ = null;
		try {
			final Session userSess = getUserSession();
			final String filePath = getDataDbFilepath();
			retVal_ = userSess.getDatabase(userSess.getServerName(), filePath, false);
		} catch (final Exception e) {
			// TODO: handle exception
		}
		return retVal_;
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
