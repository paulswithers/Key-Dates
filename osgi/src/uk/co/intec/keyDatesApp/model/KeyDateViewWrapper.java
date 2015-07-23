package uk.co.intec.keyDatesApp.model;

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
import org.openntf.osgiworlds.model.ViewEntryWrapper;

import uk.co.intec.keyDatesApp.pages.MainView;
import uk.co.intec.keyDatesApp.utils.AppUtils;

import com.ibm.commons.util.StringUtil;

/**
 * @author withersp
 *
 */
public class KeyDateViewWrapper extends AbstractViewWrapper {
	private static final long serialVersionUID = 1L;
	private String customerName;
	private Date startDate;

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

	public KeyDateViewWrapper() {
		super();
		setViewName(ViewType.BY_DATE.getValue());
		setCustomerName("");
	}

	public KeyDateViewWrapper(com.vaadin.navigator.View parentView) {
		super(parentView);
		setViewName(ViewType.BY_DATE.getValue());
		setCustomerName("");
	}

	@Override
	public Database getParentDatabase() {
		return AppUtils.getDataDb();
	}

	@Override
	public void redrawContents() {
		final MainView view = (MainView) getParentVaadinView();
		view.loadRowData(getEntriesAsMap());
	}

	@Override
	public List<ViewEntryWrapper> getEntries() {
		// NOT IN USE!!!
		return super.getEntries();
	}

	@Override
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap() {
		return getEntriesAsMap(getCustomerName(), getStartDate(), isSingleCat(), getCount());
	}

	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap(String custName, Date startDate, boolean single, int rows) {
		Map<Object, List<ViewEntryWrapper>> retVal_ = new HashMap<Object, List<ViewEntryWrapper>>();
		try {
			boolean resetWrapper = false;
			// Have any passed values changes? If so, we need to reset start
			if (!StringUtil.equalsIgnoreCase(custName, getCustomerName())) {
				resetWrapper = true;
			} else if (single != isSingleCat()) {
				resetWrapper = true;
			} else {
				if (null == getStartDate() && null == startDate) {
					// Don't need to check anything
				} else if (null == getStartDate() && null != startDate) {
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
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	public Map<Object, List<ViewEntryWrapper>> getEntriesByDate() {
		final Map<Object, List<ViewEntryWrapper>> retVal_ = new HashMap<Object, List<ViewEntryWrapper>>();
		try {
			setViewName(ViewType.BY_DATE.getValue());
			final View dateView = getView();
			ViewNavigator nav = dateView.createViewNav();
			// Get first entry for the relevant start date, or go to last
			if (null != getStartDate()) {
				final DateRange dtRange = createDateRangeFromStartDate();
				final ViewEntryCollection vec = dateView.getAllEntriesByKey(dtRange, true);
				ViewEntry tmpEnt = vec.getFirstEntry();
				if (null == tmpEnt) {
					tmpEnt = nav.getLast();
					if (getStart() == 1) {
						setStart(2);
					}
				}
				nav = dateView.createViewNavFrom(tmpEnt);
			}
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
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	public Map<Object, List<ViewEntryWrapper>> getEntriesByCustomer() {
		// View is sorted DESCENDING, so use TreeMap to sort ASCENDING
		final Map<Object, List<ViewEntryWrapper>> retVal_ = new TreeMap<Object, List<ViewEntryWrapper>>();
		try {
			setViewName(ViewType.BY_CUST.getValue());
			final View custView = getView();
			final ViewNavigator nav = custView.createViewNavFromCategory(getCustomerName());
			ViewEntry ent = nav.getNth(getStart());
			if (null != getStartDate()) {
				final List<Object> keys = new ArrayList<Object>();
				keys.add(getCustomerName());
				keys.add(createDateRangeFromStartDate());
				final ViewEntry tmpEnt = custView.getFirstEntryByKey(keys, true);
				if (null != tmpEnt) {
					ent = nav.getLast();
				}
				nav.gotoEntry(tmpEnt);
				for (int i = 0; i <= getStart(); i++) {
					ent = nav.getNext();
				}
			}
			int addedRows = 0;
			while (null != ent && addedRows < getCount()) {
				addWrappedEntryToMap(retVal_, ent);
				addedRows = addedRows + 1;

				ent = nav.getNext();
			}
			if (null == ent) {
				setEndOfView(true);
			} else {
				setEndOfView(false);
			}

			calculateAvailablePages(nav);
		} catch (final Throwable t) {
			AppUtils.handleException(t);
		}
		return retVal_;
	}

	private void addWrappedEntryToMap(final Map<Object, List<ViewEntryWrapper>> retVal_, ViewEntry ent) {
		final KeyDateEntryWrapper entWrapper = new KeyDateEntryWrapper();
		entWrapper.setEventDate((Date) ent.getColumnValue("date"));
		entWrapper.setTitle(ent.getColumnValue("title", String.class));
		entWrapper.setDescription(ent.getColumnValue("description", String.class));
		entWrapper.setCustomer(ent.getColumnValue("customer", String.class));
		entWrapper.setContact(ent.getColumnValue("contact", String.class));
		List<ViewEntryWrapper> dateEntries = new ArrayList<ViewEntryWrapper>();
		final java.sql.Date mapDate = new java.sql.Date(entWrapper.getEventDate().getTime());
		if (retVal_.containsKey(mapDate)) {
			dateEntries = retVal_.get(mapDate);
		}
		dateEntries.add(entWrapper);
		retVal_.put(mapDate, dateEntries);
	}

	private DateRange createDateRangeFromStartDate() {
		final Calendar eCal = Calendar.getInstance();
		eCal.setTime(getStartDate());
		eCal.add(Calendar.MONTH, 1);
		final DateRange dtRange = AppUtils.getUserSession().createDateRange(getStartDate(), eCal.getTime());
		return dtRange;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
}
