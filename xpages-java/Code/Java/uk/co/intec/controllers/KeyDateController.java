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
 *         "Controller" class to handle page-specific logic for KeyDate XPage
 * 
 */
public class KeyDateController implements Serializable {
	private static final long serialVersionUID = 1L;
	private KeyDateBean dataBean;

	/**
	 * Constructor
	 */
	public KeyDateController() {

	}

	/**
	 * Getter to provide easy access from within this Controller to the data bean for this page
	 * 
	 * @return KeyDateBean corresponding to the KeyDate Document being created / read / edited
	 */
	public KeyDateBean getDataBean() {
		if (null == dataBean) {
			setDataBean();
		}
		return dataBean;
	}

	/**
	 * Setter to load the data bean for easy access from this Controller
	 */
	public void setDataBean() {
		dataBean = (KeyDateBean) AppUtils.resolveVariable("keyDateBean");
	}

	/**
	 * Gets a unique list of customers Key Dates have been created for
	 * 
	 * @return ArrayList<String> unique customers
	 */
	public ArrayList<String> getCustomers() {
		ArrayList<String> retVal_ = new ArrayList<String>();
		try {
			Database currDb = AppUtils.getDataDb();
			View lkupView = currDb.getView("(luCustomers)");
			ViewNavigator nav = lkupView.createViewNavMaxLevel(0);
			ViewEntry ent = nav.getFirst();
			// ODA doesn't have iterator for ViewNavigators, no simple concept of "next"
			// May be next entry, next sibling etc
			while (null != ent) {
				if (StringUtils.isNotEmpty((String) ent.getColumnValue("customer"))) {
					retVal_.add((String) ent.getColumnValue("customer"));
				}
				ent = nav.getNextSibling();
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Gets a unique list of contacts Key Dates have been created for, based on the selected customer
	 * 
	 * @return ArrayList<String> unique contacts
	 */
	public ArrayList<String> getContacts() {
		ArrayList<String> retVal_ = new ArrayList<String>();
		try {
			String customer = getDataBean().getStringValue("customer");
			Database currDb = AppUtils.getDataDb();
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
