package uk.co.intec.keyDatesApp;

import java.util.List;

import org.openntf.domino.Database;

import uk.co.intec.keyDatesApp.components.HeaderComponent;
import uk.co.intec.keyDatesApp.pages.CalendarView;
import uk.co.intec.keyDatesApp.pages.ErrorView;
import uk.co.intec.keyDatesApp.pages.HomeView;
import uk.co.intec.keyDatesApp.pages.MainView;
import uk.co.intec.keyDatesApp.utils.AppUtils;

import com.ibm.commons.util.StringUtil;
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

@Viewport("user-scalable=no,initial-scale=1.0")
@Theme("appTheme")
public class MainUI extends UI {
	private static final long serialVersionUID = 1L;
	private Navigator uiNavigator;
	private HeaderComponent header;
	private VerticalLayout body;
	private boolean threadInited;
	private VaadinRequest request;

	public MainUI() {

	}

	public MainUI(Component content) {
		super(content);
	}

	public static MainUI get() {
		return (MainUI) UI.getCurrent();
	}

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

			final Database dataDb = AppUtils.getDataDb();
			if (null == dataDb || "Anonymous".equals(AppUtils.getUserSession().getEffectiveUserName())) {
				getUiNavigator().navigateTo(HomeView.VIEW_NAME);
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public void addNewMenuItem(final String viewName, final String viewLabel, final View viewObj) {
		getUiNavigator().addView(viewName, viewObj);
		getHeader().getMenubar().addItem(viewLabel, new MenuBar.Command() {
			private static final long serialVersionUID = 1L;

			@Override
			public void menuSelected(MenuItem selectedItem) {
				final List<MenuItem> items = getHeader().getMenubar().getItems();
				for (final MenuItem item : items) {
					if (StringUtil.equals("highlight", item.getStyleName())) {
						item.setStyleName("");
					}
				}
				selectedItem.setStyleName("highlight");
				MainUI.getCurrent().getNavigator().navigateTo(viewName);
			}
		});
	}

	public Navigator getUiNavigator() {
		return uiNavigator;
	}

	public void setUiNavigator(Navigator uiNavigator) {
		this.uiNavigator = uiNavigator;
	}

	public HeaderComponent getHeader() {
		return header;
	}

	public void setHeader(HeaderComponent header) {
		this.header = header;
	}

	public VerticalLayout getBody() {
		return body;
	}

	public void setBody(VerticalLayout body) {
		this.body = body;
	}

	public boolean isThreadInited() {
		return threadInited;
	}

	public void setThreadInited(boolean threadInited) {
		this.threadInited = threadInited;
	}

	public VaadinRequest getRequest() {
		return request;
	}

	public void setRequest(VaadinRequest request) {
		this.request = request;
	}

}
