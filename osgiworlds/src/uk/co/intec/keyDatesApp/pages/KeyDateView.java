package uk.co.intec.keyDatesApp.pages;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.vaadin.data.Property;
import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.data.fieldgroup.DefaultFieldGroupFieldFactory;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.ValoTheme;

import uk.co.intec.keyDatesApp.MainUI;
import uk.co.intec.keyDatesApp.model.KeyDateBean;
import uk.co.intec.keyDatesApp.model.KeyDateDatabaseUtils;
import uk.co.intec.keyDatesApp.model.KeyDateDocumentWrapper;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Edit page for an individual Key Date.
 *
 */
public class KeyDateView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "KeyDate";
	public static final String VIEW_LABEL = "New Key Date";
	private boolean loaded = false;
	private FormLayout editForm = new FormLayout();
	private KeyDateDocumentWrapper currDoc;
	private KeyDateBean currDocBean = new KeyDateBean();
	private BeanFieldGroup<KeyDateBean> beanFields = new BeanFieldGroup<KeyDateBean>(KeyDateBean.class);
	private ComboBox customerCombo;
	private ComboBox contactCombo;
	private Button editButton;
	private Button saveButton;

	/**
	 * Constructor, sets CssLayout to take full area
	 */
	public KeyDateView() {

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
			final String[] params = event.getParameters().split("/");
			if (StringUtils.isEmpty(params[0])) {
				setCurrDoc(new KeyDateDocumentWrapper("", true));
			} else {
				final String docId = params[0];
				setCurrDoc(new KeyDateDocumentWrapper(docId, false));
			}
			getCurrDocBean().loadFromDocumentWrapper(getCurrDoc());
			if (!isLoaded()) {
				loadContent();
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
		getEditForm().setSizeUndefined();
		getEditForm().setWidth("100%");

		getBeanFields().setBuffered(false);
		getBeanFields().setItemDataSource(getCurrDocBean());
		getBeanFields().setFieldFactory(DefaultFieldGroupFieldFactory.get());
		getBeanFields().setReadOnly(getCurrDoc().isReadOnly());

		final TextField t1 = (TextField) getBeanFields().buildAndBind("Title: ", "title");
		t1.setNullRepresentation("");
		t1.setSizeFull();
		t1.setRequired(true);
		t1.setRequiredError("Please enter title");

		final DateField d1 = getBeanFields().buildAndBind("Date: ", "date", DateField.class);
		d1.setRequired(true);
		d1.setRequiredError("Please enter date");
		d1.setRangeStart(getStartDate());
		d1.setRangeEnd(getEndDate());

		final TextArea ta1 = getBeanFields().buildAndBind("Description: ", "description", TextArea.class);
		ta1.setSizeFull();

		setCustomerCombo(new ComboBox("Customer:", KeyDateDatabaseUtils.getCustContainer()));
		getCustomerCombo().setInputPrompt("No Customer Selected");
		getCustomerCombo().setFilteringMode(FilteringMode.STARTSWITH);
		getCustomerCombo().setImmediate(true);
		getCustomerCombo().setNewItemsAllowed(true);
		getCustomerCombo().setInvalidAllowed(false);
		getCustomerCombo().setNullSelectionAllowed(true);
		getCustomerCombo().setPageLength(5);
		getCustomerCombo().setWidth("100%");
		getCustomerCombo().setResponsive(true);
		getBeanFields().bind(getCustomerCombo(), "customer");
		getCustomerCombo().addValueChangeListener(new Property.ValueChangeListener() {
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
				getContactCombo().setContainerDataSource(KeyDateDatabaseUtils.getContactContainer((String) event.getProperty().getValue()));
				getContactCombo().setValue("");
			}
		});

		setContactCombo(new ComboBox("Contact:"));
		getContactCombo().setInputPrompt("No Contact Selected");
		getContactCombo().setFilteringMode(FilteringMode.STARTSWITH);
		getContactCombo().setImmediate(true);
		getContactCombo().setNewItemsAllowed(true);
		getContactCombo().setInvalidAllowed(false);
		getContactCombo().setNullSelectionAllowed(true);
		getContactCombo().setPageLength(5);
		getContactCombo().setWidth("100%");
		getContactCombo().setResponsive(true);
		getBeanFields().bind(getContactCombo(), "contact");

		final HorizontalLayout buttonRow = new HorizontalLayout();
		setSaveButton(new Button("Save"));
		getSaveButton().addStyleName(ValoTheme.BUTTON_PRIMARY);
		getSaveButton().setIcon(FontAwesome.SAVE);
		getSaveButton().setVisible(getCurrDoc().isEditable());
		getSaveButton().addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
			 * Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				getCurrDocBean().saveDocumentWrapper(getCurrDoc());
				getCurrDoc().setEditMode(false);
				getCurrDoc().setReadOnly(true);
				getBeanFields().setReadOnly(true);
				getSaveButton().setVisible(false);
				getEditButton().setVisible(true);
			}
		});

		setEditButton(new Button("Edit"));
		getEditButton().addStyleName(ValoTheme.BUTTON_PRIMARY);
		getEditButton().setIcon(FontAwesome.EDIT);
		getEditButton().setVisible(!getCurrDoc().isEditable());
		getEditButton().addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
			 * Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				getCurrDoc().setEditMode(true);
				getCurrDoc().setReadOnly(false);
				getBeanFields().setReadOnly(false);
				getEditButton().setVisible(false);
				getSaveButton().setVisible(true);
			}
		});

		final Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName(ValoTheme.BUTTON_QUIET);
		cancelButton.setIcon(FontAwesome.UNDO);
		cancelButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			/*
			 * (non-Javadoc)
			 *
			 * @see
			 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
			 * Button.ClickEvent)
			 */
			@Override
			public void buttonClick(ClickEvent event) {
				MainUI.get().getNavigator().navigateTo(MainView.VIEW_NAME);
			}
		});
		buttonRow.addComponents(getEditButton(), getSaveButton(), cancelButton);

		getEditForm().addComponents(t1, d1, ta1, getCustomerCombo(), getContactCombo(), buttonRow);

		addComponent(editForm);

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

	/**
	 * Getter for currDoc
	 *
	 * @return KeyDateDocumentWrapper wrapper for backend document being edited
	 */
	public KeyDateDocumentWrapper getCurrDoc() {
		return currDoc;
	}

	/**
	 * Setter for currDoc
	 *
	 * @param currDoc
	 *            KeyDateDocumentWrapper wrapper for backend document being
	 *            edited
	 */
	public void setCurrDoc(KeyDateDocumentWrapper currDoc) {
		this.currDoc = currDoc;
	}

	/**
	 * @return the currDocBean
	 */
	public KeyDateBean getCurrDocBean() {
		return currDocBean;
	}

	/**
	 * @param currDocBean
	 *            the currDocBean to set
	 */
	public void setCurrDocBean(KeyDateBean currDocBean) {
		this.currDocBean = currDocBean;
	}

	/**
	 * Getter for endDate
	 *
	 * @return Date end date up to which users can create a Key Date, 5 years in
	 *         the future
	 */
	public Date getEndDate() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.YEAR, 5);
		return cal.getTime();
	}

	/**
	 * Getter for endDate
	 *
	 * @return Date end date up to which users can create a Key Date, 1 month in
	 *         the past
	 */
	public Date getStartDate() {
		final Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.MONTH, -1);
		return cal.getTime();
	}

	/**
	 * Getter for editForm
	 *
	 * @return FormLayout editable form area, required to switch readOnly on and
	 *         off
	 */
	public FormLayout getEditForm() {
		return editForm;
	}

	/**
	 * Setter for editForm
	 *
	 * @param editForm
	 *            FormLayout editable form area, required to switch readOnly on
	 *            and off
	 */
	public void setEditForm(FormLayout editForm) {
		this.editForm = editForm;
	}

	/**
	 * Getter for customerCombo
	 *
	 * @return ComboBox for selecting (or adding) a customer
	 */
	public ComboBox getCustomerCombo() {
		return customerCombo;
	}

	/**
	 * Setter for customerCombo
	 *
	 * @param customerCombo
	 *            ComboBox for selecting (or adding) a customer
	 */
	public void setCustomerCombo(ComboBox customerCombo) {
		this.customerCombo = customerCombo;
	}

	/**
	 * Getter for contactCombo
	 *
	 * @return ComboBox for selecting (or adding) a contact
	 */
	public ComboBox getContactCombo() {
		return contactCombo;
	}

	/**
	 * Setter for contactCombo
	 *
	 * @param contactCombo
	 *            ComboBox for selecting (or adding) a contact
	 */
	public void setContactCombo(ComboBox contactCombo) {
		this.contactCombo = contactCombo;
	}

	/**
	 * Getter for beanFields
	 *
	 * @return BeanFieldGroup of fields from KeyDateBean object
	 */
	public BeanFieldGroup<KeyDateBean> getBeanFields() {
		return beanFields;
	}

	/**
	 * Setter for beanFields
	 *
	 * @param beanFields
	 *            BeanFieldGroup of fields from KeyDateBean object
	 */
	public void setBeanFields(BeanFieldGroup<KeyDateBean> beanFields) {
		this.beanFields = beanFields;
	}

	/**
	 * Getter for editButton
	 *
	 * @return Button for editing
	 */
	public Button getEditButton() {
		return editButton;
	}

	/**
	 * Setter for editButton
	 *
	 * @param editButton
	 *            Button for editing
	 */
	public void setEditButton(Button editButton) {
		this.editButton = editButton;
	}

	/**
	 * Getter for saveButton
	 *
	 * @return Button for saving
	 */
	public Button getSaveButton() {
		return saveButton;
	}

	/**
	 * Setter for saveButton
	 *
	 * @param saveButton
	 *            Button for saving
	 */
	public void setSaveButton(Button saveButton) {
		this.saveButton = saveButton;
	}

}
