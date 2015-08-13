package org.openntf.osgiworlds.model;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import uk.co.intec.keyDatesApp.utils.AppUtils;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Class for generic Domino-centric utilities.
 *
 */
public class GenericDatabaseUtils {

	public GenericDatabaseUtils() {

	}

	/**
	 * Gets the current user session
	 *
	 * @return Session current user's Session
	 */
	public static Session getUserSession() {
		return Factory.getSession(SessionType.CURRENT);
	}

	/**
	 * Gets the data database based on the context parameter dataDbFilePath
	 *
	 * @return Database NSF that stores the data
	 */
	public static Database getDataDb() {
		Database retVal_ = null;
		try {
			final Session userSess = getUserSession();
			final String filePath = AppUtils.getDataDbFilepath();
			retVal_ = userSess.getDatabase(userSess.getServerName(), filePath, false);
		} catch (final Exception e) {
			// TODO: handle exception
		}
		return retVal_;
	}

	/**
	 * Gets the Session's effectiveUserName in common name format
	 *
	 * @return String user name
	 */
	public static String getUserName() {
		final String name = getUserSession().getEffectiveUserName();
		return DominoUtils.toCommonName(name);
	}

	/**
	 * Checks whether the database exists at the filepath defined for the
	 * application
	 *
	 * @return boolean whether or not an NSF can be found at the relevant
	 *         filepath
	 */
	public static boolean doesDbExist() {
		if (null == getDataDb()) {
			return false;
		} else {
			return true;
		}
	}
}
