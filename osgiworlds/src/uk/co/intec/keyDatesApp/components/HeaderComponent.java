package uk.co.intec.keyDatesApp.components;

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

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import uk.co.intec.keyDatesApp.MainUI;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Header area of the application
 */
public class HeaderComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private MenuBar menubar;
	private String userName;

	/**
	 * Constructor, passing the Vaadin application page
	 *
	 * @param ui
	 *            MainUI application page
	 */
	public HeaderComponent(MainUI ui) {
		setHeight("50px");
		setStyleName("header");

		final HorizontalLayout bannerArea = new HorizontalLayout();
		bannerArea.setStyleName("menuArea");
		bannerArea.setSizeFull();

		final ThemeResource resource = new ThemeResource("img/intec-logo.gif.png");
		final Image bannerImg = new Image();
		bannerImg.setAlternateText("Intec");
		bannerImg.setHeight("50px");
		bannerImg.setDescription("Intec Logo");
		bannerImg.setSource(resource);
		bannerImg.setWidth(null);
		bannerImg.setStyleName("bannerImg");

		setMenubar(new MenuBar());
		getMenubar().setStyleName(ValoTheme.MENU_SUBTITLE);
		getMenubar().addStyleName("valo-menu-subtitle-indent");
		getMenubar().setWidth(100, Unit.PERCENTAGE);

		if (!"Anonymous".equals(getUserName())) {
			final MenuItem logout = menubar.addItem("Logout", null);
			logout.setStyleName("menuRight");
		}

		final MenuItem userItem = menubar.addItem(getUserName(), null);
		userItem.setStyleName("menuRight");

		bannerArea.addComponents(bannerImg, menubar);
		bannerArea.setExpandRatio(menubar, 1);
		addComponent(bannerArea);
		setExpandRatio(bannerArea, 1);
		setSizeFull();
	}

	/**
	 * Getter for username
	 *
	 * @return Retrieves the current user name
	 */
	public String getUserName() {
		if (null == userName) {
			setUserName();
		}
		return userName;
	}

	/**
	 * Loads the user name
	 */
	public void setUserName() {
		try {
			this.userName = GenericDatabaseUtils.getUserName();
		} catch (final Exception e) {
			this.userName = "Anonymous";
		}
	}

	/**
	 * Getter for menubar
	 *
	 * @return MenuBar allows external access to the menu bar created and added
	 *         to the header
	 */
	public MenuBar getMenubar() {
		return menubar;
	}

	/**
	 * Setter for menubar
	 *
	 * @param menubar
	 *            MenuBar being added to this header
	 */
	public void setMenubar(MenuBar menubar) {
		this.menubar = menubar;
	}

}
