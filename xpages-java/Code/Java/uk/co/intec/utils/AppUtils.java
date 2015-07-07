package uk.co.intec.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Database;
import org.openntf.domino.Document;
import org.openntf.domino.Session;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.XspOpenLogUtil;

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

	public static Database getAppDb() {
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
			// String dataDbPathTmp = Activator.getXspPropertyAsString("dataDbPath");
			Database thisDb = (Database) resolveVariable("database");
			String dataDbPathTmp = thisDb.getApiPath();
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
		doc = AppUtils.getAppDb().getDocumentByUNID(unid);
		if (doc == null) {
			try {
				doc = AppUtils.getAppDb().getDocumentByID(unid);
			} catch (Throwable te) {
				// Just couldn't get doc
			}
		}
		return doc;
	}

	public static void initCusts() {
		try {
			if (ExtLibUtil.getApplicationScope().containsKey("customers")) {
				return;
			} else {
				ArrayList<String> opts = new ArrayList<String>();
				Database currDb = getAppDb();
				View lkupView = currDb.getView("(luCustomers)");
				ViewNavigator nav = lkupView.createViewNavMaxLevel(0);
				ViewEntry ent = nav.getFirst();
				// ODA doesn't have iterator for ViewNavigators, no simple concept of "next"
				// May be next entry, next sibling etc
				while (null != ent) {
					if (StringUtils.isNotEmpty((String) ent.getColumnValue("customer"))) {
						opts.add((String) ent.getColumnValue("customer"));
					}
					ent = nav.getNextSibling();
				}
				ExtLibUtil.getApplicationScope().put("customers", opts);
			}
		} catch (Throwable t) {
			handleException(t);
		}
	}

	public static Object resolveVariable(String name) {
		return ExtLibUtil.resolveVariable(FacesContext.getCurrentInstance(), name);
	}

}
