package org.openntf.osgiworlds.model;

import com.vaadin.ui.components.calendar.event.BasicEvent;

/**
 * @author Paul Withers
 *
 *         Extension to BasicEvent used in Calendar views. This is extended to
 *         allow the Note ID to be stored. That then allows an extension to the
 *         DominoCalendarEventClickHandler to launch the relevant page passing
 *         this BasicDominoEvent's ID, to allow viewing / editing of this
 *         entry's document.
 *
 */
public class BasicDominoEvent extends BasicEvent {
	private static final long serialVersionUID = 1L;
	private String noteId;

	/**
	 * Constructor
	 */
	public BasicDominoEvent() {
		super();
	}

	/**
	 * Getter for noteId
	 *
	 * @return String Note ID
	 */
	public String getNoteId() {
		return noteId;
	}

	/**
	 * Setter for noteId
	 *
	 * @param noteId
	 *            String Note ID
	 */
	public void setNoteId(String noteId) {
		this.noteId = noteId;
	}

}
