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

import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClickHandler;

import uk.co.intec.keyDatesApp.pages.KeyDateView;

/**
 * @author Paul Withers
 *
 *         Abstract class for clicking BasicDominoEvents in Calendar views, to
 *         launch the relevant page with the relevant document (and potentially
 *         force edit mode). Unlike the AbstractDocLinkListener, we have a
 *         handle on the relevant DominoBasicEvent from this class, so don't
 *         need to pass the Note ID of the document to open.<br/>
 *         <br/>
 *         Normal implementations of this will use a navigator in the main
 *         Vaadin UI class to navigate to the relevant page. Because that class
 *         name may vary across applications, and because there may potentially
 *         be different implementations, this is an abstract class that will
 *         need extending.<br/>
 *         <br/>
 *         Functionality on the relevant page needs to receive and process docId
 *         and editMode parameters in the <i>enter</i> method. Omit handling
 *         code on receiving page to disable that functionality.<br/>
 *         <br/>
 *         Of course this could be extended to pass multiple documents or more!
 *
 */
public abstract class AbstractDominoCalendarEventClickHandler implements EventClickHandler {
	private static final long serialVersionUID = 1L;
	private static final String ERROR_DONT_USE_DIRECTLY = "This method should not be accessed directly from the abstract class AbstractDominoCalendarEventClickHandler";
	private String pageName;
	private boolean editMode;

	/**
	 * Constructor passing page to redirect to
	 *
	 * @param pageName
	 *            String page name. Use e.g. {@link KeyDateView.VIEW_NAME}
	 */
	public AbstractDominoCalendarEventClickHandler(String pageName) {
		this(pageName, false);
	}

	/**
	 * Overloaded constructor, also passing edit mode
	 *
	 * @param pageName
	 *            String page name. Use e.g. {@link KeyDateView.VIEW_NAME}
	 * @param editMode
	 *            boolean edit mode
	 */
	public AbstractDominoCalendarEventClickHandler(String pageName, boolean editMode) {
		setPageName(pageName);
		setEditMode(editMode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.components.calendar.CalendarComponentEvents.
	 * EventClickHandler#eventClick(com.vaadin.ui.components.calendar.
	 * CalendarComponentEvents.EventClick)
	 */
	@Override
	public void eventClick(EventClick event) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/**
	 * Getter for page name
	 *
	 * @return String page view name to redirect to
	 */
	public String getPageName() {
		return pageName;
	}

	/**
	 * Setter for page name
	 *
	 * @param pageName
	 *            String page view name to redirect to
	 */
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**
	 * Getter for editMode
	 *
	 * @return boolean whether or not to launch document in edit mode
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * Setter for editMode
	 *
	 * @param editMode
	 *            boolean whether or not to launch document in edit mode
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

}
