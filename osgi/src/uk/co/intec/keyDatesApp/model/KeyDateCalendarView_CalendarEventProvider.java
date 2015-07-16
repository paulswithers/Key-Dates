package uk.co.intec.keyDatesApp.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.osgiworlds.AbstractCalendarEventProvider;

import uk.co.intec.keyDatesApp.utils.AppUtils;

import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;

public class KeyDateCalendarView_CalendarEventProvider extends AbstractCalendarEventProvider {
	private static final long serialVersionUID = 1L;

	public KeyDateCalendarView_CalendarEventProvider() {

	}

	@Override
	public List<CalendarEvent> getEvents(Date sDate, Date eDate) {
		final List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		try {
			final Database dataDb = AppUtils.getDataDb();
			final View calView = dataDb.getView("keyDatesCal");
			final DateRange dtRange = AppUtils.getUserSession().createDateRange(sDate, eDate);
			final ViewEntryCollection vec = calView.getAllEntriesByKey(dtRange, true);
			for (final ViewEntry ent : vec) {
				final BasicEvent calEnt = new BasicEvent();
				final Vector colVals = ent.getColumnValues();
				calEnt.setStart((Date) colVals.get(0));
				calEnt.setEnd((Date) colVals.get(0));
				calEnt.setAllDay(true);
				calEnt.setCaption((String) colVals.get(1));
				calEnt.setDescription((String) colVals.get(1));
				events.add(calEnt);
			}
		} catch (final Throwable t) {
			AppUtils.handleException(t);
		}
		// Override
		return events;
	}

}
