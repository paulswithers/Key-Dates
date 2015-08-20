package uk.co.intec.keyDatesApp.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openntf.osgiworlds.model.ViewWrapper;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Component to provide a pager sizes selector (5, 10, 25, 50) and page
 *         navigation buttons (Prev, 1-5, Next).
 */
public class Pager extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
	private ViewWrapper wrappedView;
	private List<Sizes> availableSizes;
	private final Map<Sizes, Button> pagerSizeButtons = new HashMap<Sizes, Button>();
	private final Map<Object, Button> pagerPagesButtons = new HashMap<Object, Button>();
	private HorizontalLayout pagerPagesPanel = new HorizontalLayout();

	/**
	 * @author Paul Withers
	 *
	 *         Enum for page sizes
	 */
	public enum Sizes {
		FIVE(5), TEN(10), TWENTY_FIVE(25), FIFTY(50);

		private int value_;

		private Sizes(int size) {
			value_ = size;
		}

		public int getValue() {
			return value_;
		}
	}

	/**
	 * Constructor
	 */
	public Pager() {

	}

	/**
	 * Overloaded constructor, allowing passing of the ViewWrapper this pager
	 * maps to and page sizes to allow switching between.
	 *
	 * @param wrappedView
	 *            ViewWrapper through which the pager should navigate
	 * @param availSizes
	 *            List<Sizes> page sizes user can select, defaulting to 5, 10,
	 *            25 if null is passed
	 */
	public Pager(ViewWrapper wrappedView, List<Sizes> availSizes) {
		setWrappedView(wrappedView);
		if (null == availSizes) {
			availSizes = new ArrayList<Sizes>();
			availSizes.add(Sizes.FIVE);
			availSizes.add(Sizes.TEN);
			availSizes.add(Sizes.TWENTY_FIVE);
		}
		setAvailableSizes(availSizes);
	}

	/**
	 * Load the pager components
	 */
	public void loadContent() {
		final HorizontalLayout pagerSizePanel = loadPagerSizesButtons();
		// loadPagerPagesButtons(); No point, data has not yet been loaded
		addComponents(pagerSizePanel, getPagerPagesPanel());
		setSizeFull();
		setComponentAlignment(getPagerPagesPanel(), Alignment.TOP_RIGHT);
	}

	/**
	 * Load pager sizes buttons
	 *
	 * @return HorizontalLayout containing the pager sizes buttons
	 */
	public HorizontalLayout loadPagerSizesButtons() {
		final HorizontalLayout panel = new HorizontalLayout();
		for (final Sizes size : getAvailableSizes()) {
			final Button pageSizeLink = new Button();
			pageSizeLink.setCaption(Integer.toString(size.getValue()));
			pageSizeLink.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			pageSizeLink.addStyleName(ValoTheme.BUTTON_TINY);
			pageSizeLink.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				/*
				 * (non-Javadoc)
				 *
				 * @see
				 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
				 * Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setCount(size.getValue());
					getWrappedView().redrawContents();
					updatePagerSizeButtonStyles();
					loadPagerPagesButtons();
				}
			});
			getPagerSizeButtons().put(size, pageSizeLink);
			panel.addComponents(pageSizeLink);

			if (getAvailableSizes().indexOf(size) < (getAvailableSizes().size() - 1)) {
				final Label spacer = new Label("|");
				panel.addComponent(spacer);
			}
		}
		updatePagerSizeButtonStyles();
		return panel;
	}

	/**
	 * Update styles (adding or removing "selected" style) depending on whether
	 * or not each pager button is selected
	 */
	public void updatePagerSizeButtonStyles() {
		for (final Sizes size : getPagerSizeButtons().keySet()) {
			if (size.getValue() == getWrappedView().getCount()) {
				getPagerSizeButtons().get(size).addStyleName("v-button-borderless-selected");
			} else {
				getPagerSizeButtons().get(size).removeStyleName("v-button-borderless-selected");
			}
		}
	}

	/**
	 * Load pages buttons - Prev, page numbers, Next.<br/>
	 * <br/>
	 * If current page is 3 or less, the first page number displayed will be 1.
	 * Otherwise, if available page is less than current page + 2, the first
	 * page number will be available pages - 4. Otherwise, the first page number
	 * will be current page - 2.
	 */
	public void loadPagerPagesButtons() {
		getPagerPagesPanel().removeAllComponents();
		if (getWrappedView().getAvailablePages() > 1) {
			final Button pageLink = new Button();
			pageLink.setCaption("Prev");
			pageLink.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			pageLink.addStyleName(ValoTheme.BUTTON_TINY);
			pageLink.setIcon(FontAwesome.ANGLE_LEFT);
			pageLink.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				/*
				 * (non-Javadoc)
				 *
				 * @see
				 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
				 * Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setCurrentPage(getWrappedView().getCurrentPage() - 1);
					getWrappedView().setStart(getWrappedView().getStart() - getWrappedView().getCount());
					getWrappedView().redrawContents();
				}
			});
			final Label spacer = new Label("|");
			getPagerPagesButtons().put("Prev", pageLink);
			getPagerPagesPanel().addComponents(pageLink, spacer);
		}

		// Buttons for all pages
		int startPageNo = 1;
		if (getWrappedView().getCurrentPage() > 3) {
			if (getWrappedView().getAvailablePages() < (getWrappedView().getCurrentPage() + 2)) {
				startPageNo = getWrappedView().getAvailablePages() - 4;
				if (startPageNo < 1) {
					startPageNo = 1;
				}
			} else {
				startPageNo = getWrappedView().getCurrentPage() - 2;
			}
		}
		for (int i = startPageNo; i <= getWrappedView().getAvailablePages(); i++) {
			final Button pageLink = new Button();
			final int newPageNo = i;
			pageLink.setCaption(Integer.toString(i));
			pageLink.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			pageLink.addStyleName(ValoTheme.BUTTON_TINY);
			pageLink.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				/*
				 * (non-Javadoc)
				 *
				 * @see
				 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
				 * Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setStart(1 + ((newPageNo - 1) * getWrappedView().getCount()));
					getWrappedView().setCurrentPage(newPageNo);
					getWrappedView().redrawContents();
				}
			});
			getPagerPagesButtons().put(i, pageLink);
			getPagerPagesPanel().addComponent(pageLink);
			if (getWrappedView().getAvailablePages() > 1) {
				final Label spacer = new Label("|");
				getPagerPagesPanel().addComponent(spacer);
			}
		}

		if (getWrappedView().getAvailablePages() > 1) {
			final Button pageLink = new Button();
			pageLink.setCaption("Next");
			pageLink.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			pageLink.addStyleName(ValoTheme.BUTTON_TINY);
			pageLink.addStyleName(ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
			pageLink.setIcon(FontAwesome.ANGLE_RIGHT);
			pageLink.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				/*
				 * (non-Javadoc)
				 *
				 * @see
				 * com.vaadin.ui.Button.ClickListener#buttonClick(com.vaadin.ui.
				 * Button.ClickEvent)
				 */
				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setCurrentPage(getWrappedView().getCurrentPage() + 1);
					getWrappedView().setStart(getWrappedView().getStart() + getWrappedView().getCount());
					getWrappedView().redrawContents();
				}
			});
			getPagerPagesButtons().put("Next", pageLink);
			getPagerPagesPanel().addComponents(pageLink);
		}

		updatePagerPagesButtonStyles();
	}

	/**
	 * Update styles on pages buttons. Prev is disabled if on page 1. Next is
	 * disabled if at end of view. Current page is disabled, all others enabled.
	 */
	public void updatePagerPagesButtonStyles() {
		if (getWrappedView().getCurrentPage() == 1) {
			getPagerPagesButtons().get("Prev").setEnabled(false);
		} else {
			getPagerPagesButtons().get("Prev").setEnabled(true);
		}

		for (int i = 1; i <= getWrappedView().getAvailablePages(); i++) {
			if (getWrappedView().getCurrentPage() == i) {
				getPagerPagesButtons().get(i).setEnabled(false);
			} else {
				getPagerPagesButtons().get(i).setEnabled(true);
			}
		}

		if (getWrappedView().getCurrentPage() == getWrappedView().getAvailablePages()) {
			getPagerPagesButtons().get("Next").setEnabled(false);
		} else {
			getPagerPagesButtons().get("Next").setEnabled(true);
		}

	}

	/**
	 * Getter for wrappedView
	 *
	 * @return ViewWrapper wrapping the underlying (Domino) View
	 */
	public ViewWrapper getWrappedView() {
		return wrappedView;
	}

	/**
	 * Setter for wrappedView
	 *
	 * @param wrappedView
	 *            ViewWrapper wrapping the underlying (Domino) View
	 */
	public void setWrappedView(ViewWrapper wrappedView) {
		this.wrappedView = wrappedView;
	}

	/**
	 * Getter for availableSizes
	 *
	 * @return List of Sizes available for selection in this pager
	 */
	public List<Sizes> getAvailableSizes() {
		return availableSizes;
	}

	/**
	 * Stter for availableSizes
	 *
	 * @param availableSizes
	 *            List of Sizes available for selection in this pager
	 */
	public void setAvailableSizes(List<Sizes> availableSizes) {
		this.availableSizes = availableSizes;
	}

	/**
	 * Getter for pagerSizesButtons, to allow easy access from outside this
	 * class
	 *
	 * @return Map where the key is a Sizes enum and the value is the Button
	 */
	public Map<Sizes, Button> getPagerSizeButtons() {
		return pagerSizeButtons;
	}

	/**
	 * Getter for pagerPagesButtons, to allow easy access from outside this
	 * class
	 *
	 * @return Map where they key is a label and the value is the Button
	 */
	public Map<Object, Button> getPagerPagesButtons() {
		return pagerPagesButtons;
	}

	/**
	 * Getter for pagerPagesPanel
	 *
	 * @return HorizontalLayout containing the pagerPagesButtons
	 */
	public HorizontalLayout getPagerPagesPanel() {
		return pagerPagesPanel;
	}

	/**
	 * Setter for pagerPagesPanel
	 *
	 * @param pagerPagesPanel
	 *            HorizontalLayout containing the pagerPagesButtons
	 */
	public void setPagerPagesPanel(HorizontalLayout pagerPagesPanel) {
		this.pagerPagesPanel = pagerPagesPanel;
	}

}
