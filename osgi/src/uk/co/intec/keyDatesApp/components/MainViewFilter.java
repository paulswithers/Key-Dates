package uk.co.intec.keyDatesApp.components;

import java.util.Date;
import java.util.Locale;

import uk.co.intec.keyDatesApp.model.KeyDateDatabaseUtils;
import uk.co.intec.keyDatesApp.model.KeyDateViewWrapper;
import uk.co.intec.keyDatesApp.pages.MainView;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.IndexedContainer;
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

public class MainViewFilter extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
	private static final String CUST_CONTAINER_PROPERTY = "CUSTOMER_NAME";
	private MainView parentView;
	private ComboBox custField;
	private PopupDateField dateField;
	private CheckBox singleCatButton;
	private IndexedContainer custContainer;
	private Table custTable;

	public MainViewFilter() {

	}

	public MainViewFilter(MainView parentView) {
		setParentView(parentView);
		setStyleName(ValoTheme.MENU_SUBTITLE);
		setSizeFull();
		setMargin(false);
		loadContent();
	}

	public MainView getParentView() {
		return parentView;
	}

	public void setParentView(MainView parentView) {
		this.parentView = parentView;
	}

	public void loadContent() {
		final FormLayout cust = new FormLayout();
		cust.setMargin(false);
		setCustField(new ComboBox("Customer:", getCustContainer()));
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

	public ComboBox getCustField() {
		return custField;
	}

	public void setCustField(ComboBox custField) {
		this.custField = custField;
	}

	public IndexedContainer getCustContainer() {
		if (null == custContainer) {
			setCustContainer();
		}
		return custContainer;
	}

	public void setCustContainer() {
		custContainer = new IndexedContainer();
		custContainer.addContainerProperty(CUST_CONTAINER_PROPERTY, String.class, "");
		KeyDateDatabaseUtils.loadCustomersToContainer(custContainer, CUST_CONTAINER_PROPERTY);
	}

	public Table getCustTable() {
		return custTable;
	}

	public void setCustTable(Table custTable) {
		this.custTable = custTable;
	}

	public PopupDateField getDateField() {
		return dateField;
	}

	public void setDateField(PopupDateField dateField) {
		this.dateField = dateField;
	}

	public CheckBox getSingleCatButton() {
		return singleCatButton;
	}

	public void setSingleCatButton(CheckBox singleCatButton) {
		this.singleCatButton = singleCatButton;
	}
}
