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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntry;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Abstract class implementing the ViewWrapper interface. Some methods,
 *         like getEntries() and getEntriesAsMap() do not have default
 *         implementations here, because there is no default implementation that
 *         can be applied.
 *
 */
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

	/**
	 * Constructor, setting to start at the first ViewEntry and display five
	 * ViewEntries on the page.
	 */
	public AbstractViewWrapper() {
		setStart(1);
		setCurrentPage(1);
		setCount(5);
	}

	/**
	 * Overloaded constructor, in addition setting the com.vaadin.navigator.View
	 * this ViewWrapper is used on.
	 *
	 * @param parentView
	 */
	public AbstractViewWrapper(com.vaadin.navigator.View parentView) {
		setStart(1);
		setCurrentPage(1);
		setCount(5);
		setParentVaadinView(parentView);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getServerName()
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#setServerName(java.lang.String)
	 */
	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getDatabaseName()
	 */
	@Override
	public String getDatabaseName() {
		return databaseName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setDatabaseName(java.lang.
	 * String)
	 */
	@Override
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getViewName()
	 */
	@Override
	public String getViewName() {
		return viewName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#setViewName(java.lang.String)
	 */
	@Override
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getView()
	 */
	@Override
	public View getView() {
		return getParentDatabase().getView(getViewName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getEntries()
	 */
	@Override
	public List<ViewEntryWrapper> getEntries() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getEntriesAsMap()
	 */
	@Override
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getEntriesAsMap(java.lang.
	 * Object)
	 */
	@Override
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap(Object key) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getStart()
	 */
	@Override
	public int getStart() {
		return start;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setStart(int)
	 */
	@Override
	public void setStart(int start) {
		this.start = start;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getSearchString()
	 */
	@Override
	public String getSearchString() {
		return searchString;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setSearchString(java.lang.
	 * String)
	 */
	@Override
	public void setSearchString(String searchString) {
		this.searchString = searchString;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getCategory()
	 */
	@Override
	public List<Object> getCategory() {
		return category;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setCategory(java.util.List)
	 */
	@Override
	public void setCategory(List<Object> category) {
		this.category = category;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#setCategory(java.lang.Object)
	 */
	@Override
	public void setCategory(Object singleCat) {
		final List<Object> categoriesInt = new ArrayList<Object>();
		categoriesInt.add(singleCat);
		setCategory(categoriesInt);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#isExactMatch()
	 */
	@Override
	public boolean isExactMatch() {
		return exactMatch;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setExactMatch(boolean)
	 */
	@Override
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#isSingleCat()
	 */
	@Override
	public boolean isSingleCat() {
		return singleCat;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setSingleCat(boolean)
	 */
	@Override
	public void setSingleCat(boolean singleCat) {
		this.singleCat = singleCat;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#isEndOfView()
	 */
	@Override
	public boolean isEndOfView() {
		return endOfView;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setEndOfView(boolean)
	 */
	@Override
	public void setEndOfView(boolean endOfView) {
		this.endOfView = endOfView;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getParentDatabase()
	 */
	@Override
	public Database getParentDatabase() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getCount()
	 */
	@Override
	public int getCount() {
		return count;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setCount(int)
	 */
	@Override
	public void setCount(int rows) {
		count = rows;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getParentVaadinView()
	 */
	@Override
	public com.vaadin.navigator.View getParentVaadinView() {
		return parentVaadinView;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#setParentVaadinView(com.vaadin.
	 * navigator.View)
	 */
	@Override
	public void setParentVaadinView(com.vaadin.navigator.View parentView) {
		parentVaadinView = parentView;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#redrawContents()
	 */
	@Override
	public void redrawContents() {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getAvailablePages()
	 */
	@Override
	public int getAvailablePages() {
		return availablePages;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setAvailablePages(int)
	 */
	@Override
	public void setAvailablePages(int pages) {
		availablePages = pages;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#calculateAvailablePages(java.
	 * lang.Object)
	 */
	@Override
	public void calculateAvailablePages(Object coll) {
		if (isEndOfView()) {
			setAvailablePages(getCurrentPage());
		} else {
			int pageCount;
			int maxPages;
			if (getCurrentPage() > 3) {
				maxPages = 2; // max range = e.g. 2 - 6
			} else {
				maxPages = 5 - getCurrentPage(); // Max range = 1-5
			}
			if (coll instanceof ViewEntryCollection) {
				pageCount = getRemainingPages((ViewEntryCollection) coll, maxPages);
			} else {
				pageCount = getRemainingPages((ViewNavigator) coll, maxPages);
			}
			if (getCurrentPage() > 3) {
				pageCount = pageCount + getCurrentPage();
			} else {
				if (pageCount < 5) {
					pageCount = pageCount + getCurrentPage();
				}
			}
			setAvailablePages(pageCount);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#getRemainingPages(org.openntf.
	 * domino.ViewEntryCollection, int)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public int getRemainingPages(ViewEntryCollection ec, int maxPages) {
		int retVal_ = 1;
		while (retVal_ <= maxPages) {
			final ViewEntry ent = ec.getNthEntry(getCount() * retVal_);
			if (null == ent) {
				break;
			}
			retVal_ = retVal_ + 1;
		}
		return retVal_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.model.ViewWrapper#getRemainingPages(org.openntf.
	 * domino.ViewNavigator, int)
	 */
	@Override
	public int getRemainingPages(ViewNavigator nav, int maxPages) {
		int retVal_ = 1;
		while (retVal_ <= maxPages) {
			// We know page 1 is fine, so just start from page 2
			final int entriesOnPage = nav.skip(getCount());
			if (getCount() > entriesOnPage) {
				break;
			}
			retVal_ = retVal_ + 1;
		}
		return retVal_;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#getCurrentPage()
	 */
	@Override
	public int getCurrentPage() {
		return currentPage;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.model.ViewWrapper#setCurrentPage(int)
	 */
	@Override
	public void setCurrentPage(int page) {
		currentPage = page;
	}
}
