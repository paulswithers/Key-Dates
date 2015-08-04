package org.openntf.osgiworlds.model;

import org.openntf.domino.Database;
import org.openntf.domino.Session;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import uk.co.intec.keyDatesApp.utils.AppUtils;

public class GenericDatabaseUtils {

	public GenericDatabaseUtils() {

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
			final String filePath = AppUtils.getDataDbFilepath();
			retVal_ = userSess.getDatabase(userSess.getServerName(), filePath, false);
		} catch (final Exception e) {
			// TODO: handle exception
		}
		return retVal_;
	}

	public static String getUserName() {
		final String name = getUserSession().getEffectiveUserName();
		return DominoUtils.toCommonName(name);
	}

	public static boolean doesDbExist() {
		if (null == getDataDb()) {
			return false;
		} else {
			return true;
		}
	}
}
