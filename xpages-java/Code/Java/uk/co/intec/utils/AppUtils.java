package uk.co.intec.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.XspOpenLogUtil;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.application.ApplicationEx;
import com.ibm.xsp.extlib.util.ExtLibUtil;

/*
	Copyright 2015 Paul Withers Licensed under the Apache License, Version 2.0
	(the "License"); you may not use this file except in compliance with the
	License. You may obtain a copy of the License at

	http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
	or agreed to in writing, software distributed under the License is distributed
	on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
	express or implied. See the License for the specific language governing
	permissions and limitations under the License
*/

/**
 * @author Paul Withers
 * 
 *         Generic application utility methods
 * 
 */
public class AppUtils {
	private static String dataDbPath;

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

	/**
	 * Logs to OpenLog
	 * 
	 * @param t
	 *            Throwable exception / error
	 */
	public static void handleException(Throwable t) {
		XspOpenLogUtil.logError(t);
	}

	/**
	 * Opens the data database, based on the filepath returned from {@link getDataDbPath()}
	 * 
	 * @return Database containing all the documents for this application
	 */
	public static Database getDataDb() {
		Database retVal_ = null;
		try {
			Session currSess = Factory.getSession();
			retVal_ = currSess.getDatabase(getDataDbPath());
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Getter for dataDbPath variable
	 * 
	 * @return String path to database containing the documents for this application
	 */
	public static String getDataDbPath() {
		if (StringUtils.isEmpty(dataDbPath)) {
			setDataDbPath();
		}
		return dataDbPath;
	}

	/**
	 * Setter for dataDbPath variable, loading from xsp.property value "dataDbPath"
	 */
	public static void setDataDbPath() {
		try {
			// Compute as required
			String dataDbPathTmp = getXspProperty("dataDbPath", ExtLibUtil.getCurrentDatabase().getFilePath());
			dataDbPath = dataDbPathTmp;
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
	}

	/**
	 * Displays a validation error message to the user on the XPage
	 * 
	 * @param message
	 *            String message to display to the user
	 */
	public static void addFacesMessage(String message) {
		FacesContext.getCurrentInstance().addMessage("",
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

	/**
	 * Get a specific document by passing either Note ID or UNID, returning null if nothing found (unlike core API
	 * functionality, which throws an error")
	 * 
	 * @param unid
	 *            String Note ID or UNID
	 * @return Document or null
	 */
	public static Document getDocumentByNoteID_Or_UNID(final String unid) {
		Document doc;
		doc = AppUtils.getDataDb().getDocumentByUNID(unid);
		if (doc == null) {
			try {
				doc = AppUtils.getDataDb().getDocumentByID(unid);
			} catch (Throwable te) {
				// Just couldn't get doc
			}
		}
		return doc;
	}

	/**
	 * Calls ExtLibUtil.resolveVariable, allowing developer to just pass the variable name
	 * 
	 * @param name
	 *            String variable to return
	 * @return Object that corresponds to the variable name. Still needs casting to required class
	 */
	public static Object resolveVariable(String name) {
		return ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), name);
	}

	/**
	 * Calls {@link getIniVar()} to find an xsp.property
	 * 
	 * @param propertyName
	 *            String xsp.property variable to look for
	 * @param defaultValue
	 *            String default value to use, if variable is set set
	 * @return String xsp.property value
	 */
	private static String getXspProperty(String propertyName, String defaultValue) {
		String retVal = ApplicationEx.getInstance().getApplicationProperty(propertyName,
				getIniVar(propertyName, defaultValue));
		return retVal;
	}

	/**
	 * Looks to xsp.properties file in this NSF
	 * 
	 * @param propertyName
	 *            String xsp.property variable to look for
	 * @param defaultValue
	 *            String default value to use, if variable is set set
	 * @return String xsp.property value
	 */
	private static String getIniVar(String propertyName, String defaultValue) {
		String newVal = Factory.getSession().getEnvironmentString(propertyName, true);
		if (StringUtil.isNotEmpty(newVal)) {
			return newVal;
		} else {
			return defaultValue;
		}
	}

}
