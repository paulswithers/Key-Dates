package uk.co.intec.keyDatesApp;

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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Viewport;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Component;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import uk.co.intec.keyDatesApp.components.HeaderComponent;
import uk.co.intec.keyDatesApp.pages.CalendarView;
import uk.co.intec.keyDatesApp.pages.ErrorView;
import uk.co.intec.keyDatesApp.pages.HomeView;
import uk.co.intec.keyDatesApp.pages.KeyDateView;
import uk.co.intec.keyDatesApp.pages.MainView;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         This is the application page for Vaadin. Switching navigation options
 *         just loads a different "page" or <i>View</i> onto the body area.
 *         Extends Vaadin UI class.
 */
@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("appTheme")
public class MainUI extends UI {
	private static final long serialVersionUID = 1L;
	private Navigator uiNavigator;
	private HeaderComponent header;
	private VerticalLayout body;
	private VaadinRequest request;

	/**
	 * Constructor
	 */
	public MainUI() {

	}

	/**
	 * Overloaded constructor
	 *
	 * @param content
	 */
	public MainUI(Component content) {
		super(content);
	}

	/**
	 * Gets this Vaadin UI object, casting as a MainUI
	 *
	 * @return MainUI this object
	 */
	public static MainUI get() {
		return (MainUI) UI.getCurrent();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		try {
			setRequest(request);

			Responsive.makeResponsive(this);

			addStyleName(ValoTheme.UI_WITH_MENU);

			getPage().setTitle("Key Dates App");

			setStyleName("main-screen");

			final VerticalLayout layout = new VerticalLayout();
			setHeader(new HeaderComponent(this));
			layout.addComponent(getHeader());
			setContent(layout);

			setBody(new VerticalLayout());
			getBody().setMargin(new MarginInfo(false, true, false, true));
			layout.addComponent(getBody());

			setUiNavigator(new Navigator(this, body));
			getUiNavigator().setErrorView(ErrorView.class);

			addNewMenuItem(HomeView.VIEW_NAME, HomeView.VIEW_LABEL, new HomeView());
			addNewMenuItem(MainView.VIEW_NAME, MainView.VIEW_LABEL, new MainView());
			addNewMenuItem(CalendarView.VIEW_NAME, CalendarView.VIEW_LABEL, new CalendarView());
			addNewMenuItem(KeyDateView.VIEW_NAME, KeyDateView.VIEW_LABEL, new KeyDateView());

			if (!GenericDatabaseUtils.doesDbExist() || "Anonymous".equals(GenericDatabaseUtils.getUserName())) {
				getUiNavigator().navigateTo(HomeView.VIEW_NAME);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Adds a new View to the Navigator and adds a new menu option to the
	 * Header's menu bar. The default View should have a viewName set as "". New
	 * Views and menu options should be added in the {@link init} method
	 *
	 * @param viewName
	 *            String view name, needs to be unique
	 * @param viewLabel
	 *            String label to appear on the menu button
	 * @param viewObj
	 *            View object corresponding to the menu option
	 */
	public void addNewMenuItem(final String viewName, final String viewLabel, final View viewObj) {
		getUiNavigator().addView(viewName, viewObj);
		getHeader().getMenubar().addItem(viewLabel, new MenuBar.Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				final List<MenuItem> items = getHeader().getMenubar().getItems();
				for (final MenuItem item : items) {
					if (StringUtils.equals("highlight", item.getStyleName())) {
						item.setStyleName("");
					}
				}
				selectedItem.setStyleName("highlight");
				MainUI.getCurrent().getNavigator().navigateTo(viewName);
			}
		});
	}

	/**
	 * Getter for uiNavigator, allowing us to register a View
	 *
	 * @return Navigator the main navigator for this application
	 */
	public Navigator getUiNavigator() {
		return uiNavigator;
	}

	/**
	 * Setter for uiNavigator. Should be set in {@link init}.
	 *
	 * @param uiNavigator
	 *            Navigator the main navigator for this application
	 */
	public void setUiNavigator(Navigator uiNavigator) {
		this.uiNavigator = uiNavigator;
	}

	/**
	 * Getter for header
	 *
	 * @return HeaderComponent the header area of the application
	 */
	public HeaderComponent getHeader() {
		return header;
	}

	/**
	 * Setter for header. Should be set in {@link init}
	 *
	 * @param header
	 *            HeaderComponent the header area of the application
	 */
	public void setHeader(HeaderComponent header) {
		this.header = header;
	}

	/**
	 * Getter for body
	 *
	 * @return VerticalLayout body area of the application
	 */
	public VerticalLayout getBody() {
		return body;
	}

	/**
	 * Setter for body
	 *
	 * @param body
	 *            VerticalLayout body area of the application
	 */
	public void setBody(VerticalLayout body) {
		this.body = body;
	}

	/**
	 * Getter for request
	 *
	 * @return VaadinRequest the current Vaadin request
	 */
	public VaadinRequest getRequest() {
		return request;
	}

	/**
	 * Setter for request
	 *
	 * @param request
	 *            VaadinRequest loads the current Vaadin request
	 */
	public void setRequest(VaadinRequest request) {
		this.request = request;
	}

}
