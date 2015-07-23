package uk.co.intec.keyDatesApp.pages;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openntf.osgiworlds.model.ViewEntryWrapper;

import uk.co.intec.keyDatesApp.components.Pager;
import uk.co.intec.keyDatesApp.components.Pager.Sizes;
import uk.co.intec.keyDatesApp.model.KeyDateEntryWrapper;
import uk.co.intec.keyDatesApp.model.KeyDateViewWrapper;

import com.ibm.commons.util.StringUtil;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

public class MainView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "ListView";
	public static final String VIEW_LABEL = "Key Dates";
	private static final SimpleDateFormat DATE_ONLY = new SimpleDateFormat("dd MMM yyyy");
	private VerticalLayout body = new VerticalLayout();
	private KeyDateViewWrapper viewWrapper = new KeyDateViewWrapper(this);
	public boolean isLoaded = false;
	private Pager pager;

	public MainView() {

		setSizeFull();

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		try {
			if (!isLoaded) {
				body.setDefaultComponentAlignment(Alignment.TOP_LEFT);

				final ArrayList<Sizes> availSizes = new ArrayList<Sizes>();
				availSizes.add(Sizes.FIVE);
				availSizes.add(Sizes.TEN);
				availSizes.add(Sizes.TWENTY_FIVE);
				final Pager newPager = new Pager(viewWrapper, availSizes);
				newPager.loadContent();
				setPager(newPager);

				loadContent();
				getPager().loadPagerPagesButtons();
				isLoaded = true;
			}

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void loadContent() {
		try {
			viewWrapper.setStartDate(new java.util.Date());
			final Map<Object, List<ViewEntryWrapper>> data = viewWrapper.getEntriesAsMap();

			loadRowData(data);
			addComponents(getPager(), body);
			setSizeFull();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void loadRowData(final Map<Object, List<ViewEntryWrapper>> data) {
		body.removeAllComponents();
		if (data.isEmpty()) {
			final Label msg = new Label("No entries found matching criteria");
			msg.setStyleName(ValoTheme.LABEL_FAILURE);
			body.addComponent(msg);
			removeComponent(getPager());
		} else {
			for (final Object key : data.keySet()) {
				if (key instanceof java.sql.Date) { // It will be!
					// Add the header
					final VerticalLayout catContainer = new VerticalLayout();
					catContainer.addStyleName(ValoTheme.MENU_TITLE);
					final Label category = new Label();
					final java.sql.Date sqlDate = (java.sql.Date) key;
					category.setValue(DATE_ONLY.format(sqlDate));
					catContainer.addComponent(category);
					body.addComponent(catContainer);

					// Load the entries
					for (final ViewEntryWrapper veWrapped : data.get(key)) {
						final VerticalLayout entryRow = new VerticalLayout();
						final KeyDateEntryWrapper entry = (KeyDateEntryWrapper) veWrapped;
						final StringBuilder suffixTitle = new StringBuilder("");
						if (getViewWrapper().getViewName().equals(KeyDateViewWrapper.ViewType.BY_DATE)) {
							if (StringUtil.isNotEmpty(entry.getCustomer())) {
								suffixTitle.append(" (" + entry.getCustomer());
								if (StringUtil.isNotEmpty(entry.getContact())) {
									suffixTitle.append(" - " + entry.getContact());
								}
								suffixTitle.append(")");
							}
						} else {
							if (StringUtil.isNotEmpty(entry.getContact())) {
								suffixTitle.append(" - " + entry.getContact());
							}
						}
						final Button title = new Button(entry.getTitle() + suffixTitle.toString());
						title.addStyleName(ValoTheme.BUTTON_LINK);
						// Add click event
						title.addClickListener(new ClickListener() {
							private static final long serialVersionUID = 1L;

							@Override
							public void buttonClick(ClickEvent event) {
								// TODO Auto-generated method stub

							}
						});
						entryRow.addComponent(title);

						// Add summary, if appropriate
						if (StringUtil.isNotEmpty(entry.getDescription())) {
							final Label summary = new Label(entry.getDescription());
							summary.addStyleName(ValoTheme.LABEL_SMALL);
							summary.addStyleName("view-desc");
							entryRow.addComponent(summary);
						}

						body.addComponent(entryRow);
					}
				}
			}
		}
	}

	public VerticalLayout getContainer() {
		return body;
	}

	public void setContainer(VerticalLayout container) {
		this.body = container;
	}

	public KeyDateViewWrapper getViewWrapper() {
		return viewWrapper;
	}

	public void setViewWrapper(KeyDateViewWrapper viewWrapper) {
		this.viewWrapper = viewWrapper;
	}

	public Pager getPager() {
		return pager;
	}

	public void setPager(Pager pager) {
		this.pager = pager;
	}

}
