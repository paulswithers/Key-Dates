package org.openntf.osgiworlds.model;

import java.io.Serializable;

public abstract class AbstractViewEntryWrapper implements ViewEntryWrapper, Serializable {
	private static final long serialVersionUID = 1L;
	private String noteId;

	public AbstractViewEntryWrapper() {

	}

	@Override
	public String getNoteId() {
		return noteId;
	}

	@Override
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

}
