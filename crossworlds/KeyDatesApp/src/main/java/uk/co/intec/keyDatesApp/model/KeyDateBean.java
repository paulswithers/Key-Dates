package uk.co.intec.keyDatesApp.model;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import uk.co.intec.keyDatesApp.pages.KeyDateView;

/**
 * @author Paul Withers
 *
 *         Bean backing the KeyDateDocumentWrapper, exposing only the relevant
 *         fields users should be able to edit from the KeyDateView page.<br/>
 *         <br/>
 *         This may seem tautologous, to have a wrapper extending another
 *         wrapper for the underlying Document. But they perform different
 *         functions. The KeyDateDocumentWrapper is designed to handle
 *         consistent validation and processing functions for <i>any</i> Key
 *         Date, whether edited by the user or the system. This bean is designed
 *         to map specific fields available for editing from an individual
 *         FormLayout control, to handle loading from the KeyDateDocumentWrapper
 *         loaded to the view and handle saving back to it.
 *
 */
public class KeyDateBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title;
	private Date date;
	private String description;
	private String customer;
	private String contact;
	private KeyDateView parentView;

	public KeyDateBean() {

	}

	/**
	 * Getter for title
	 *
	 * @return String title of Key Date
	 */
	@NotNull(message = "Please enter title")
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for title
	 *
	 * @param title
	 *            String title of Key Date
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for date. This is a java.util.Date. The system is still storing
	 * date and time into the document.
	 *
	 * @return Date date of Key Date
	 */
	@NotNull(message = "Please enter date")
	public Date getDate() {
		return date;
	}

	/**
	 * Setter for date.
	 *
	 * @param date
	 *            Date date of Key Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Getter for description
	 *
	 * @return String description of Key Date
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 *
	 * @param description
	 *            String description of Key Date
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for customer
	 *
	 * @return String customer for Key Date
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * Setter for customer
	 *
	 * @param customer
	 *            String customer for Key Date
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * Getter for contact
	 *
	 * @return String contact for Key Date
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * Setter for contact
	 *
	 * @param contact
	 *            String contact for Key Date
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/**
	 * Getter for parentView
	 *
	 * @return KeyDateView view (page) containing the FormLayout this bean is
	 *         used on
	 */
	public KeyDateView getParentView() {
		return parentView;
	}

	/**
	 * Setter for parentView
	 *
	 * @param parentView
	 *            KeyDateView view (page) containing the FormLayout this bean is
	 *            used on
	 */
	public void setParentView(KeyDateView parentView) {
		this.parentView = parentView;
	}

	/**
	 * Initialise the values for this bean from the backend document's wrapper.
	 *
	 * @param wrappedDoc
	 *            KeyDateDocumentWrapper wrapper for the document being edited
	 */
	public void loadFromDocumentWrapper(KeyDateDocumentWrapper wrappedDoc) {
		setTitle((String) wrappedDoc.getValue("title"));
		setDate(wrappedDoc.getDateValue("date"));
		setDescription(wrappedDoc.getValueAsString("description"));
		setCustomer((String) wrappedDoc.getValue("customer"));
		setContact((String) wrappedDoc.getValue("contact"));
	}

	/**
	 * Passes the values for this bean back to the backend document's wrapper
	 * and calls its save method.
	 *
	 * @param wrappedDoc
	 *            KeyDateDocumentWrapper wrapper for the document being edited
	 */
	public void saveDocumentWrapper(KeyDateDocumentWrapper wrappedDoc) {
		wrappedDoc.setValue("title", getTitle());
		wrappedDoc.setValue("date", getDate());
		wrappedDoc.setValue("description", getDescription());
		wrappedDoc.setValue("customer", getCustomer());
		wrappedDoc.setValue("contact", getContact());
		wrappedDoc.save();
	}

}
