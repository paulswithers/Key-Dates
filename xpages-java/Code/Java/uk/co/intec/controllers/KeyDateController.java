package uk.co.intec.controllers;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;

import uk.co.intec.beans.KeyDateBean;
import uk.co.intec.utils.AppUtils;

import com.ibm.xsp.extlib.util.ExtLibUtil;

public class KeyDateController implements Serializable {
	private static final long serialVersionUID = 1L;
	private KeyDateBean dataBean;

	public KeyDateController() {
		try {
			AppUtils.initCusts();
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
	}

	public void setDataBean() {
		dataBean = (KeyDateBean) AppUtils.resolveVariable("keyDateBean");
	}

	public KeyDateBean getDataBean() {
		if (null == dataBean) {
			setDataBean();
		}
		return dataBean;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<String> getCustomers() {
		return (ArrayList<String>) ExtLibUtil.getApplicationScope().get("customers");
	}

	public ArrayList<String> getContacts() {
		ArrayList<String> retVal_ = new ArrayList<String>();
		try {
			String customer = getDataBean().getStringValue("customer");
			Database currDb = AppUtils.getAppDb();
			View lkupView = currDb.getView("(luCustomers)");
			ViewNavigator nav = lkupView.createViewNavFromCategory(customer);
			ViewEntry ent = nav.getFirst();
			while (null != ent) {
				if (StringUtils.isNotEmpty((String) ent.getColumnValue("contact"))) {
					retVal_.add((String) ent.getColumnValue("contact"));
				}
				ent = nav.getNextSibling();
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

}
