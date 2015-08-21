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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.openntf.osgiworlds.model.ViewEntryWrapper;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import uk.co.intec.keyDatesApp.components.MainViewFilter;
import uk.co.intec.keyDatesApp.components.Pager;
import uk.co.intec.keyDatesApp.model.DocLinkListener;
import uk.co.intec.keyDatesApp.model.KeyDateEntryWrapper;
import uk.co.intec.keyDatesApp.model.KeyDateViewWrapper;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Main view page including view filter (which provides faceted search
 *         of fields), pager, and navigable view.
 *
 */
public class MainView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "ListView";
	public static final String VIEW_LABEL = "Key Dates";
	public static final SimpleDateFormat DATE_ONLY = new SimpleDateFormat("dd MMM yyyy");
	private final VerticalLayout body = new VerticalLayout();
	private KeyDateViewWrapper viewWrapper = new KeyDateViewWrapper(this);
	private MainViewFilter filter = new MainViewFilter(this);
	private boolean loaded = false;
	private Pager pager;

	/**
	 * Constructor, sets CssLayout to take full area
	 */
	public MainView() {

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
				body.setDefaultComponentAlignment(Alignment.TOP_LEFT);

				final Pager newPager = new Pager(viewWrapper, null);
				newPager.loadContent();
				setPager(newPager);

				loadContent();
				getPager().loadPagerPagesButtons();
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
		try {
			viewWrapper.setStartDate(new java.util.Date());
			final Map<Object, List<ViewEntryWrapper>> data = viewWrapper.getEntriesAsMap();

			loadRowData(data);

			addComponents(filter, getPager(), body);
			setSizeFull();

		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes any existing row data loaded to the page and loads
	 * ViewEntryWrappers passed to this method. If no entries were passed to the
	 * method, the message "No entries found matching criteria" is displayed.
	 * Otherwise writes the entries to the page, grouped under the date each Key
	 * Date is for.
	 *
	 * @param data
	 *            Map of data where key is a java.sql.Date (so does not include
	 *            a time element) and value is the wrapped ViewEntries for that
	 *            date.
	 */
	public void loadRowData(final Map<Object, List<ViewEntryWrapper>> data) {
		body.removeAllComponents();
		if (null == data || data.isEmpty()) {
			final Label msg = new Label("No entries found matching criteria");
			msg.setStyleName(ValoTheme.LABEL_FAILURE);
			body.addComponent(msg);
		} else {
			for (final Object key : data.keySet()) {
				if (key instanceof java.sql.Date) { // It will be!
					// Add the header
					final VerticalLayout catContainer = new VerticalLayout();
					catContainer.addStyleName(ValoTheme.MENU_TITLE);
					catContainer.addStyleName("category-header");
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
							if (StringUtils.isNotEmpty(entry.getCustomer())) {
								suffixTitle.append(" (" + entry.getCustomer());
								if (StringUtils.isNotEmpty(entry.getContact())) {
									suffixTitle.append(" - " + entry.getContact());
								}
								suffixTitle.append(")");
							}
						} else {
							if (StringUtils.isNotEmpty(entry.getContact())) {
								suffixTitle.append(" - " + entry.getContact());
							}
						}
						final Button title = new Button(entry.getTitle() + suffixTitle.toString());
						title.addStyleName(ValoTheme.BUTTON_LINK);
						// Add click event
						title.addClickListener(new DocLinkListener(KeyDateView.VIEW_NAME, entry.getNoteId()));
						entryRow.addComponent(title);

						// Add summary, if appropriate
						if (StringUtils.isNotEmpty(entry.getDescription())) {
							final Label summary = new Label(entry.getDescription());
							summary.setContentMode(ContentMode.HTML);
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

	/**
	 * Getter for viewWrapper
	 *
	 * @return KeyDateViewWrapper that wraps the backend views used by this page
	 */
	public KeyDateViewWrapper getViewWrapper() {
		return viewWrapper;
	}

	/**
	 * Setter for viewWrapper
	 *
	 * @param viewWrapper
	 *            KeyDateViewWrapper that wraps the backend views used by this
	 *            page
	 */
	public void setViewWrapper(KeyDateViewWrapper viewWrapper) {
		this.viewWrapper = viewWrapper;
	}

	/**
	 * Getter for pager
	 *
	 * @return Pager to be displayed above the view container
	 */
	public Pager getPager() {
		return pager;
	}

	/**
	 * Setter for pager
	 *
	 * @param pager
	 *            Pager to be displayed above the view container
	 */
	public void setPager(Pager pager) {
		this.pager = pager;
	}

	/**
	 * Getter for filter
	 *
	 * @return MainViewFilter that allows filtering of contents of viewWrapper
	 */
	public MainViewFilter getFilter() {
		return filter;
	}

	/**
	 * Setter for filter
	 *
	 * @param filter
	 *            MainViewFilter that allows filtering of the contents of the
	 *            viewWrapper
	 */
	public void setFilter(MainViewFilter filter) {
		this.filter = filter;
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
