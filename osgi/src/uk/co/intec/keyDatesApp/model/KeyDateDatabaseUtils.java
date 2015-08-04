package uk.co.intec.keyDatesApp.model;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewNavigator;
import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import com.vaadin.data.Item;
import com.vaadin.data.util.AbstractContainer;

public class KeyDateDatabaseUtils {

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
}
