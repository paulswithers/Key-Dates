package org.openntf.osgiworlds.model;

import java.util.List;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         A wrapper for a Domino View. Use serverName, databaseName and
 *         viewName to map to the relevant Domino view(s) - there's no specific
 *         reason for needing multiple ViewWrappers for different views. You can
 *         just reset whichever variables you need to and handle what data gets
 *         output, based on the current viewName.<br/>
 *         <br/>
 *         By using searchString and category, this wrapper may only provide
 *         access to a sub-set of ViewEntries. If those are both blank, the
 *         wrapper will provide access to all ViewEntries int he underlying
 *         View. Otherwise you will not be able to navigate outside the sub-set
 *         of ViewEntries matching the searchString and category without
 *         modifying the criteria for this wrapper.<br/>
 *         <br/>
 *         Start and count are used to manage which entries are displayed back
 *         to the user on the current request.<br/>
 *         <br/>
 *         currentPage and availablePages are used for updating a pager.<br/>
 *         <br/>
 *         getEntries() and getEntriesAsMap() methods are used to get relevant
 *         ViewEntryWrappers to display back to the user. The wrappers need to
 *         contain all the column values or computed values you want to display
 *         to the user.
 */
public interface ViewWrapper {

	/**
	 * Gets the server name for the Domino Database in which the Domino View
	 * resides
	 *
	 * @return String server name
	 */
	public String getServerName();

	/**
	 * Sets the server name for the Domino Database in which the Domino View
	 * resides
	 *
	 * @param serverName
	 *            String server name
	 */
	public void setServerName(String serverName);

	/**
	 * Gets the Domino database filepath in which the View resides
	 *
	 * @return String database filepath
	 */
	public String getDatabaseName();

	/**
	 * Sets the Domino database filepath in which the View resides
	 *
	 * @param databaseName
	 *            String database filepath
	 */
	public void setDatabaseName(String databaseName);

	/**
	 * Gets the name of the Domino View this object wraps
	 *
	 * @return String view name
	 */
	public String getViewName();

	/**
	 * Sets the name of the Domino View this object wraps
	 *
	 * @param viewName
	 *            String view name
	 */
	public void setViewName(String viewName);

	/**
	 * Gets the Domino View this object wraps
	 *
	 * @return View wrapped
	 */
	public View getView();

	/**
	 * Gets a List of wrapped ViewEntry objects
	 *
	 * @return List of wrapped entries
	 */
	public List<ViewEntryWrapper> getEntries();

	/**
	 * Gets a Map of wrapped ViewEntry objects, effectively categorising the
	 * view
	 *
	 * @return Map of wrapped entries
	 */
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap();

	/**
	 * Gets a Map of wrapped ViewEntry objects based on a passed key (wrapper
	 * version of getAllEntriesByKey)
	 *
	 * @param key
	 *            Object sub-category to retrieve entries for
	 * @return Map of wrapped entries
	 */
	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap(Object key);

	/**
	 * Gets the start index within the entries
	 *
	 * @return int nth entry from which to start
	 */
	public int getStart();

	/**
	 * Sets the start index within the entries
	 *
	 * @param start
	 *            int nth entry from which to start
	 */
	public void setStart(int start);

	/**
	 * Gets the number of entries to retrieve
	 *
	 * @return int count of entries
	 */
	public int getCount();

	/**
	 * Sets the number of entries to retrieve
	 *
	 * @param rows
	 *            int entries to retrieve
	 */
	public void setCount(int rows);

	/**
	 * Gets the search string in @Formula format to perform a Full-Text search
	 *
	 * @return String search string
	 */
	public String getSearchString();

	/**
	 * Sets the search string in @Formula format to perform a Full-Text search
	 *
	 * @param searchString
	 *            String search string
	 */
	public void setSearchString(String searchString);

	/**
	 * Gets the List of Objects from which to start or retrieve a single
	 * category of entries
	 *
	 * @return List of values in relevant sorted columns
	 */
	public List<Object> getCategory();

	/**
	 * Sets the List of Objects from which to start or retrieve a single
	 * category of entries
	 *
	 * @param categories
	 *            List of values in relevant sorted columns
	 */
	public void setCategory(List<Object> categories);

	/**
	 * Sets the category from which to start or retrieve a single category of
	 * entries
	 *
	 * @param singleCat
	 *            Object matching value in first sorted column
	 */
	public void setCategory(Object singleCat);

	/**
	 * Gets whether or not entries should be retrieved only for an exact match
	 * on categories
	 *
	 * @return boolean whether to look for an exact match
	 */
	public boolean isExactMatch();

