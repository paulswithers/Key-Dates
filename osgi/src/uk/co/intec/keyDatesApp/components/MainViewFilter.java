package uk.co.intec.keyDatesApp.components;

import java.util.Date;
import java.util.Locale;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.shared.ui.datefield.Resolution;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PopupDateField;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

import uk.co.intec.keyDatesApp.model.KeyDateDatabaseUtils;
import uk.co.intec.keyDatesApp.model.KeyDateViewWrapper;
import uk.co.intec.keyDatesApp.pages.MainView;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Component that contains selectors for filtering the contains of the
 *         KeyDateViewWrapper.
 *
 */
public class MainViewFilter extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
	private MainView parentView;
	private ComboBox custField;
	private PopupDateField dateField;
	private CheckBox singleCatButton;
	private Table custTable;

	/**
	 * Constructor
	 */
	public MainViewFilter() {

	}

	/**
	 * Overloaded constructor that passes the page the view filter should be
	 * added to. This method sets some styling to this container as well as
	 * loading all the content.
	 *
	 * @param parentView
	 *            MainView page this filter container will be added to
	 */
	public MainViewFilter(MainView parentView) {
		setParentView(parentView);
		setStyleName(ValoTheme.MENU_SUBTITLE);
		setSizeFull();
		setMargin(false);
		loadContent();
	}

	/**
	 * Getter for parentView
	 *
	 * @return MainView page this filter container will be added to
	 */
	public MainView getParentView() {
		return parentView;
	}

	/**
	 * Setter for parentView
	 *
	 * @param parentView
	 *            MainView page this filter container will be added to
	 */
	public void setParentView(MainView parentView) {
		this.parentView = parentView;
	}

	/**
	 * Main method to load the filtering fields and valueChangeListeners for
	 * those fields.
	 */
	public void loadContent() {
		final FormLayout cust = new FormLayout();
		cust.setMargin(false);
		setCustField(new ComboBox("Customer:", KeyDateDatabaseUtils.getCustContainer()));
		getCustField().setInputPrompt("No Customer Selected");
		getCustField().setFilteringMode(FilteringMode.STARTSWITH);
		getCustField().setImmediate(true);
		getCustField().setInvalidAllowed(false);
		getCustField().setNullSelectionAllowed(true);
		getCustField().setPageLength(5);
		getCustField().setWidth("95%");
		getCustField().setResponsive(true);
		getCustField().addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.vaadin.data.Property.ValueChangeListener#valueChange(com.
			 * vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final KeyDateViewWrapper viewWrapper = getParentView().getViewWrapper();
				getParentView().loadRowData(
						viewWrapper.getEntriesAsMap((String) event.getProperty().getValue(), viewWrapper.getStartDate(), viewWrapper.isSingleCat(), viewWrapper.getCount()));
				getParentView().getPager().loadPagerPagesButtons();
			}
		});
		cust.addComponent(getCustField());

		final FormLayout date = new FormLayout();
		date.setMargin(false);
		setDateField(new PopupDateField("Start Date:"));
		getDateField().setValue(new Date());
		getDateField().setResolution(Resolution.DAY);
		getDateField().setLocale(Locale.getDefault());
		getDateField().setResponsive(true);
		getDateField().setTextFieldEnabled(false);
		getDateField().setWidth("95%");
		getDateField().setRequired(true);
		getDateField().setRequiredError("A date is required!");

		getDateField().addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.vaadin.data.Property.ValueChangeListener#valueChange(com.
			 * vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				final KeyDateViewWrapper viewWrapper = getParentView().getViewWrapper();
				getParentView().loadRowData(
						viewWrapper.getEntriesAsMap(viewWrapper.getCustomerName(), (Date) event.getProperty().getValue(), viewWrapper.isSingleCat(), viewWrapper.getCount()));
				getParentView().getPager().loadPagerPagesButtons();
			}
		});

		date.addComponent(getDateField());

		final FormLayout singleCatLayout = new FormLayout();
		singleCatLayout.setMargin(false);
		singleCatLayout.setDefaultComponentAlignment(Alignment.MIDDLE_RIGHT);
		setSingleCatButton(new CheckBox());
		getSingleCatButton().setStyleName(ValoTheme.CHECKBOX_SMALL);
		getSingleCatButton().setResponsive(true);
		getSingleCatButton().setCaption("Restrict to Date");
		getSingleCatButton().setWidth("95%");
		getSingleCatButton().addValueChangeListener(new Property.ValueChangeListener() {
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.vaadin.data.Property.ValueChangeListener#valueChange(com.
			 * vaadin.data.Property.ValueChangeEvent)
			 */
			@Override
			public void valueChange(ValueChangeEvent event) {
				final KeyDateViewWrapper viewWrapper = getParentView().getViewWrapper();
				final Boolean val = (Boolean) event.getProperty().getValue();
				getParentView().loadRowData(viewWrapper.getEntriesAsMap(viewWrapper.getCustomerName(), viewWrapper.getStartDate(), val.booleanValue(), viewWrapper.getCount()));
				getParentView().getPager().loadPagerPagesButtons();
			}
		});
		singleCatLayout.addComponent(getSingleCatButton());

		addComponents(cust, date, singleCatLayout);
		setExpandRatio(cust, 2);
		setExpandRatio(date, 1);
		setExpandRatio(singleCatLayout, 1);
		setComponentAlignment(singleCatLayout, Alignment.MIDDLE_RIGHT);

	}

	/**
	 * Getter for custField
	 *
	 * @return ComboBox for selecting customer
	 */
	public ComboBox getCustField() {
		return custField;
	}

	/**
	 * Setter for custField
	 *
	 * @param custField
	 *            ComboBox for selecting customer
	 */
	public void setCustField(ComboBox custField) {
		this.custField = custField;
	}

	/**
	 * Getter for custTable
	 *
	 * @return Table bound to the custContainer IndexedContainer
	 */
	public Table getCustTable() {
		return custTable;
	}

	/**
	 * Setter for custTable
	 *
	 * @param custTable
	 *            Table bound to the custContainer IndexedContainer
	 */
	public void setCustTable(Table custTable) {
		this.custTable = custTable;
	}

	/**
	 * Getter for dateField
	 *
	 * @return PopupDateField allowing selection of start date for filter
	 */
	public PopupDateField getDateField() {
		return dateField;
	}

	/**
	 * Setter for dateField
	 *
	 * @param dateField
	 *            PopupDateField allowing selection of start date for filter
	 */
	public void setDateField(PopupDateField dateField) {
		this.dateField = dateField;
	}

	/**
	 * Getter for singleCatButton
	 *
	 * @return CheckBox allowing restriction to a single date
	 */
	public CheckBox getSingleCatButton() {
		return singleCatButton;
	}

	/**
	 * Setter for singleCatButton
	 *
	 * @param singleCatButton
	 *            Checkbox allowing restriction to a single date
	 */
	public void setSingleCatButton(CheckBox singleCatButton) {
		this.singleCatButton = singleCatButton;
	}
}
