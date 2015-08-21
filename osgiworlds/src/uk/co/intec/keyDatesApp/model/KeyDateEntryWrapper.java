package uk.co.intec.keyDatesApp.model;

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

import java.util.Date;

import org.openntf.osgiworlds.model.AbstractViewEntryWrapper;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Wrapper for Key Date ViewEntry.
 *
 */
public class KeyDateEntryWrapper extends AbstractViewEntryWrapper {
	private static final long serialVersionUID = 1L;
	private Date eventDate;
	private String title;
	private String description;
	private String customer;
	private String contact;

	/**
	 * Constructor
	 */
	public KeyDateEntryWrapper() {

	}

	/**
	 * Getter for eventDate
	 *
	 * @return Date the Key Date is set for
	 */
	public Date getEventDate() {
		return eventDate;
	}

	/**
	 * Setter for eventDate
	 *
	 * @param eventDate
	 *            Date the Key Date is set for
	 */
	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	/**
	 * Getter for title
	 *
	 * @return String title of the Key Date event
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for title
	 *
	 * @param title
	 *            String title of the Key Date event
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for description
	 *
	 * @return String description of the Key Date event
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Setter for description
	 *
	 * @param description
	 *            String description of the Key Date event
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Getter for customer
	 *
	 * @return String customer the Key Date event is for
	 */
	public String getCustomer() {
		return customer;
	}

	/**
	 * Setter for customer
	 *
	 * @param customer
	 *            String customer the Key Date event is for
	 */
	public void setCustomer(String customer) {
		this.customer = customer;
	}

	/**
	 * Getter for contact
	 *
	 * @return String contact the Key Date event is for
	 */
	public String getContact() {
		return contact;
	}

	/**
	 * Setter for contact
	 *
	 * @param contact
	 *            String contact the Key Date event is for
	 */
	public void setContact(String contact) {
		this.contact = contact;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */
	@SuppressWarnings("deprecation")
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
