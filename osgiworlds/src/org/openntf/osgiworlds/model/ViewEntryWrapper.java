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
