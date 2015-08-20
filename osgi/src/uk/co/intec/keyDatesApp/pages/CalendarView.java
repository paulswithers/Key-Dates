package uk.co.intec.keyDatesApp.pages;

import java.util.Date;
import java.util.GregorianCalendar;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

import uk.co.intec.keyDatesApp.components.DateSelector;
import uk.co.intec.keyDatesApp.model.KeyDateCalendarEventClickHandler;
import uk.co.intec.keyDatesApp.model.KeyDateCalendarView_CalendarEventProvider;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Calendar page
 *
 */
public class CalendarView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Calendar";
	public static final String VIEW_LABEL = "Calendar";
	private boolean loaded;
	private boolean showWeekends;
	private Calendar calLayout = new Calendar();
	private Date startDate;
	private Date endDate;

	/**
	 * Constructor, sets CssLayout to take full area
	 */
	public CalendarView() {

		setSizeFull();

	}

	/**
	 * Shows an error message on the screen
	 *
	 * @param msg
	 *            String error to display
	 */
	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * com.vaadin.navigator.View#enter(com.vaadin.navigator.ViewChangeListener.
	 * ViewChangeEvent)
	 */
	@Override
	public void enter(ViewChangeEvent event) {
		if (!isLoaded()) {

			loadContent();

			setLoaded(true);
		}
	}

	/**
	 * Loads the main content for the page. Only called on first entry to the
	 * page, because calling method sets <i>isLoaded</i> to true after
	 * successfully completing.
	 */
	public void loadContent() {
		try {
			final VerticalLayout body = new VerticalLayout();
			getCalLayout().setSizeFull();

			getCalLayout().setHandler(new KeyDateCalendarEventClickHandler(KeyDateView.VIEW_NAME));

			final Date today = new Date();
			final GregorianCalendar cal = new GregorianCalendar(getLocale());
			cal.setTime(today);
			cal.set(GregorianCalendar.DAY_OF_MONTH, 1);
			setStartDate(cal.getTime());
			cal.add(GregorianCalendar.MONTH, 1);
			cal.add(GregorianCalendar.DATE, -1);
			setEndDate(cal.getTime());

			getCalLayout().setResponsive(true);
			getCalLayout().setStartDate(getStartDate());
			getCalLayout().setEndDate(getEndDate());

			getCalLayout().setFirstVisibleHourOfDay(8);
			getCalLayout().setLastVisibleHourOfDay(20);
			getCalLayout().setReadOnly(true);
			getCalLayout().setFirstVisibleDayOfWeek(1);

			getCalLayout().setEventProvider(new KeyDateCalendarView_CalendarEventProvider());

			setShowWeekends(true);
			final DateSelector dateButtons = new DateSelector(getCalLayout());
			body.addComponents(dateButtons, getCalLayout());

			addComponent(body);
			setSizeFull();
		} catch (final Exception e) {

		}
	}

	/**
	 * Getter for loaded
	 *
	 * @return boolean whether the content has already been loaded
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * Setter for loaded
	 *
	 * @param loaded
	 *            boolean whether or not the content has already been loaded
	 */
	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	/**
	 * Getter for startDate
	 *
	 * @return Date start date for the calendar view
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Setter for startDate
	 *
	 * @param startDate
	 *            Date start date for the calendar view
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * Getter for endDate
	 *
	 * @return Date end date for the calendar view
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Setter for endDate
	 *
	 * @param endDate
	 *            Date end date for the calendar view
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * Getter for showWeekends
	 *
	 * @return boolean whether or not the calendar should show weekends
	 */
	public boolean isShowWeekends() {
		return showWeekends;
	}

	/**
	 * Setter for showWeekends
	 *
	 * @param showWeekends
	 *            boolean whether or not the calendar should show weekends
	 */
	public void setShowWeekends(boolean showWeekends) {
		if (showWeekends) {
			getCalLayout().setLastVisibleDayOfWeek(7);
		} else {
			getCalLayout().setLastVisibleDayOfWeek(5);
		}
		this.showWeekends = showWeekends;
	}

	/**
	 * Getter for calLayout
	 *
	 * @return Calendar Vaadin calendar component
	 */
	public Calendar getCalLayout() {
		return calLayout;
	}

	/**
	 * Setter for calLayout
	 *
	 * @param calLayout
	 *            Calendar Vaadin calendar component
	 */
	public void setCalLayout(Calendar calLayout) {
		this.calLayout = calLayout;
	}

}
