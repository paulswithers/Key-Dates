package com.timtripcony;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.collections.map.CaseInsensitiveMap;

import com.ibm.xsp.model.DataObject;

/**
 * "Borrowed" from Tim's NotesIn9 series, episodes 133-135
 * 
 * Tim, you were a genius. You may be gone but will never be forgotten
 */
public abstract class AbstractMapModel implements Serializable, DataObject {
	private static final long serialVersionUID = 1L;
	private Map<Object, Object> values;

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

	public Object getValue(Object key) {
		return getValues().get(key);
	}

	public Map<Object, Object> getValues() {
		if (values == null) {
			values = new CaseInsensitiveMap();
		}
		return values;
	}

	public boolean isReadOnly(Object key) {
		return false;
	}

	public void setValue(Object key, Object value) {
		getValues().put(key, value);

	}

}
