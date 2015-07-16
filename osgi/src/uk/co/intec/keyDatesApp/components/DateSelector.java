package uk.co.intec.keyDatesApp.components;

import java.text.DateFormatSymbols;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import uk.co.intec.keyDatesApp.utils.AppUtils;

import com.vaadin.server.FontAwesome;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Calendar;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.DateClickEvent;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClick;
import com.vaadin.ui.components.calendar.CalendarComponentEvents.WeekClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicDateClickHandler;
import com.vaadin.ui.components.calendar.handler.BasicWeekClickHandler;
import com.vaadin.ui.themes.ValoTheme;

public class DateSelector extends VerticalLayout {
	private static final long serialVersionUID = 1L;
	private Calendar calLayout;
	private GregorianCalendar calendar;
	private Date currentMonthsFirstDate;
	private final Label captionLabel = new Label("");
	private Button monthButton;
	private Button weekButton;
	private Button nextButton;
	private Button prevButton;
	private Mode viewMode = Mode.MONTH;

	private enum Mode {
		MONTH, WEEK, DAY;
	}

	public Calendar getCalLayout() {
		return calLayout;
	}

	public void setCalLayout(Calendar calLayout) {
		this.calLayout = calLayout;
	}

	public DateSelector(Calendar cal) {
		try {
			setLocale(Locale.getDefault());
			setCalLayout(cal);
			calendar = new GregorianCalendar(getLocale());
			calendar.setTime(getCalLayout().getStartDate());
			currentMonthsFirstDate = calendar.getTime();

			addCalendarEventListeners();
			initNavigationButtons();
			updateCaptionLabel();

			final HorizontalLayout hl = new HorizontalLayout();
			hl.setWidth("100%");
			hl.setSpacing(true);
			hl.setMargin(new MarginInfo(false, false, true, false));
			hl.addComponent(prevButton);
			hl.setComponentAlignment(prevButton, Alignment.MIDDLE_LEFT);

			final HorizontalLayout monthButtonPanel = new HorizontalLayout();
			monthButtonPanel.addComponent(monthButton);
			hl.addComponent(monthButtonPanel);
			hl.setComponentAlignment(monthButtonPanel, Alignment.MIDDLE_CENTER);

			captionLabel.addStyleName(ValoTheme.LABEL_COLORED);
			captionLabel.setWidth(null);
			hl.addComponent(captionLabel);
			hl.setComponentAlignment(captionLabel, Alignment.MIDDLE_CENTER);

			final HorizontalLayout weekButtonPanel = new HorizontalLayout();
			weekButtonPanel.addComponent(weekButton);
			hl.addComponent(weekButtonPanel);
			hl.setComponentAlignment(weekButtonPanel, Alignment.MIDDLE_CENTER);

			hl.addComponent(nextButton);
			hl.setComponentAlignment(nextButton, Alignment.MIDDLE_RIGHT);

			monthButton.setVisible(viewMode == Mode.WEEK);
			weekButton.setVisible(viewMode == Mode.DAY);
			addComponent(hl);
		} catch (final Throwable t) {
			AppUtils.handleException(t);
		}
	}

	@SuppressWarnings("serial")
	private void addCalendarEventListeners() {
		// Register week clicks by changing the schedules start and end dates.
		getCalLayout().setHandler(new BasicWeekClickHandler() {

			@Override
			public void weekClick(WeekClick event) {
				// let BasicWeekClickHandler handle calendar dates, and update
				// only the other parts of UI here
				super.weekClick(event);
				updateCaptionLabel();
				switchToWeekView();
				calendar.setTime(getCalLayout().getStartDate());
			}
		});

		getCalLayout().setHandler(new BasicDateClickHandler() {

			@Override
			public void dateClick(DateClickEvent event) {
				// let BasicDateClickHandler handle calendar dates, and update
				// only the other parts of UI here
				super.dateClick(event);
				switchToDayView();
				calendar.setTime(getCalLayout().getStartDate());
			}
		});
	}

