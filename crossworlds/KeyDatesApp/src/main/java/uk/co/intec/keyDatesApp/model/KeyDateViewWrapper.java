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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.openntf.domino.Database;
import org.openntf.domino.DateRange;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;
import org.openntf.osgiworlds.model.AbstractViewWrapper;
import org.openntf.osgiworlds.model.GenericDatabaseUtils;
import org.openntf.osgiworlds.model.ViewEntryWrapper;

import com.ibm.commons.util.StringUtil;

import uk.co.intec.keyDatesApp.pages.MainView;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Wrapper for Key Date views
 */
public class KeyDateViewWrapper extends AbstractViewWrapper {
	private static final long serialVersionUID = 1L;
	private String customerName;
	private Date startDate;

	/**
	 * @author Paul Withers<br/>
	 *         <br/>
	 *         Enum mapping to the underlying View names, to avoid typos
	 */
	public enum ViewType {
		BY_DATE("keyDatesCal"), BY_CUST("KeyDatesByCust");

		private String value_;

		private ViewType(String value) {
			value_ = value;
		}

		public String getValue() {
			return value_;
		}
	}

	/**
	 * Constructor, defaulting to keyDatesCal view (we're categorising
	 * on-the-fly, so we can use the Calendar view) and no customer restriction.
	 */
	public KeyDateViewWrapper() {
		super();
		setViewName(ViewType.BY_DATE.getValue());
		setCustomerName("");
	}

