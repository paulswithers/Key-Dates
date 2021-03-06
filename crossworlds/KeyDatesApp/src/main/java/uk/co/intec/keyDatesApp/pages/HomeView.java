package uk.co.intec.keyDatesApp.pages;

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

import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import uk.co.intec.keyDatesApp.utils.AppUtils;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Home page.
 *
 */
public class HomeView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = ""; // Default view name is ""
	public static final String VIEW_LABEL = "Home";
	private boolean loaded = false;

	/**
	 * Constructor, sets CssLayout to take full area
	 */
	public HomeView() {

		setSizeFull();

	}

	/**
	 * Shows an error message on the screen
	 *
	 * @param msg
	 *            String error to display
	 */
	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		try {
			if (!isLoaded()) {
				loadContent();
				setLoaded(true);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Loads the main content for the page. Only called on first entry to the
	 * page, because calling method sets <i>isLoaded</i> to true after
	 * successfully completing.
	 */
	public void loadContent() {
		if ("Anonymous".equals(GenericDatabaseUtils.getUserName())) {
			final Label warning = new Label();
			warning.setStyleName(ValoTheme.LABEL_H2);
			warning.setStyleName(ValoTheme.LABEL_FAILURE);
			warning.setValue("Anonymous access is not allowed on this application!");
			addComponent(warning);
		} else {
			if (GenericDatabaseUtils.doesDbExist()) {
				final Label intro = new Label();
				intro.setStyleName(ValoTheme.LABEL_H2);
				intro.setValue("Welcome to Key Dates OSGi Application");
				addComponent(intro);
			} else {
				final Label warning = new Label();
				warning.setStyleName(ValoTheme.LABEL_H2);
				warning.setStyleName(ValoTheme.LABEL_FAILURE);
				warning.setContentMode(ContentMode.HTML);
				warning.setValue(
						"We cannot open the data database. Most likely reasons are:<ul><li>You don't have access to the database, in which case you should contact IT.</li>"
								+ "<li>The Key Dates database has not been set up at the filepath " + AppUtils.getDataDbFilepath()
								+ ". Create the data database at that location or amend the 'dataDbFilePath' context parameter in WebContent > WEB-INF > web.xml of the application and issue 'restart task http' to the Domino server</li></ul>");
				addComponent(warning);
			}
		}
	}

	/**
	 * Getter for loaded
	 *
	 * @return boolean whether the content has already been loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Setter for loaded
	 *
	 * @param loaded
	 *            boolean whether or not the content has already been loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}
