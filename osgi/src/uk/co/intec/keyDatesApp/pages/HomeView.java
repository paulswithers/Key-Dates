package uk.co.intec.keyDatesApp.pages;

import org.openntf.domino.Database;

import uk.co.intec.keyDatesApp.utils.AppUtils;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

public class HomeView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = ""; // Default view name is ""
	public static final String VIEW_LABEL = "Home";
	public boolean loaded = false;

	public HomeView() {

		setSizeFull();

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

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

	public void loadContent() {
		if ("Anonymous".equals(AppUtils.getUserSession().getEffectiveUserName())) {
			final Label warning = new Label();
			warning.setStyleName(ValoTheme.LABEL_H2);
			warning.setStyleName(ValoTheme.LABEL_FAILURE);
			warning.setValue("Anonymous access is not allowed on this application!");
			addComponent(warning);
		} else {
			final Database dataDb = AppUtils.getDataDb();
			if (null == dataDb) {
				final Label warning = new Label();
				warning.setStyleName(ValoTheme.LABEL_H2);
				warning.setStyleName(ValoTheme.LABEL_FAILURE);
				warning.setContentMode(ContentMode.HTML);
				warning.setValue("We cannot open the data database. Most likely reasons are:<ul><li>You don't have access to the database, in which case you should contact IT.</li>"
						+ "<li>The Key Dates database has not been set up at the filepath "
						+ AppUtils.getDataDbFilepath()
						+ ". Create the data database at that location or amend the 'dataDbFilePath' context parameter in WebContent > WEB-INF > web.xml of the application and issue 'restart task http' to the Domino server</li></ul>");
				addComponent(warning);
			} else {
				final Label intro = new Label();
				intro.setStyleName(ValoTheme.LABEL_H2);
				intro.setValue("Welcome to Key Dates OSGi Application");
				addComponent(intro);
			}
		}
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

}
