package uk.co.intec.beans;

import java.util.ArrayList;

import org.apache.commons.collections.list.TreeList;
import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;

import uk.co.intec.utils.AppUtils;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.timtripcony.AbstractSmartDocumentModel;

public class KeyDateBean extends AbstractSmartDocumentModel {
	private static final long serialVersionUID = 1L;

	@Override
	public String getFormDescription() {
		return "Key Date";
	}

	@Override
	public String getFormName() {
		return "KeyDate";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean postSave() {
		ArrayList<String> customers = (ArrayList<String>) ExtLibUtil.getApplicationScope().get("customers");
		customers.add(getStringValue("customer"));
		return true;
	}

	public TreeList lookupContact() {
		TreeList retVal_ = new TreeList();
		try {
			// Don't bother if no customer
			if (isBlank("customer")) {
				return retVal_;
			}
			Database currDb = AppUtils.getAppDb();
			View lkupView = currDb.getView("(luCustomers)");
			ViewEntryCollection ec = lkupView.getAllEntriesByKey(getStringValue("customer"), true);
			if (null == ec.getFirstEntry()) {
				return retVal_;
			} else {
				ArrayList<String> keys = new ArrayList<String>();
				keys.add(getStringValue("customer"));
				keys.add(getStringValue("contact"));
				ec = lkupView.getAllEntriesByKey(keys, false);
				for (ViewEntry ent : ec) {
					retVal_.add(ent.getColumnValue("contact"));
				}
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

}
