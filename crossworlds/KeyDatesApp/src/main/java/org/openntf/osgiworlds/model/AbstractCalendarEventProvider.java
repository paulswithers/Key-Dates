package org.openntf.osgiworlds.model;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;

import com.vaadin.sass.internal.util.StringUtil;
import com.vaadin.ui.components.calendar.event.BasicEvent;
import com.vaadin.ui.components.calendar.event.CalendarEvent;
import com.vaadin.ui.components.calendar.event.CalendarEventProvider;

import uk.co.intec.keyDatesApp.model.KeyDateDatabaseUtils;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Basic Abstract class extending Vaadin CalendarEventProvider, for a
 *         Domino CalendarView. <b>NOTE:</b> I have not fully tested this.
 *
 */
public class AbstractCalendarEventProvider implements CalendarEventProvider {
	private static final long serialVersionUID = 1L;
	private Date startDate;
	private Date endDate;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.ui.components.calendar.event.CalendarEventProvider#getEvents(
	 * java.util.Date, java.util.Date)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CalendarEvent> getEvents(Date sDate, Date eDate) {
		final List<CalendarEvent> events = new ArrayList<CalendarEvent>();
		try {
			final Database dataDb = GenericDatabaseUtils.getDataDb();
			final View calView = dataDb.getView("Calendar");
			final DateRange dtRange = GenericDatabaseUtils.getUserSession().createDateRange(sDate, eDate);
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
			KeyDateDatabaseUtils.handleException(t);
		}
		return events;
	}

	/**
	 * Getter for startDate, the date from which the Vaadin Calendar starts
	 *
	 * @return Date start for the current display
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter for startDate, the date from which the Vaadin Calendar starts
	 *
	 * @param startDate
	 *            Date start date for the current display
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for endDate, the date with which the Vaadin Calendar ends
	 *
	 * @return Date end date for the current display
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter for endDate, the date with which the Vaadin Calendar ends
	 *
	 * @param endDate
	 *            Date end date for the current display
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
