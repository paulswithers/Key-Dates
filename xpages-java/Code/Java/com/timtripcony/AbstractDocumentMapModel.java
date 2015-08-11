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
 *         Extension of AbstractMapModel to add Document-related / XPages-related properties. Amended by PW to rely on
 *         OpenNTF Domino API (this allowed me to remove a lot of code Tim had repeated from ODA)
 * 
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

	/**
	 * Constructor, calling overloaded constructor and simulating DominoDocument datasource, to get default Document
	 * based on URL
	 */
	public AbstractDocumentMapModel() {
		this(false, "", "");
	}

	/**
	 * Overloaded constructor, added by PW, allowing loading based on ignoreRequestParams, requestParamPrefix and
	 * documentId URL parameter
	 * 
	 * @param ignoreRequestParams
	 *            boolean whether or not to load UNID, action etc from URL parameters
	 * @param requestParamPrefix
	 *            String alternative requestParamPrefix for UNID, action etc URL parameters
	 * @param documentId
	 *            String alternative UNID / Note ID from which to load Document
	 */
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

	/**
	 * Getter for unid property
	 * 
	 * @return String UNID of the Document being interacted with
	 */
	public String getUnid() {
		return unid;
	}

	/**
	 * Setter for unid property
	 * 
	 * @param unid
	 *            String of the Document to interact with
	 */
	public void setUnid(String unid) {
		this.unid = unid;
	}

	/**
	 * A Map of Item names and the specific Item type (e.g. READERS, AUTHORS etc). Added by PW
	 * 
	 * @return Map<String, Item.Type> specific Item types, where Key = Item name, Value = Item type
	 */
	public Map<String, Item.Type> getFieldTypeMap() {
		return fieldTypeMap;
	}

	/**
	 * Load a Map of Item names and specific Item types (e.g. READERS, AUTHORS etc). Added by PW
	 * 
	 * @param fieldTypeMap
	 *            Map<String, Item.Type> specific Item types, where Key = Item name, Value = Item type
	 */
	public void setFieldTypeMap(Map<String, Item.Type> fieldTypeMap) {
		this.fieldTypeMap = fieldTypeMap;
	}

	/**
	 * Returns Form name to use for this Document
	 * 
	 * @return String Form property of this Document
	 */
	public abstract String getFormName();

	/**
	 * Returns a description for the Form used for this Document, useful for displaying in Form Tables
	 * 
	 * @return String Form description
	 */
	public abstract String getFormDescription();

	/**
	 * Sets the Document as a new note
	 */
	public void initNew() {
		setNewNote(true);
	}

	/**
	 * Loads the object with settings based on the passed UNID:
	 * <ul>
	 * <ol>
	 * Whether or not it is a new Document
	 * </ol>
	 * <ol>
	 * Whether or not the Document is deleted
	 * </ol>
	 * <ol>
	 * Whether or not the Document is read only
	 * </ol>
	 * <ol>
	 * Sets wrappedDoc as a DominoDocument object
	 * </ol>
	 * <ol>
	 * Loads all fields into the object, omitting named fields, anything prefixed with "$", or with type "Error"
	 * </ol>
	 * </ul>
	 * 
	 * Extended by PW to allow specific fields to be ignored and to ensure empty fields are loaded as "" (empty String),
	 * not "[]" (String version of empty Vector)
	 * 
	 * @param unid
	 *            String UNID or Note ID relating to this Document (empty = new Document)
	 */
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

	/**
	 * PostSave event code to run
	 * 
	 * @return boolean Success / Failure in running postSave code, required to handle onward routing
	 */
	public boolean postSave() {
		return true;
	}

	/**
	 * QuerySave event code to run
	 * 
	 * @return boolean success / Failure in running querySave code, required to handle onward routing
	 */
	public boolean querySave() {
		return true;
	}

	/**
	 * Sets Item types based on fieldTypeMap. Currently only handles READERS, AUTHORS or NAMES
	 * 
	 * @param doc
	 *            Document being saved
	 * @return boolean success / failure of setting Item types
	 */
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

	/**
	 * Save of the Document, calling querySave at the start and postSave at the end
	 * 
	 * @return boolean success / failure of the save
	 */
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

	/**
	 * Overloaded save method, allowing navigation handling to force return to a view, if required
	 * 
	 * @param goToView
	 *            boolean whether or not to go to the view
	 * @return String navigationRule name (defaults are xsp-success for going to next page, xsp-current to stay on
	 *         current page)
	 */
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

	/**
	 * Setter for newNote
	 * 
	 * @param newNote
	 *            boolean whether or not this is a new Document
	 */
	public void setNewNote(boolean newNote) {
		this.newNote = newNote;
	}

	/**
	 * Getter for newNote
	 * 
	 * @return boolean whether or not this is a new Document
	 */
	public boolean isNewNote() {
		return newNote;
	}

	/**
	 * Setter for deleted. <b>NB</b> Not intended to actually delete the document
	 * 
	 * @param deleted
	 *            boolean whether or not the Document should be considered deleted
	 */
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	/**
	 * Getter for deleted
	 * 
	 * @return boolean whether or not the document is deleted
	 */
	public boolean isDeleted() {
		return deleted;
	}

	/**
	 * Setter for readOnly
	 * 
	 * @param readOnly
	 *            boolean whether or not the Document should be considered read only
	 */
	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	/**
	 * Getter for readOnly
	 * 
	 * @return boolean whether or not the Document is read only
	 */
	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * Setter for wrappedDoc
	 * 
	 * @param wrappedDoc
	 *            DominoDocument XPages wrapper for the Document
	 */
	public void setWrappedDoc(DominoDocument wrappedDoc) {
		this.wrappedDoc = wrappedDoc;
	}

	/**
	 * Getter for wrappedDoc
	 * 
	 * @return DominoDocument XPages wrapper for the Document
	 */
	public DominoDocument getWrappedDoc() {
		return wrappedDoc;
	}

	/**
	 * Setter for parentUnid
	 * 
	 * @param parentUnid
	 *            String UNID or NoteID of the Document's parent
	 */
	public void setParentUnid(String parentUnid) {
		this.parentUnid = parentUnid;
	}

	/**
	 * Getter for parentUnid
	 * 
	 * @return String UNID or NoteID of the Document's parent
	 */
	public String getParentUnid() {
		return parentUnid;
	}

	/**
	 * Setter for ignoreRequestParams
	 * 
	 * @param ignoreRequestParams
	 *            boolean whether or not to ignore request parameters when initialising the Document
	 */
	public void setIgnoreRequestParams(boolean ignoreRequestParams) {
		this.ignoreRequestParams = ignoreRequestParams;
	}

	/**
	 * Getter for ignoreRequestParams
	 * 
	 * @return boolean whether or not to ignore request parameters when initialising the Document
	 */
	public boolean isIgnoreRequestParams() {
		return ignoreRequestParams;
	}

	/**
	 * Setter for documentId
	 * 
	 * @param documentId
	 *            String UNID or NoteID for the Document
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * Getter for documentId
	 * 
	 * @return String UNID or NoteID for the Document
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Setter for requestParamPrefix
	 * 
	 * @param requestParamPrefix
	 *            String URL parameter to use to locate the Document's URL parameters
	 */
	public void setRequestParamPrefix(String requestParamPrefix) {
		this.requestParamPrefix = requestParamPrefix;
	}

	/**
	 * String getter for requestParamPrefix
	 * 
	 * @return String URL parameter to use to locate the Document's URL parameters
	 */
	public String getRequestParamPrefix() {
		return requestParamPrefix;
	}

}
