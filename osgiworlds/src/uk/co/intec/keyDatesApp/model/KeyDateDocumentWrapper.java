package uk.co.intec.keyDatesApp.model;

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

import java.util.Date;
import java.util.HashMap;

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
public class KeyDateDocumentWrapper extends AbstractSmartDocumentModel {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public KeyDateDocumentWrapper() {
		super();
	}

	/**
	 * Overloaded constructor, setting documentId and editMode based on
	 * parameters.
	 *
	 * @param documentId
	 *            String Note ID or UNID
	 * @param editMode
	 *            boolean edit mode
	 */
	public KeyDateDocumentWrapper(String documentId, boolean editMode) {
		super(documentId, editMode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.timtripcony.AbstractDocumentMapModel#getFormDescription()
	 */
	@Override
	public String getFormDescription() {
		return "Key Date";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.timtripcony.AbstractDocumentMapModel#getFormName()
	 */
	@Override
	public String getFormName() {
		return "KeyDate";
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.timtripcony.AbstractDocumentMapModel#initialiseTypeConversionMap()
	 */
	@Override
	public void initialiseTypeConversionMap() {
		final HashMap<String, Class<?>> map = new HashMap<String, Class<?>>();
		map.put("date", Date.class);
		setTypeConversionMap(map);
	}

}
