package com.timtripcony;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;

import javax.faces.context.FacesContext;

import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Document;
import org.openntf.domino.Item;
import org.openntf.domino.Item.Type;

import uk.co.intec.utils.AppUtils;

import com.ibm.commons.util.StringUtil;
import com.ibm.jscript.debug.ArrayListUtil;
import com.ibm.xsp.extlib.util.ExtLibUtil;
import com.ibm.xsp.model.domino.wrapped.DominoDocument;

/**
 * "Borrowed" from Tim's NotesIn9 series, episodes 133-135
 * 
 * Tim, you were a genius. You may be gone but will never be forgotten
 */
public abstract class AbstractDocumentMapModel extends AbstractMapModel {
	private static final long serialVersionUID = 1L;
	private String unid;
	private String parentUnid;
	private String documentId;
	private boolean ignoreRequestParams;
	private String requestParamPrefix;
	private DominoDocument wrappedDoc;
	private boolean newNote;
	private boolean deleted;
	private boolean readOnly;
	private Map<String, Item.Type> fieldTypeMap;

	public AbstractDocumentMapModel() {
		this(false, "", "");
	}

	public AbstractDocumentMapModel(boolean ignoreRequestParams, String requestParamPrefix, String documentId) {
		// This doesn't get called immediately, so we can set properties elsewhere
		try {
			setIgnoreRequestParams(ignoreRequestParams);
			setRequestParamPrefix(requestParamPrefix);
			setDocumentId(documentId);
			if (StringUtils.isEmpty(getRequestParamPrefix())) {
				setRequestParamPrefix("documentId");
			}
			if (!isIgnoreRequestParams()) {
				setDocumentId(ExtLibUtil.readParameter(FacesContext.getCurrentInstance(), getRequestParamPrefix()));
			}
			if (StringUtil.isNotEmpty(getDocumentId())) {
				load(getDocumentId());
			} else {
				initNew();
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
	}

	public String getUnid() {
		return unid;
	}

	public void setUnid(String unid) {
		this.unid = unid;
	}

	public Map<String, Item.Type> getFieldTypeMap() {
		return fieldTypeMap;
	}

	public void setFieldTypeMap(Map<String, Item.Type> fieldTypeMap) {
		this.fieldTypeMap = fieldTypeMap;
	}

	public abstract String getFormName();

	public abstract String getFormDescription();

	public void initNew() {
		setNewNote(true);
	}

	public void load(final String unid) {
		setUnid(unid);
		Document doc = null;
		setNewNote(true); // Default to true
		setDeleted(false); // Default to false
		setReadOnly(false); // Default to false
		try {
			if (StringUtil.isNotEmpty(unid)) {
				try {
					doc = AppUtils.getDocumentByNoteID_Or_UNID(unid);
					setWrappedDoc(DominoDocument.wrap(AppUtils.getDataDbPath(), // database name
							doc, // Document
							null, // computeWithForm
							null, // concurrency Mode
							false, // allowDeletedDocs
							null, // saveLinksAs
							null)); // webQuerySaveAgent
					for (Object eachItem : doc.getItems()) {
						if (eachItem instanceof Item) {
							Item item = (Item) eachItem;
							String itemName = item.getName();
							// Certainly not a comprehensive list of items to skip
							ArrayList<String> ignoreList = ArrayListUtil.stringToArrayList("MIME_Version");
							String firstChar = StringUtils.left(itemName, 1);
							if (!ignoreList.contains(itemName) && !StringUtils.equals(firstChar, "$")) {
								// Item may be of type "Error"
								if (item.getType() != Type.ERRORITEM.getValue()) {
									Object itemValue = wrappedDoc.getValue(itemName);
									setValue(itemName, itemValue);
									if (itemValue instanceof Vector) {
										if ("[]".equals(itemValue.toString())) {
											setValue(itemName, new String(""));
										}
									}
								}
							}
						}
					}
					if (doc.isDeleted() || !doc.isValid()) {
						setDeleted(true);
					}
					setNewNote(false);
				} catch (Throwable t) {
					AppUtils.handleException(t);
				}
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
	}

	public boolean postSave() {
		return true;
	}

	public boolean querySave() {
		return true;
	}

	public boolean setSpecialFieldTypes(Document doc) {
		boolean result = false;
		try {
			if (getFieldTypeMap() != null) {
				for (String itemName : getFieldTypeMap().keySet()) {
					if (doc.hasItem(itemName)) {
						Item item = doc.getFirstItem(itemName);
						Item.Type fieldType = getFieldTypeMap().get(itemName);
						if (fieldType.equals(Type.AUTHORS)) {
							item.setAuthors(true);
						} else if (fieldType.equals(Type.READERS)) {
							item.setReaders(true);
						} else if (fieldType.equals(Type.NAMES)) {
							item.setNames(true);
						}
					}
				}
			}
			return true;
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return result;
	}

	public boolean save() {
		boolean result = false;
		if (querySave()) {
			Document doc = null;
			try {
				if (StringUtil.isEmpty(getUnid())) {
					doc = AppUtils.getDataDb().createDocument();
					setUnid(doc.getUniversalID());
					doc.replaceItemValue("Form", getFormName());
				} else {
					doc = AppUtils.getDocumentByNoteID_Or_UNID(getUnid());
				}
				for (Entry entry : getValues().entrySet()) {
					String itemName = entry.getKey().toString();
					doc.replaceItemValue(itemName, entry.getValue());
				}
				if (setSpecialFieldTypes(doc)) {
					if (doc.save()) {
						if (StringUtils.isNotEmpty(parentUnid)) {
							Document parentDoc = AppUtils.getDocumentByNoteID_Or_UNID(parentUnid);
							doc.makeResponse(parentDoc);
						}
						result = postSave();
						setNewNote(false);
					}
				}
			} catch (Throwable t) {
				AppUtils.handleException(t);
			}
		}
		return result;

	}

	public String save(boolean goToView) {
		String retVal_ = "";
		try {
			if (save()) {
				if (goToView) {
					retVal_ = "xsp-success";
				} else {
					retVal_ = "xsp-current";
				}
			} else {
				AppUtils.addFacesMessage("Save failed");
			}
		} catch (Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	public void setNewNote(boolean newNote) {
		this.newNote = newNote;
	}

	public boolean isNewNote() {
		return newNote;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	public void setWrappedDoc(DominoDocument wrappedDoc) {
		this.wrappedDoc = wrappedDoc;
	}

	public DominoDocument getWrappedDoc() {
		return wrappedDoc;
	}

	public void setParentUnid(String parentUnid) {
		this.parentUnid = parentUnid;
	}

	public String getParentUnid() {
		return parentUnid;
	}

	public void setIgnoreRequestParams(boolean ignoreRequestParams) {
		this.ignoreRequestParams = ignoreRequestParams;
	}

	public boolean isIgnoreRequestParams() {
		return ignoreRequestParams;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public String getRequestParamPrefix() {
		return requestParamPrefix;
	}

	public void setRequestParamPrefix(String requestParamPrefix) {
		this.requestParamPrefix = requestParamPrefix;
	}

}
