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

import org.apache.commons.lang.StringUtils;
import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.AbstractContainer;
import com.vaadin.data.util.IndexedContainer;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Class for application-specific Domino-specific utilities.
 */
public class KeyDateDatabaseUtils {
	private static final String CUST_CONTAINER_PROPERTY = "CUSTOMER_NAME";
	private static final String CONTACT_CONTAINER_PROPERTY = "CONTACT_NAME";

	/**
	 * Gets the customers Key Dates have been set up for into a Vaadin Container
	 * class.
	 *
	 * @param container
	 *            AbstractContainer or extension into which to load the
	 *            customers
	 * @param property
	 *            String ItemProperty of the container into which to load the
	 *            customer name
	 */
	@SuppressWarnings("unchecked")
	public static void loadCustomersToContainer(AbstractContainer container, String property) {
		final Database currDb = GenericDatabaseUtils.getDataDb();
		final View lkupView = currDb.getView("(luCustomers)");
		final ViewNavigator nav = lkupView.createViewNavMaxLevel(0);
		ViewEntry ent = nav.getFirst();
		while (null != ent) {
			final Item item = container.addItem(ent.getColumnValue("customer"));
			if (item != null) {
				item.getItemProperty(property).setValue(ent.getColumnValue("customer"));
			}
			ent = nav.getNextSibling();
		}
	}

	/**
	 * Gets an IndexedContainer of customers for whom Key Dates have been
	 * created
	 *
	 * @return IndexedContainer of customers
	 */
	public static IndexedContainer getCustContainer() {
		final IndexedContainer custContainer = new IndexedContainer();
		custContainer.addContainerProperty(CUST_CONTAINER_PROPERTY, String.class, "");
		KeyDateDatabaseUtils.loadCustomersToContainer(custContainer, CUST_CONTAINER_PROPERTY);
		return custContainer;
	}

	/**
	 * Gets a unique list of contacts Key Dates have been created for, based on
	 * the selected customer
	 *
	 * @param container
	 *            AbstractContainer or extension into which to load the
	 *            customers
	 * @param property
	 *            String ItemProperty of the container into which to load the
	 *            customer name
	 * @param customer
	 *            String customer to filter on
	 */
	public static void loadContactsToContainer(AbstractContainer container, String property, String customer) {
		final Database currDb = GenericDatabaseUtils.getDataDb();
		final View lkupView = currDb.getView("(luCustomers)");
		final ViewNavigator nav = lkupView.createViewNavFromCategory(customer);
		ViewEntry ent = nav.getFirst();
		while (null != ent) {
			if (StringUtils.isNotEmpty((String) ent.getColumnValue("contact"))) {
				final Item item = container.addItem(ent.getColumnValue("contact"));
				if (item != null) {
					item.getItemProperty(property).setValue(ent.getColumnValue("contact"));
				}
			}
			ent = nav.getNextSibling();
		}
	}

	/**
	 * Gets an IndexedContainer of contacts for whom Key Dates have been
	 * created, based on the passed customer
	 *
	 * @param customer
	 *            String customer already selected
	 * @return IndexedContainer of customers
	 */
	public static IndexedContainer getContactContainer(String customer) {
		final IndexedContainer contactContainer = new IndexedContainer();
		contactContainer.addContainerProperty(CONTACT_CONTAINER_PROPERTY, String.class, "");
		KeyDateDatabaseUtils.loadContactsToContainer(contactContainer, CONTACT_CONTAINER_PROPERTY, customer);
		return contactContainer;
	}

	/**
	 * Exception handling and logging for the application
	 *
	 * @param t
	 *            Throwable or Exception encountered
	 */
	public static void handleException(Throwable t) {
		DominoUtils.handleException(t);
	}
}