	private void initNavigationButtons() {
		monthButton = new Button("Month view");
		monthButton.addStyleName(ValoTheme.BUTTON_QUIET);
		monthButton.addStyleName(ValoTheme.BUTTON_SMALL);
		monthButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				switchToMonthView();
			}
		});

		weekButton = new Button("Week view");
		weekButton.addStyleName(ValoTheme.BUTTON_QUIET);
		weekButton.addStyleName(ValoTheme.BUTTON_SMALL);
		weekButton.addClickListener(new ClickListener() {

			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				// simulate week click
				final WeekClickHandler handler = (WeekClickHandler) getCalLayout().getHandler(WeekClick.EVENT_ID);
				handler.weekClick(new WeekClick(getCalLayout(), calendar.get(GregorianCalendar.WEEK_OF_YEAR), calendar
						.get(GregorianCalendar.YEAR)));
			}
		});

		prevButton = new Button("Prev");
		prevButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		prevButton.setIcon(FontAwesome.ANGLE_LEFT);
		prevButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				handlePreviousButtonClick();
			}
		});

		nextButton = new Button("Next");
		nextButton.addStyleName(ValoTheme.BUTTON_FRIENDLY);
		nextButton.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		nextButton.setIcon(FontAwesome.ANGLE_RIGHT);
		nextButton.addClickListener(new Button.ClickListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void buttonClick(ClickEvent event) {
				handleNextButtonClick();
			}
		});
	}

	private void handleNextButtonClick() {
		switch (viewMode) {
		case MONTH:
			nextMonth();
			break;
		case WEEK:
			nextWeek();
			break;
		case DAY:
			nextDay();
			break;
		}
	}

	private void handlePreviousButtonClick() {
		switch (viewMode) {
		case MONTH:
			previousMonth();
			break;
		case WEEK:
			previousWeek();
			break;
		case DAY:
			previousDay();
			break;
		}
	}

	private void nextMonth() {
		rollMonth(1);
	}

	private void previousMonth() {
		rollMonth(-1);
	}

	private void nextWeek() {
		rollWeek(1);
	}

	private void previousWeek() {
		rollWeek(-1);
	}

	private void nextDay() {
		rollDate(1);
	}

	private void previousDay() {
		rollDate(-1);
	}

	private void rollMonth(int direction) {
		calendar.setTime(currentMonthsFirstDate);
		calendar.add(GregorianCalendar.MONTH, direction);
		resetTime(false);
		currentMonthsFirstDate = calendar.getTime();
		getCalLayout().setStartDate(currentMonthsFirstDate);

		updateCaptionLabel();

		calendar.add(GregorianCalendar.MONTH, 1);
		calendar.add(GregorianCalendar.DATE, -1);
		resetCalendarTime(true);
	}

	private void rollWeek(int direction) {
		calendar.add(GregorianCalendar.WEEK_OF_YEAR, direction);
		calendar.set(GregorianCalendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		resetCalendarTime(false);
		resetTime(true);
		calendar.add(GregorianCalendar.DATE, 6);
		getCalLayout().setEndDate(calendar.getTime());
	}

	private void rollDate(int direction) {
		calendar.add(GregorianCalendar.DATE, direction);
		resetCalendarTime(false);
		resetCalendarTime(true);
	}

	private void updateCaptionLabel() {
		final DateFormatSymbols s = new DateFormatSymbols(getLocale());
		final String month = s.getShortMonths()[calendar.get(GregorianCalendar.MONTH)];
		captionLabel.setValue(month + " " + calendar.get(GregorianCalendar.YEAR));
	}

	/*
	 * Switch the view to week view.
	 */
	public void switchToWeekView() {
		viewMode = Mode.WEEK;
		weekButton.setVisible(false);
		monthButton.setVisible(true);
	}

	/*
	 * Switch the Calendar component's start and end date range to the target
	 * month only. (sample range: 01.01.2010 00:00.000 - 31.01.2010 23:59.999)
	 */
	public void switchToMonthView() {
		viewMode = Mode.MONTH;
		monthButton.setVisible(false);
		weekButton.setVisible(false);

		calendar.setTime(currentMonthsFirstDate);
		getCalLayout().setStartDate(currentMonthsFirstDate);

		updateCaptionLabel();

		calendar.add(GregorianCalendar.MONTH, 1);
		calendar.add(GregorianCalendar.DATE, -1);
		resetCalendarTime(true);
	}

	/*
	 * Switch to day view (week view with a single day visible).
	 */
	public void switchToDayView() {
		viewMode = Mode.DAY;
		monthButton.setVisible(true);
		weekButton.setVisible(true);
	}

	private void resetCalendarTime(boolean resetEndTime) {
		resetTime(resetEndTime);
		if (resetEndTime) {
			getCalLayout().setEndDate(calendar.getTime());
		} else {
			getCalLayout().setStartDate(calendar.getTime());
			updateCaptionLabel();
		}
	}

	/*
	 * Resets the calendar time (hour, minute second and millisecond) either to
	 * zero or maximum value.
	 */
	private void resetTime(boolean max) {
		if (max) {
			calendar.set(GregorianCalendar.HOUR_OF_DAY, calendar.getMaximum(GregorianCalendar.HOUR_OF_DAY));
			calendar.set(GregorianCalendar.MINUTE, calendar.getMaximum(GregorianCalendar.MINUTE));
			calendar.set(GregorianCalendar.SECOND, calendar.getMaximum(GregorianCalendar.SECOND));
			calendar.set(GregorianCalendar.MILLISECOND, calendar.getMaximum(GregorianCalendar.MILLISECOND));
		} else {
			calendar.set(GregorianCalendar.HOUR_OF_DAY, 0);
			calendar.set(GregorianCalendar.MINUTE, 0);
			calendar.set(GregorianCalendar.SECOND, 0);
			calendar.set(GregorianCalendar.MILLISECOND, 0);
		}
	}

}
