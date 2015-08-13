package org.openntf.osgiworlds.model;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Wrapper for Domino ViewEntry object
 *
 */
public interface ViewEntryWrapper {

	/**
	 * String gets the NoteID for this wrapped ViewEntry
	 *
	 * @return String NoteID
	 */
	public String getNoteId();

	/**
	 * Sets the NoteID for this wrapped ViewEntry
	 *
	 * @param noteId
	 *            String NoteID
	 */
	public void setNoteId(String noteId);

}
