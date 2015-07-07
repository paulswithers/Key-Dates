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

/**
 * "Borrowed" from Tim's NotesIn9 series, episodes 133-135
 * 
 * Tim, you were a genius. You may be gone but will never be forgotten
 */
public abstract class AbstractSmartDocumentModel extends AbstractDocumentMapModel {
	private static final long serialVersionUID = 1L;
	private boolean editMode;
	private boolean editable; // Because I keep forgetting!!
	private transient Document beDoc;

	public AbstractSmartDocumentModel() {
		this(false, "", "", false);
	}

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

	@Override
	public void load(final String unid) {
		super.load(unid);
	}

	@Override
	public void initNew() {
		super.initNew();
	}

	public Document getBEDoc() {
		Document retVal_ = null;
		if (StringUtils.isNotEmpty(getUnid())) {
			retVal_ = AppUtils.getDocumentByNoteID_Or_UNID(getUnid());
		}
		return retVal_;
	}

	public void edit() {
		setEditMode(true);
	}

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

	public <T> T getTypedValue(final String name, final Class<T> type) {
		return type.cast(getValues().get(name));
	}

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

	public Date getDateValue(final String name) {
		if (!hasField(name)) {
			return null;
		}
		if (String.class.equals(getType(name))) {
			return null;
		}
		return getTypedValue(name, Date.class);
	}

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

	public boolean isEditMode() {
		return editMode;
	}

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

	public void setEditMode(final boolean editMode) {
		this.editMode = editMode;
	}

	@Override
	public void setValue(final Object key, final Object value) {
		try {
			PropertyUtils.setProperty(this, key.toString(), value);
		} catch (Throwable t) {
			super.setValue(key, value);
		}
	}

	public boolean isEditable() {
		return editMode;
	}

	/**
	 * Checks whether a text field. Remember the values of approval dates are Strings
	 * 
	 * @param itemName
	 *            String sign off level to get Item name to check whether or not it's empty
	 * @return boolean, whether blank or not
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

	public boolean isNotBlank(String itemName) {
		return !isBlank(itemName);
	}

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

	public boolean hasField(String fieldName) {
		return getValues().containsKey(fieldName);
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		super.setReadOnly(readOnly);
		if (readOnly) {
			setEditMode(false);
		}
	}
}
