package uk.co.intec.keyDatesApp.model;

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

import org.openntf.osgiworlds.model.AbstractDominoCalendarEventClickHandler;
import org.openntf.osgiworlds.model.BasicDominoEvent;

import com.vaadin.ui.components.calendar.CalendarComponentEvents.EventClick;

import uk.co.intec.keyDatesApp.MainUI;
import uk.co.intec.keyDatesApp.pages.KeyDateView;

/**
 * @author Paul Withers
 *
 *         Handler for clicking BasicDominoEvents in Calendar views, to launch
 *         the relevant page with the relevant document (and potentially force
 *         edit mode). Unlike the AbstractDocLinkListener, we have a handle on
 *         the relevant DominoBasicEvent from this class, so don't need to pass
 *         the Note ID of the document to open.<br/>
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
public class KeyDateCalendarEventClickHandler extends AbstractDominoCalendarEventClickHandler {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor passing page to redirect to
	 *
	 * @param pageName
	 *            String page name. Use e.g. {@link KeyDateView.VIEW_NAME}
	 */
	public KeyDateCalendarEventClickHandler(String pageName) {
		super(pageName);
	}

	/**
	 * Overloaded constructor, also passing edit mode
	 *
	 * @param pageName
	 *            String page name. Use e.g. {@link KeyDateView.VIEW_NAME}
	 * @param editMode
	 *            boolean edit mode
	 */
	public KeyDateCalendarEventClickHandler(String pageName, boolean editMode) {
		super(pageName, editMode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.AbstractDominoCalendarEventClickHandler#
	 * eventClick(com.vaadin.ui.components.calendar.CalendarComponentEvents.
	 * EventClick)
	 */
	@Override
	public void eventClick(EventClick event) {
		final BasicDominoEvent e = (BasicDominoEvent) event.getCalendarEvent();
		if (isEditMode()) {
			MainUI.get().getNavigator().navigateTo(getPageName() + "/" + e.getNoteId() + "/edit");
		} else {
			MainUI.get().getNavigator().navigateTo(getPageName() + "/" + e.getNoteId());
		}
	}

}