	/**
	 * Sets whether or not entries should be retrieved only for an exact match
	 * on categories
	 *
	 * @param exactMatch
	 *            whether to look for an exact match
	 */
	public void setExactMatch(boolean exactMatch);

	/**
	 * Gets whether to only show a single category or start from this category
	 * onwards
	 *
	 * @return boolean whether to show a single category
	 */
	public boolean isSingleCat();

	/**
	 * Sets whether to only show a single category or start from this category
	 * onwards
	 *
	 * @param singleCat
	 *            whether to show a single category
	 */
	public void setSingleCat(boolean singleCat);

	/**
	 * Gets the Database the wrapped View resised in
	 *
	 * @return
	 */
	public Database getParentDatabase();

	/**
	 * Gets whether or not this collection reaches the end of the View or the
	 * ViewEntries matching search or categorisation criteria
	 *
	 * @return boolean end of view
	 */
	public boolean isEndOfView();

	/**
	 * Sets whether or not this collection reaches the end of the View or the
	 * ViewEntries matching search or categorisation criteria
	 *
	 * @param endOfView
	 *            boolean end of view
	 */
	public void setEndOfView(boolean endOfView);

	/**
	 * Gets the Vaadin View ("page") this ViewWrapper is used on
	 *
	 * @return com.vaadin.navigator.View page used on
	 */
	public com.vaadin.navigator.View getParentVaadinView();

	/**
	 * Sets the Vaadin View ("page") this ViewWrapper is used on
	 *
	 * @param parentView
	 *            com.vaadin.navigator.View page used on
	 */
	public void setParentVaadinView(com.vaadin.navigator.View parentView);

	/**
	 * Main method used to write the new set of entries to the page. If a pager
	 * is being used, this method should also update the pager buttons
	 * (Previous, page numbers, Next)
	 */
	public void redrawContents();

	/**
	 * Gets the number of pages to be displayed as page numbers in the pager
	 *
	 * @return int number of pages available from the wrapped View
	 */
	public int getAvailablePages();

	/**
	 * Sets the number of pages to be displayed as page numbers in the pager
	 *
	 * @param pages
	 *            int number of pages available from the wrapped View
	 */
	public void setAvailablePages(int pages);

	/**
	 * Calculates the available pages and sets <i>availablePages</i>.<br/>
	 * <br/>
	 * If at the end of the view, sets it to current page.<br/>
	 * <br/>
	 * Maximum number of pages to check is 2 is currentPage > 3, otherwise 5 -
	 * currentPage. Links will only be shown to 5 pages, so beyond page 3, links
	 * will only be shown for 2 pages either side of currentPage.<br/>
	 * <br/>
	 * Otherwise calls relevant getRemainingPages up to the maximum depending on
	 * whether the collection is a ViewEntryCollection or a ViewNavigator.<br/>
	 * <br/>
	 * If currentPage > 3, sets pageCount to pageCount + currentPage. Otherwise,
	 * if pageCount < 5, sets pageCount to currentPage + pageCount.<br/>
	 * <br/>
	 * 
	 * @param coll
	 */
	public void calculateAvailablePages(Object coll);

	/**
	 * Calculates the remaining pages after the current page based on a
	 * ViewNavigator. Where possible use this, because ViewNavigators are
	 * quicker and have a skip() method to skip <i>count</i> entries and return
	 * the number of available entries up to a defined maximum.
	 *
	 * @param nav
	 *            ViewNavigator containing entries available in this viewWrapper
	 * @param maxCount
	 *            maximum number of pages to look for, 5 or currentPage + 2,
	 *            whichever ia higher
	 * @return int remaining pages
	 */
	public int getRemainingPages(ViewNavigator nav, int maxCount);

	/**
	 * Calculates the remaining pages after the current page based on a
	 * ViewEntryCollection. Where possible use the ViewNavigator option, but
	 * sometimes that's not possible. This method needs to do a for loop to
	 * iterate through to see if there are <i>count</i> number of entries
	 * available for the relevant page
	 *
	 * @param ec
	 *            ViewEntryCollection containing entries available in this
	 *            viewWrapper
	 * @param maxCount
	 *            maximum number of pages to look for, 5 or currentPage + 2,
	 *            whichever ia higher
	 * @return int remaining pages
	 */
	public int getRemainingPages(ViewEntryCollection ec, int maxCount);

	/**
	 * Gets the current page number being displayed
	 *
	 * @return int current page number
	 */
	public int getCurrentPage();

	/**
	 * Sets the current page number being displayed
	 *
	 * @param page
	 *            int current page number
	 */
	public void setCurrentPage(int page);

}
