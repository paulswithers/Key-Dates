package uk.co.intec.keyDatesApp.pages;

import java.util.Date;
import java.util.GregorianCalendar;

import uk.co.intec.keyDatesApp.components.DateSelector;
import uk.co.intec.keyDatesApp.model.KeyDateCalendarView_CalendarEventProvider;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;

public class CalendarView extends CssLayout implements View {
	private static final long serialVersionUID = 1L;
	public static final String VIEW_NAME = "Calendar";
	public static final String VIEW_LABEL = "Calendar";
	private boolean loaded;
	private boolean showWeekends;
	private Calendar calLayout = new Calendar();
	private Date startDate;
	private Date endDate;

	public CalendarView() {

		setSizeFull();

	}

	public void showError(String msg) {
		Notification.show(msg, Type.ERROR_MESSAGE);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		if (!isLoaded()) {

			loadContent();

			setLoaded(true);
		}
	}

	public void loadContent() {
		try {
			final VerticalLayout body = new VerticalLayout();
			getCalLayout().setSizeFull();

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

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
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

	public boolean isShowWeekends() {
		return showWeekends;
	}

	public void setShowWeekends(boolean showWeekends) {
		if (showWeekends) {
			getCalLayout().setLastVisibleDayOfWeek(7);
		} else {
			getCalLayout().setLastVisibleDayOfWeek(5);
		}
		this.showWeekends = showWeekends;
	}

	public Calendar getCalLayout() {
		return calLayout;
	}

	public void setCalLayout(Calendar calLayout) {
		this.calLayout = calLayout;
	}

}
