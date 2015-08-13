package org.openntf.osgiworlds.model;

import java.io.Serializable;

/**
 * @author Paul Withers
 *
 *         Abstract class implementing ViewEntryWrapper interface
 *
 */
public abstract class AbstractViewEntryWrapper implements ViewEntryWrapper, Serializable {
	private static final long serialVersionUID = 1L;
	private String noteId;

	/**
	 * Constructor
	 */
	public AbstractViewEntryWrapper() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openntf.osgiworlds.model.ViewEntryWrapper#getNoteId()
	 */
	@Override
	public String getNoteId() {
		return noteId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.openntf.osgiworlds.model.ViewEntryWrapper#setNoteId(java.lang.String)
	 */
	@Override
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

}
