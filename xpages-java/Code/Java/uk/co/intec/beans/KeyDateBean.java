package uk.co.intec.beans;

import java.util.ArrayList;

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

}
