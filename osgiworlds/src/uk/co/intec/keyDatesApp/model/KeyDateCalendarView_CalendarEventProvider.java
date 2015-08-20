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
import org.openntf.osgiworlds.model.AbstractCalendarEventProvider;
import org.openntf.osgiworlds.model.BasicDominoEvent;
import org.openntf.osgiworlds.model.GenericDatabaseUtils;

import com.vaadin.ui.components.calendar.event.CalendarEvent;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Extension of AbstractCalendarEventProvider to get relevant
 *         ViewEntries and load as Vaadin CalendarEvents.
 *
 */
public class KeyDateCalendarView_CalendarEventProvider extends AbstractCalendarEventProvider {
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 */
	public KeyDateCalendarView_CalendarEventProvider() {

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.AbstractCalendarEventProvider#getEvents(java
	 * .util.Date, java.util.Date)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public List<CalendarEvent> getEvents(Date sDate, Date eDate) {
		final List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		try {
			final Database dataDb = GenericDatabaseUtils.getDataDb();
			final View calView = dataDb.getView("keyDatesCal");
			final DateRange dtRange = GenericDatabaseUtils.getUserSession().createDateRange(sDate, eDate);
			final ViewEntryCollection vec = calView.getAllEntriesByKey(dtRange, true);
			for (final ViewEntry ent : vec) {
				final BasicDominoEvent calEnt = new BasicDominoEvent();
				final Vector colVals = ent.getColumnValues();
				calEnt.setStart((Date) colVals.get(0));
				calEnt.setEnd((Date) colVals.get(0));
				calEnt.setAllDay(true);
				calEnt.setCaption((String) colVals.get(1));
				calEnt.setDescription((String) colVals.get(1));
				calEnt.setNoteId(ent.getNoteID());
				events.add(calEnt);
			}
		} catch (final Throwable t) {
			KeyDateDatabaseUtils.handleException(t);
		}
		// Override
		return events;
	}

}
