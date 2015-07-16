package org.openntf.osgiworlds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;

import uk.co.intec.keyDatesApp.utils.AppUtils;

import com.vaadin.sass.internal.util.StringUtil;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;

public class AbstractCalendarEventProvider implements CalendarEventProvider {
	private static final long serialVersionUID = 1L;
	private Date startDate;
	private Date endDate;

	@Override
	public List<CalendarEvent> getEvents(Date sDate, Date eDate) {
		final List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		try {
			final Database dataDb = AppUtils.getDataDb();
			final View calView = dataDb.getView("Calendar");
			final DateRange dtRange = AppUtils.getUserSession().createDateRange(sDate, eDate);
			final ViewEntryCollection vec = calView.getAllEntriesByKey(dtRange, true);
			for (final ViewEntry ent : vec) {
				final BasicEvent calEnt = new BasicEvent();
				calEnt.setStart(ent.getColumnValue("$146", Date.class));
				calEnt.setEnd(ent.getColumnValue("$134", Date.class));
				final String icon = ent.getColumnValue("$149", String.class);
				if (StringUtil.containsSubString(icon, "194")) {
					calEnt.setAllDay(true);
				} else {
					calEnt.setAllDay(false);
				}
				final ArrayList<String> subjectDetails = ent.getColumnValue("$147", ArrayList.class);
				calEnt.setCaption(subjectDetails.get(0));
				if (subjectDetails.size() > 1) {
					calEnt.setDescription(subjectDetails.get(0) + ", " + subjectDetails.get(1));
				} else {
					calEnt.setDescription(calEnt.getCaption());
				}
				events.add(calEnt);
			}
		} catch (final Throwable t) {
			AppUtils.handleException(t);
		}
		return events;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
