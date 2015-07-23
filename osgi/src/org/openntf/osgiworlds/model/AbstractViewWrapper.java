package org.openntf.osgiworlds.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewNavigator;

public abstract class AbstractViewWrapper implements ViewWrapper, Serializable {
	private static final long serialVersionUID = 1L;
	private static final String ERROR_DONT_USE_DIRECTLY = "This method should not be accessed directly from the abstract class AbstractViewWrapper";
	private String serverName;
	private String databaseName;
	private String viewName;
	private int start = 1;
	private int count = 10;
	private String searchString;
	private List<Object> category;
	private boolean exactMatch;
	private boolean singleCat;
	private boolean endOfView;
	private int availablePages;
	private int currentPage;
	private com.vaadin.navigator.View parentVaadinView;

	public AbstractViewWrapper() {
		setStart(1);
		setCurrentPage(1);
		setCount(5);
	}

	public AbstractViewWrapper(com.vaadin.navigator.View parentView) {
		setStart(1);
		setCurrentPage(1);
		setCount(5);
		setParentVaadinView(parentView);
	}

	@Override
	public String getServerName() {
		return serverName;
	}

	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Override
	public String getDatabaseName() {
		return databaseName;
	}

	@Override
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	@Override
	public String getViewName() {
		return viewName;
	}

	@Override
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Override
	public View getView() {
		return getParentDatabase().getView(getViewName());
	}

	@Override
	public List<ViewEntryWrapper> getEntries() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	@Override
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	@Override
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap(Object key) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	@Override
	public int getStart() {
		return start;
	}

	@Override
	public void setStart(int start) {
		this.start = start;
	}

	@Override
	public String getSearchString() {
		return searchString;
	}

	@Override
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	@Override
	public List<Object> getCategory() {
		return category;
	}

	@Override
	public void setCategory(List<Object> category) {
		this.category = category;
	}

	@Override
	public void setCategory(Object singleCat) {
		final List<Object> categoriesInt = new ArrayList<Object>();
		categoriesInt.add(singleCat);
		setCategory(categoriesInt);
	}

	@Override
	public boolean isExactMatch() {
		return exactMatch;
	}

	@Override
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	@Override
	public boolean isSingleCat() {
		return singleCat;
	}

	@Override
	public void setSingleCat(boolean singleCat) {
		this.singleCat = singleCat;
	}

	@Override
	public boolean isEndOfView() {
		return endOfView;
	}

	@Override
	public void setEndOfView(boolean endOfView) {
		this.endOfView = endOfView;
	}

	@Override
	public Database getParentDatabase() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	@Override
	public int getCount() {
		return count;
	}

	@Override
	public void setCount(int rows) {
		count = rows;
	}

	@Override
	public com.vaadin.navigator.View getParentVaadinView() {
		return parentVaadinView;
	}

	@Override
	public void setParentVaadinView(com.vaadin.navigator.View parentView) {
		parentVaadinView = parentView;
	}

	@Override
	public void redrawContents() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	@Override
	public int getAvailablePages() {
		return availablePages;
	}

	@Override
	public void setAvailablePages(int pages) {
		availablePages = pages;
	}

	@Override
	public void calculateAvailablePages(ViewNavigator nav) {
		if (getStart() < 2) { // We've reset (and possibly with only one page)
			if (isEndOfView()) {
				setAvailablePages(1);
			} else {
				int pageCount = 1;
				final int maxPages = 5;
				while (pageCount < maxPages) {
					pageCount = pageCount + 1;
					final int entriesOnPage = nav.skip(getCount());
					if (getCount() > entriesOnPage) {
						break;
					}
				}
				setAvailablePages(pageCount);
			}
		}
	}

	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	@Override
	public void setCurrentPage(int page) {
		currentPage = page;
	}
}
