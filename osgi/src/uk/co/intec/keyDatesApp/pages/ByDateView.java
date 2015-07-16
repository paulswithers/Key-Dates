package uk.co.intec.keyDatesApp.pages;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;

public class ByDateView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "ByDate";
	public static final String VIEW_LABEL = "Key Dates";
	public boolean isLoaded = false;

	public ByDateView() {

		setSizeFull();

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		try {
			if (!isLoaded) {
				loadContent();
				isLoaded = true;
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void loadContent() {
		try {

			setSizeFull();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

}
