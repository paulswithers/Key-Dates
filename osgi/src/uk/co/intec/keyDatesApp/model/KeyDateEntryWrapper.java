package uk.co.intec.keyDatesApp.model;

import java.util.Date;

import org.openntf.osgiworlds.model.AbstractViewEntryWrapper;

public class KeyDateEntryWrapper extends AbstractViewEntryWrapper {
	private static final long serialVersionUID = 1L;
	private Date eventDate;
	private String title;
	private String description;
	private String customer;
	private String contact;

	public KeyDateEntryWrapper() {

	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("Date:" + getEventDate().toGMTString()); // quick and dirty
		sb.append("Title:" + getTitle());
		sb.append("Description:" + getDescription());
		sb.append("Customer:" + getCustomer());
		sb.append("Contacts:" + getContact());
		return sb.toString();
	}

}
