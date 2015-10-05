package com.timtripcony;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Vector;

import javax.faces.context.FacesContext;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.openntf.domino.DateTime;
import org.openntf.domino.Document;

import uk.co.intec.utils.AppUtils;

import com.ibm.commons.util.StringUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;

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
 *         Extension of AbstractDocumentMapModel to add "smart" functionality
 */
public abstract class AbstractSmartDocumentModel extends AbstractDocumentMapModel {
	private static final long serialVersionUID = 1L;
	private boolean editMode;
	private boolean editable; // Because I keep forgetting!!

	/**
	 * Constructor, calling overloaded constructor and simulating DominoDocument datasource, to get default Document
	 * based on URL
	 */
	public AbstractSmartDocumentModel() {
		this(false, "", "", false);
	}

	/**
	 * Overloaded constructor, added by PW, allowing loading based on ignoreRequestParams, requestParamPrefix documentId
	 * and editMode URL parameter
	 * 
	 * @param ignoreRequestParams
	 *            boolean whether or not to load UNID, action etc from URL parameters
	 * @param requestParamPrefix
	 *            String alternative requestParamPrefix for UNID, action etc URL parameters
	 * @param documentId
	 *            String alternative UNID / Note ID from which to load Document
	 * @param editMode
	 *            boolean whether or not to put the document in edit mode
	 */
	public AbstractSmartDocumentModel(boolean ignoreRequestParams, String requestParamPrefix, String documentId,
			boolean editMode) {
		super(ignoreRequestParams, requestParamPrefix, documentId);
		setEditMode(editMode);
		if (!ignoreRequestParams) {
			// TODO: Outside the scope of this, but we need to handle different requestParamPrefix
			// Currently uses the action parameter of main doc
			String action = ExtLibUtil.readParameter(FacesContext.getCurrentInstance(), "action");
			if ("editDocument".equalsIgnoreCase(action)) {
				setEditMode(true);
			}
		}
		if (StringUtil.isEmpty(getUnid()) || isEditMode()) {
			if (!isReadOnly()) {
				edit();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractDocumentMapModel#load(java.lang.String)
	 */
	@Override
	public void load(final String unid) {
		super.load(unid);
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractDocumentMapModel#initNew()
	 */
	@Override
	public void initNew() {
		super.initNew();
	}

	/**
	 * Method to retrieve backend document
	 * 
	 * @return Document backend document
	 */
	public Document getBEDoc() {
		Document retVal_ = null;
		if (StringUtils.isNotEmpty(getUnid())) {
			retVal_ = AppUtils.getDocumentByNoteID_Or_UNID(getUnid());
		}
		return retVal_;
	}

	/**
	 * Puts the document in edit mode
	 */
	public void edit() {
		setEditMode(true);
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractMapModel#getType(java.lang.Object)
	 */
	@Override
	public Class<?> getType(final Object key) {
		Class<?> result = null;
		try {
			result = PropertyUtils.getPropertyType(this, key.toString());
		} catch (Throwable t) {
			result = super.getType(key);
		}
		return result;
	}

	/**
	 * Gets a field value, casting it to a specific Java class
	 * 
	 * @param name
	 *            String Item name to get value from
	 * @param type
	 *            Class to cast the value to
	 * @return Item value cast to relevant Java class
	 */
	public <T> T getTypedValue(final String name, final Class<T> type) {
		return type.cast(getValues().get(name));
	}

	/**
	 * Gets a field value cast to a String, returning a blank String if the field does not exist or an error occurs
	 * 
	 * @param name
	 *            String Item name to get value from
	 * @return String value of the Item
	 */
	public String getStringValue(final String name) {
		if (!hasField(name)) {
			return "";
		}
		try {
			return getTypedValue(name, String.class);
		} catch (Throwable t) {
			return "";
		}
	}

	/**
	 * Gets a field value cast to a Date, returning null if the field is a String or does not exist
	 * 
	 * @param name
	 *            String Item name to get value from
	 * @return Date value of the Item
	 */
	public Date getDateValue(final String name) {
		if (!hasField(name)) {
			return null;
		}
		if (String.class.equals(getType(name))) {
			return null;
		}
		return getTypedValue(name, Date.class);
	}

	/**
	 * Gets a Date/Time field value as a Date, returning in date only or date and time format using {@link
	 * AppUtils.convertDateAndTime()} or {@link AppUtils.convertDate()}
	 * 
	 * @param name
	 *            String Item name to get value from
	 * @param includeTime
	 *            boolean whether or not to include time in the output
	 * @return String value of the Item including just date or date/time
	 */
	public String getDateString(final String name, final boolean includeTime) {
		String retVal_ = "";
		try {
			Date dt;
			if (Date.class.equals(getValue(name).getClass())) {
				dt = getTypedValue(name, Date.class);
			} else {
				DateTime dtTime = getTypedValue(name, DateTime.class);
				dt = dtTime.toJavaDate();
			}
			if (includeTime) {
				retVal_ = AppUtils.convertDateAndTime(dt);
			} else {
				retVal_ = AppUtils.convertDate(dt);
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Gets a field or multi-value field as a String, using toString() method
	 * 
	 * @param name
	 *            String Item name to get value from
	 * @return String value of the Item
	 */
	public String getValueAsString(final String name) {
		String retVal_ = "";
		try {
			if (null != getValue(name)) {
				if (Vector.class.equals(getValue(name).getClass())) {
					String value = getValue(name).toString();
					value = StringUtils.substringAfter(value, "[");
					retVal_ = StringUtils.substringBeforeLast(value, "]");
				} else {
					retVal_ = getValue(name).toString();
				}
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractMapModel#getValue(java.lang.Object)
	 */
	@Override
	public Object getValue(final Object key) {
		Object result = null;
		try {
			result = PropertyUtils.getProperty(this, key.toString());
		} catch (Throwable t) {
			result = super.getValue(key);
		}
		return result;
	}

	/**
	 * Getter for editMode
	 * 
	 * @return boolean whether or not the Document is in edit mode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * Setter for editMode
	 * 
	 * @param editMode
	 *            boolean whether or not the Document is in edit mode
	 */
	public void setEditMode(final boolean editMode) {
		this.editMode = editMode;
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractMapModel#isReadOnly(java.lang.Object)
	 */
	@Override
	public boolean isReadOnly(final Object key) {
		boolean result = !isEditMode() || super.isReadOnly(key);
		try {
			if (!result) {
				result = PropertyUtils.getWriteMethod(PropertyUtils.getPropertyDescriptor(this, key.toString())) == null;
			}
		} catch (Throwable t) {
			result = super.isReadOnly(key);
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractMapModel#setValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setValue(final Object key, final Object value) {
		try {
			PropertyUtils.setProperty(this, key.toString(), value);
		} catch (Throwable t) {
			super.setValue(key, value);
		}
	}

	/**
	 * Getter for editable
	 * 
	 * @return boolean whether or not the document is in edit mode (because muscle memory makes me use "editable"
	 */
	public boolean isEditable() {
		return editMode;
	}

	/**
	 * Checks whether a text field is blank
	 * 
	 * @param itemName
	 *            String Item name to check whether or not it's empty
	 * @return boolean, whether Item is blank or not
	 */
	public boolean isBlank(String itemName) {
		boolean retVal_ = true;
		try {
			Object valueToCheck = this.getValue(itemName);
			if (null == valueToCheck) {
				retVal_ = true;
			} else if (valueToCheck instanceof String) {
				// String, just check it
				if (!StringUtils.isEmpty((String) valueToCheck)) {
					retVal_ = false;
				}
			} else if (valueToCheck instanceof Vector) {
				// Vector, try to get first entry as String and check it
				// This could be refactored to check "[]".equals(vector.toString() - But I've changed setValue() to create a String instead of a Vector, so not sure how to test this
				if ("[]".equals(valueToCheck.toString())) {
					setValue(itemName, new String(""));
				}
			} else {
				// Just use its toString() method
				if (!StringUtils.isEmpty(valueToCheck.toString())) {
					retVal_ = false;
				}
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Checks whether a text field is blank
	 * 
	 * @param itemName
	 *            String Item name to check whether or not it's empty
	 * @return boolean, whether or not Item is <b>not</b> blank
	 */
	public boolean isNotBlank(String itemName) {
		return !isBlank(itemName);
	}

	/**
	 * Add a value or collection of values to an ArrayList
	 * 
	 * @param passedArr
	 *            ArrayList<Object> to add the value to
	 * @param value
	 *            Object to add to the ArrayList
	 */
	public void addToArrayList(ArrayList<Object> passedArr, Object value) {
		try {
			if (null == value) {
				return;
			}
			if (value instanceof Collection) {
				Collection<?> coll = (Collection<?>) value;
				for (Object valNode : coll) {
					if (valNode != null) {
						passedArr.add(valNode);
					}
				}
			} else {
				if (StringUtils.isEmpty(value.toString())) {
					return;
				}
				passedArr.add(value);
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
	}

	/**
	 * Converts a field value to a currency string
	 * 
	 * @param value
	 *            Object value to convert
	 * @param forceDecimals
	 *            boolean whether or the currency value should force decimals even if a whole number
	 * @return String value as a currency
	 */
	public String convertToCurrency(Object value, boolean forceDecimals) {
		String retVal_ = "";
		try {
			if (value instanceof Number) {
				float number = new Float(value.toString());
				float epsilon = 0.004f; // 4 tenths of a penny
				if (!forceDecimals && Math.abs(Math.round(number) - number) < epsilon) {
					retVal_ = String.format("%10.0f", number); // sdb
				} else {
					retVal_ = String.format("%10.2f", number); // dj_segfault
				}
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return StringUtils.trim(retVal_);
	}

	/**
	 * Whether or not the Document has an Item with the passed field name
	 * 
	 * @param fieldName
	 *            String Item name to check for
	 * @return boolean whether or not the Item exists in this wrapper of the Document
	 */
	public boolean hasField(String fieldName) {
		return getValues().containsKey(fieldName);
	}

	/* (non-Javadoc)
	 * @see com.timtripcony.AbstractDocumentMapModel#setReadOnly(boolean)
	 */
	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if (readOnly) {
			setEditMode(false);
		}
	}
}
