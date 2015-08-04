package uk.co.intec.keyDatesApp.components;

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

public class Pager extends HorizontalLayout {
	private static final long serialVersionUID = 1L;
	private ViewWrapper wrappedView;
	private List<Sizes> availableSizes;
	private final Map<Sizes, Button> pagerSizeButtons = new HashMap<Sizes, Button>();
	private final Map<Object, Button> pagerPagesButtons = new HashMap<Object, Button>();
	private HorizontalLayout pagerPagesPanel = new HorizontalLayout();

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

	public Pager() {

	}

	public Pager(ViewWrapper wrappedView, List<Sizes> availSizes) {
		setWrappedView(wrappedView);
		setAvailableSizes(availSizes);
	}

	public void loadContent() {
		final HorizontalLayout pagerSizePanel = loadPagerSizesButtons();
		// loadPagerPagesButtons(); No point, data has not yet been loaded
		addComponents(pagerSizePanel, getPagerPagesPanel());
		setSizeFull();
		setComponentAlignment(getPagerPagesPanel(), Alignment.TOP_RIGHT);
	}

	public HorizontalLayout loadPagerSizesButtons() {
		final HorizontalLayout panel = new HorizontalLayout();
		for (final Sizes size : getAvailableSizes()) {
			final Button pageSizeLink = new Button();
			pageSizeLink.setCaption(Integer.toString(size.getValue()));
			pageSizeLink.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			pageSizeLink.addStyleName(ValoTheme.BUTTON_TINY);
			pageSizeLink.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

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

	public void updatePagerSizeButtonStyles() {
		for (final Sizes size : getPagerSizeButtons().keySet()) {
			if (size.getValue() == getWrappedView().getCount()) {
				getPagerSizeButtons().get(size).addStyleName("v-button-borderless-selected");
			} else {
				getPagerSizeButtons().get(size).removeStyleName("v-button-borderless-selected");
			}
		}
	}

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

				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setCurrentPage(getWrappedView().getCurrentPage() - 1);
					getWrappedView().setStart(getWrappedView().getStart() - getWrappedView().getCount());
					getWrappedView().redrawContents();
					updatePagerPagesButtonStyles();
				}
			});
			final Label spacer = new Label("|");
			getPagerPagesButtons().put("Prev", pageLink);
			getPagerPagesPanel().addComponents(pageLink, spacer);
		}

		// Buttons for all pages
		for (int i = 1; i <= getWrappedView().getAvailablePages(); i++) {
			final Button pageLink = new Button();
			final int newPageNo = i;
			pageLink.setCaption(Integer.toString(i));
			pageLink.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
			pageLink.addStyleName(ValoTheme.BUTTON_TINY);
			pageLink.addClickListener(new ClickListener() {
				private static final long serialVersionUID = 1L;

				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setStart(1 + ((newPageNo - 1) * getWrappedView().getCount()));
					getWrappedView().setCurrentPage(newPageNo);
					getWrappedView().redrawContents();
					updatePagerPagesButtonStyles();
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

				@Override
				public void buttonClick(ClickEvent event) {
					getWrappedView().setCurrentPage(getWrappedView().getCurrentPage() + 1);
					getWrappedView().setStart(getWrappedView().getStart() + getWrappedView().getCount());
					getWrappedView().redrawContents();
					updatePagerPagesButtonStyles();
					// TODO: Update list of available pages
				}
			});
			getPagerPagesButtons().put("Next", pageLink);
			getPagerPagesPanel().addComponents(pageLink);
		}

		updatePagerPagesButtonStyles();
	}

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

	public ViewWrapper getWrappedView() {
		return wrappedView;
	}

	public void setWrappedView(ViewWrapper wrappedView) {
		this.wrappedView = wrappedView;
	}

	public List<Sizes> getAvailableSizes() {
		return availableSizes;
	}

	public void setAvailableSizes(List<Sizes> availableSizes) {
		this.availableSizes = availableSizes;
	}

	public Map<Sizes, Button> getPagerSizeButtons() {
		return pagerSizeButtons;
	}

	public HorizontalLayout getPagerPagesPanel() {
		return pagerPagesPanel;
	}

	public void setPagerPagesPanel(HorizontalLayout pagerPagesPanel) {
		this.pagerPagesPanel = pagerPagesPanel;
	}

	public Map<Object, Button> getPagerPagesButtons() {
		return pagerPagesButtons;
	}

}
