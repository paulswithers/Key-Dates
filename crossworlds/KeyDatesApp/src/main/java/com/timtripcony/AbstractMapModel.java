package com.timtripcony;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;

/* 
Copyright 2015 Tim Tripcony, Paul Withers
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0 

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License
*/

/**
 * @author Tim Tripcony, Paul Withers
 * 
 *         Extension of DataObject and Serializable to allow easy access to
 *         properties of the Document
 */
public abstract class AbstractMapModel implements Serializable {
	private static final long serialVersionUID = 1L;
	private Map<Object, Object> values;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#getType(java.lang.Object)
	 */
	public Class<?> getType(final Object key) {
		Class<?> result = null;
		if (getValues().containsKey(key)) {
			Object value = getValues().get(key);
			if (value != null) {
				result = value.getClass();
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#getValue(java.lang.Object)
	 */
	public Object getValue(Object key) {
		return getValues().get(key);
	}

	/**
	 * Retrieves a Map of all Items within the Document. Extended by PW to use
	 * CaseInsensitiveMap. <br/>
	 * This allows the code to deal with existing Documents or fields set via
	 * LotusScript, were the case of the Item name may be unreliable
	 * 
	 * @return Map<Object,Object> Key=Item name, Value=Item value(s)
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, Object> getValues() {
		if (values == null) {
			values = new CaseInsensitiveMap();
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#isReadOnly(java.lang.Object)
	 */
	public boolean isReadOnly(Object key) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ibm.xsp.model.DataObject#setValue(java.lang.Object,
	 * java.lang.Object)
	 */
	public void setValue(Object key, Object value) {
		getValues().put(key, value);

	}

}
