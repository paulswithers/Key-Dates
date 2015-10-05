package org.openntf.osgiworlds.model;

/*

<!--
Copyright 2015 Paul Withers
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License
-->

*/

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
