package uk.co.intec.keyDatesApp.components;

import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import uk.co.intec.keyDatesApp.MainUI;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class HeaderComponent extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private MenuBar menubar;
	private String userName;

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

	public String getUserName() {
		if (null == userName) {
			setUserName();
		}
		return userName;
	}

	public void setUserName() {
		try {
			this.userName = GenericDatabaseUtils.getUserName();
		} catch (final Exception e) {
			this.userName = "Anonymous";
		}
	}

	public MenuBar getMenubar() {
		return menubar;
	}

	public void setMenubar(MenuBar menubar) {
		this.menubar = menubar;
	}

}
