package org.openntf.osgiworlds.model;

/*

<!--
Copyright 2015 Paul Withers
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License
-->

*/

import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
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
		Session sess = Factory.getSession(SessionType.CURRENT);
		sess.setFixEnable(Fixes.APPEND_ITEM_VALUE, true);
		sess.setFixEnable(Fixes.DOC_UNID_NULLS, true);
		sess.setFixEnable(Fixes.FORCE_JAVA_DATES, true);
		sess.setFixEnable(Fixes.MIME_CONVERT, true);
		sess.setFixEnable(Fixes.ODA_NAMES, true);
		sess.setFixEnable(Fixes.REMOVE_ITEM, true);
		sess.setFixEnable(Fixes.REPLACE_ITEM_NULL, true);
		sess.setFixEnable(Fixes.VIEW_UPDATE_OFF, true);
		sess.setFixEnable(Fixes.VIEWENTRY_RETURN_CONSTANT_VALUES, true);
		return sess;
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

	/**
	 * Get a specific document by passing either Note ID or UNID, returning null
	 * if nothing found (unlike core API functionality, which throws an error")
	 *
	 * @param unid
	 *            String Note ID or UNID
	 * @return Document or null
	 */
	public static Document getDocumentByNoteID_Or_UNID(final String unid) {
		Document doc;
		doc = getDataDb().getDocumentByUNID(unid);
		if (doc == null) {
			try {
				doc = getDataDb().getDocumentByID(unid);
			} catch (final Throwable te) {
				// Just couldn't get doc
			}
		}
		return doc;
	}
}
