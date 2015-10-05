package uk.co.intec.beans;

import java.util.ArrayList;

import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.timtripcony.AbstractSmartDocumentModel;

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
 *         Wrapper for the Key Date Document being created / read / edited
 * 
 */
public class KeyDateBean extends AbstractSmartDocumentModel {
	private static final long serialVersionUID = 1L;

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractDocumentMapModel#getFormDescription()
	 */
	@Override
	public String getFormDescription() {
		return "Key Date";
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractDocumentMapModel#getFormName()
	 */
	@Override
	public String getFormName() {
		return "KeyDate";
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractDocumentMapModel#postSave()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean postSave() {
		ArrayList<String> customers = (ArrayList<String>) ExtLibUtil.getApplicationScope().get("customers");
		customers.add(getStringValue("customer"));
		return true;
	}

}