	/**
	 * Overloaded constructor, additionally loading the page into this wrapper.
	 *
	 * @param parentView
	 *            com.vaadin.navigator.View page displayed
	 */
	public KeyDateViewWrapper(com.vaadin.navigator.View parentView) {
		super(parentView);
		setViewName(ViewType.BY_DATE.getValue());
		setCustomerName("");
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.AbstractViewWrapper#getParentDatabase()
	 */
	@Override
	public Database getParentDatabase() {
		return GenericDatabaseUtils.getDataDb();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.AbstractViewWrapper#redrawContents()
	 */
	@Override
	public void redrawContents() {
		final MainView view = (MainView) getParentVaadinView();
		view.loadRowData(getEntriesAsMap());
		view.getPager().loadPagerPagesButtons();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.AbstractViewWrapper#getEntries()
	 */
	@Override
	public List<ViewEntryWrapper> getEntries() {
		// NOT IN USE!!!
		return super.getEntries();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.AbstractViewWrapper#getEntriesAsMap()
	 */
	@Override
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap() {
		return getEntriesAsMap(getCustomerName(), getStartDate(), isSingleCat(), getCount());
	}

	/**
	 * Overloaded version of getEntriesAsMap() passing in the customer name,
	 * start date, whether or not to restrict only to entries for that date and
	 * customer, and number of rows to display at a time.
	 *
	 * @param custName
	 *            String customer name to restrict the wrapper to
	 * @param startDate
	 *            Date start date to restrict the wrapper to
	 * @param single
	 *            boolean whether or not to only display matches
	 * @param rows
	 *            int number of ViewEntries to display
	 * @return Map of KeyDateEntryWrappers to display to the user
	 */
	@SuppressWarnings("deprecation")
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap(String custName, Date startDate, boolean single, int rows) {
		Map<Object, List<ViewEntryWrapper>> retVal_ = new HashMap<Object, List<ViewEntryWrapper>>();
		try {
			boolean resetWrapper = false;
			startDate.setHours(0);
			startDate.setMinutes(0);
			startDate.setSeconds(0);
			// Have any passed values changes? If so, we need to reset start
			if (!StringUtil.equalsIgnoreCase(custName, getCustomerName())) {
				resetWrapper = true;
			} else if (single != isSingleCat()) {
				resetWrapper = true;
			} else {
				if (null == getStartDate() && null != startDate) {
					resetWrapper = true;
				} else if (null != getStartDate() && null == startDate) {
					resetWrapper = true;
				} else {
					final GregorianCalendar passedCal = new GregorianCalendar();
					passedCal.setTime(startDate);
					final GregorianCalendar thisCal = new GregorianCalendar();
					thisCal.setTime(getStartDate());
					if (passedCal.get(GregorianCalendar.DATE) != thisCal.get(GregorianCalendar.DATE)) {
						resetWrapper = true;
					} else if (passedCal.get(GregorianCalendar.MONTH) != thisCal.get(GregorianCalendar.MONTH)) {
						resetWrapper = true;
					} else if (passedCal.get(GregorianCalendar.YEAR) != thisCal.get(GregorianCalendar.YEAR)) {
						resetWrapper = true;
					}
				}
			}

			// Have rows to display change?
			if (getCount() != rows) {
				// Yes, so re-do search from same start point
				setCount(rows);
			}

			if (resetWrapper) {
				setStart(1);
				setCurrentPage(1);
			}
			setCustomerName(custName);
			setStartDate(startDate);
			setSingleCat(single);

			// If a customer name has been supplied, filter from that view
			if (StringUtil.isEmpty(getCustomerName())) {
				retVal_ = getEntriesByDate();
			} else {
				retVal_ = getEntriesByCustomer();
			}
		} catch (final Throwable t) {
			KeyDateDatabaseUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Get entries only based on a passed date. createDateRangeFromStartDate
	 * handles getting just selected date or all from selected date.<br/>
	 * <br/>
	 * If there are no entries after the selected date, just abort.<br/>
	 * <br/>
	 * Otherwise, load <i>count</i> entries into the wrapper. If there are fewer
	 * entries than <i>count</i> in the view, set <i>endOfView</i> to true.
	 * Also, recalculate the available pages.
	 *
	 * @return Map of KeyDateEntryWrappers to display to the user
	 */
	public Map<Object, List<ViewEntryWrapper>> getEntriesByDate() {
		final Map<Object, List<ViewEntryWrapper>> retVal_ = new TreeMap<Object, List<ViewEntryWrapper>>();
		try {
			setViewName(ViewType.BY_DATE.getValue());
			final View dateView = getView();
			ViewNavigator nav = dateView.createViewNav();
			// Get first entry for the relevant start date, or go to last
			final DateRange dtRange = createDateRangeFromStartDate();
			final ViewEntryCollection vec = dateView.getAllEntriesByKey(dtRange, true);
			final ViewEntry tmpEnt = vec.getFirstEntry();
			if (null == tmpEnt) {
				return null;
			}
			nav = dateView.createViewNavFrom(tmpEnt);
			ViewEntry ent = nav.getNth(getStart());
			int addedRows = 0;
			while (null != ent && addedRows < getCount()) {
				if (ent.isDocument()) {
					addWrappedEntryToMap(retVal_, ent);
					addedRows = addedRows + 1;
				}
				ent = nav.getNext();
			}
			if (null == ent) {
				setEndOfView(true);
			} else {
				setEndOfView(false);
			}

			calculateAvailablePages(nav);
		} catch (final Throwable t) {
			KeyDateDatabaseUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Get entries based on customer and date (date is always set). If
	 * "Restrict to Date" is selected on the browser (isSingleCat=true), then
	 * only get entries for the selected date, otherwise for a year into the
	 * future.<br/>
	 * <br/>
	 * If isSingleCat and there are none that match, just abort.<br/>
	 * <br/>
	 * Otherwise, get the entries for selected customer starting from selected
	 * date. If there are none after selected date, also just abort.<br/>
	 * <br/>
	 * Otherwise, load <i>count</i> entries into the wrapper. If there are fewer
	 * entries than <i>count</i> in the view, set <i>endOfView</i> to true.
	 * Also, recalculate the available pages.
	 *
	 * @return of KeyDateEntryWrappers to display to the user
	 */
	@SuppressWarnings("deprecation")
	public Map<Object, List<ViewEntryWrapper>> getEntriesByCustomer() {
		// View is sorted DESCENDING, so use TreeMap to sort ASCENDING
		final Map<Object, List<ViewEntryWrapper>> retVal_ = new TreeMap<Object, List<ViewEntryWrapper>>();
		try {
			setViewName(ViewType.BY_CUST.getValue());
			final View custView = getView();
			final ViewNavigator nav = custView.createViewNavFromCategory(getCustomerName());
			ViewEntryCollection ec = null;
			ViewEntry ent = nav.getNth(getStart());
			final List<Object> keys = new ArrayList<Object>();
			keys.add(getCustomerName());
			keys.add(createDateRangeFromStartDate());
			int addedRows = 0;
			if (isSingleCat()) {
				ec = custView.getAllEntriesByKey(keys, true);
				ent = ec.getNthEntry(getStart());
				if (null == ent) {
					return null;
				} else {
					// Old style while loop, because we actually want to know we
					// still have more entries
					while (null != ent && addedRows < getCount()) {
						addWrappedEntryToMap(retVal_, ent);
						addedRows = addedRows + 1;
						ent = ec.getNextEntry();
					}
				}
			} else {
				ent = custView.getFirstEntryByKey(keys, true);
				if (null == ent) {
					return null;
				}
				// Can't use createViewNavFrom() and getNth(), because we don't
				// want next customer
				nav.gotoEntry(ent);
				for (int i = 1; i < getStart(); i++) {
					ent = nav.getNext();
				}
				while (null != ent && addedRows < getCount()) {
					addWrappedEntryToMap(retVal_, ent);
					addedRows = addedRows + 1;
					ent = nav.getNext();
				}
			}
			if (null == ent) {
				setEndOfView(true);
			} else {
				setEndOfView(false);
			}

			if (isSingleCat()) {
				calculateAvailablePages(ec);
			} else {
				calculateAvailablePages(nav);
			}
		} catch (final Throwable t) {
			KeyDateDatabaseUtils.handleException(t);
		}
		return retVal_;
	}

	/**
	 * Method to create a KeyDateEntryWrapper and add it to the map.
	 *
	 * @param retVal_
	 *            Map to add the entry to
	 * @param ent
	 *            ViewEntry to wrap
	 */
	private void addWrappedEntryToMap(final Map<Object, List<ViewEntryWrapper>> retVal_, ViewEntry ent) {
		final KeyDateEntryWrapper entWrapper = new KeyDateEntryWrapper();
		entWrapper.setEventDate((Date) ent.getColumnValue("date"));
		entWrapper.setTitle(ent.getColumnValue("title", String.class));
		entWrapper.setDescription(ent.getColumnValue("description", String.class));
		entWrapper.setCustomer(ent.getColumnValue("customer", String.class));
		entWrapper.setContact(ent.getColumnValue("contact", String.class));
		entWrapper.setNoteId(ent.getNoteID());
		List<ViewEntryWrapper> dateEntries = new ArrayList<ViewEntryWrapper>();
		final java.sql.Date mapDate = new java.sql.Date(entWrapper.getEventDate().getTime());
		if (retVal_.containsKey(mapDate)) {
			dateEntries = retVal_.get(mapDate);
		}
		dateEntries.add(entWrapper);
		retVal_.put(mapDate, dateEntries);
	}

	/**
	 * Creates a DateRange based on the start date, extending either a single
	 * day or a month. The DateRange is then used to restrict the wrapper's
	 * contents.
	 *
	 * @return DateRange either a single day (if isSingleCat()) or five years
	 */
	private DateRange createDateRangeFromStartDate() {
		final Calendar eCal = Calendar.getInstance();
		eCal.setTime(getStartDate());
		if (isSingleCat()) {
			eCal.add(Calendar.DATE, 1);
		} else {
			eCal.add(Calendar.YEAR, 5);
		}
		final DateRange dtRange = GenericDatabaseUtils.getUserSession().createDateRange(getStartDate(), eCal.getTime());
		return dtRange;
	}

	/**
	 * Getter for customer name
	 *
	 * @return String customer name to restrict to
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * Setter for customer name
	 *
	 * @param customerName
	 *            String customer name to restrict to
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * Getter for startDate
	 *
	 * @return Date from which to start wrapper
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter for startDate
	 *
	 * @param startDate
	 *            Date from which to start wrapper
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
