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

import org.openntf.osgiworlds.model.AbstractDocLinkListener;

import com.vaadin.ui.Button.ClickEvent;

import uk.co.intec.keyDatesApp.MainUI;
import uk.co.intec.keyDatesApp.pages.KeyDateView;

/**
 * @author Paul Withers
 *
 *         Listener for Button links in views, to launch the relevant page with
 *         the relevant document (and potentially force edit mode).<br/>
 *         <br/>
 *         Functionality on the relevant page needs to receive and process docId
 *         and editMode parameters in the <i>enter</i> method. Omit handling
 *         code on receiving page to disable that functionality.<br/>
 *         <br/>
 *         Of course this could be extended to pass multiple documents or more!
 */
public class DocLinkListener extends AbstractDocLinkListener {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor, passing page to redirect to and document ID to load
	 *
	 * @param pageName
	 *            String page name. Use e.g. {@link KeyDateView.VIEW_NAME}
	 * @param docId
	 *            String Note ID or UNID to load on the page
	 */
	public DocLinkListener(String pageName, String docId) {
		super(pageName, docId);
	}

	/**
	 * Overloaded constructor, also passing edit mode
	 *
	 * @param pageName
	 *            String page name. Use e.g. {@link KeyDateView.VIEW_NAME}
	 * @param docId
	 *            String Note ID or UNID to load on the page
	 * @param editMode
	 *            boolean edit mode
	 */
	public DocLinkListener(String pageName, String docId, boolean editMode) {
		super(pageName, docId, editMode);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.AbstractDocLinkListener#buttonClick(com.
	 * vaadin.ui.Button.ClickEvent)
	 */
	@Override
	public void buttonClick(ClickEvent event) {
		if (isEditMode()) {
			MainUI.get().getNavigator().navigateTo(getPageName() + "/" + getDocId() + "/edit");
		} else {
			MainUI.get().getNavigator().navigateTo(getPageName() + "/" + getDocId());
		}
	}

}
