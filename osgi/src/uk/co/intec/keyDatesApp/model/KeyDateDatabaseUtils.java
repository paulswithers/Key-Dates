package uk.co.intec.keyDatesApp.model;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.domino.utils.DominoUtils;
import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.AbstractContainer;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Class for application-specific Domino-specific utilities.
 */
public class KeyDateDatabaseUtils {

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
	 * Exception handling and logging for the application
	 *
	 * @param t
	 *            Throwable or Exception encountered
	 */
	public static void handleException(Throwable t) {
		DominoUtils.handleException(t);
	}
}
