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

public class AppUtils {
	private static String dataDbPath;

	public static String convertDate(Date passedDate) {
		final SimpleDateFormat DATE_FORMAT_DATE_ONLY = new SimpleDateFormat("dd/MM/yyyy");
		return DATE_FORMAT_DATE_ONLY.format(passedDate);
	}

	public static String convertDateAndTime(Date passedDate) {
		final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
		return DATE_FORMAT.format(passedDate);
	}

	public static void handleException(Throwable t) {
		XspOpenLogUtil.logError(t);
	}

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

	public static String getDataDbPath() {
		if (StringUtils.isEmpty(dataDbPath)) {
			setDataDbPath();
		}
		return dataDbPath;
	}

	public static void setDataDbPath() {
		try {
			// Compute as required
			String dataDbPathTmp = getXspProperty("dataDbPath", ExtLibUtil.getCurrentDatabase().getFilePath());
			dataDbPath = dataDbPathTmp;
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
	}

	public static void addFacesMessage(String message) {
		FacesContext.getCurrentInstance().addMessage("",
				new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message));
	}

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

	public static Object resolveVariable(String name) {
		return ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), name);
	}

	private static String getXspProperty(String propertyName, String defaultValue) {
		String retVal = ApplicationEx.getInstance().getApplicationProperty(propertyName,
				getIniVar(propertyName, defaultValue));
		return retVal;
	}

	private static String getIniVar(String propertyName, String defaultValue) {
		String newVal = Factory.getSession().getEnvironmentString(propertyName, true);
		if (StringUtil.isNotEmpty(newVal)) {
			return newVal;
		} else {
			return defaultValue;
		}
	}

}
