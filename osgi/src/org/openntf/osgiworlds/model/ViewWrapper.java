package org.openntf.osgiworlds.model;

import java.util.List;
import java.util.Map;

import org.openntf.domino.Database;
import org.openntf.domino.View;
import org.openntf.domino.ViewEntryCollection;
import org.openntf.domino.ViewNavigator;

public interface ViewWrapper {

	public String getServerName();

	public void setServerName(String serverName);

	public String getDatabaseName();

	public void setDatabaseName(String databaseName);

	public String getViewName();

	public void setViewName(String viewName);

	public View getView();

	public List<ViewEntryWrapper> getEntries();

	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap();

	public Map<Object, List<ViewEntryWrapper>> getEntriesAsMap(Object key);

	public int getStart();

	public void setStart(int start);

	public int getCount();

	public void setCount(int rows);

	public String getSearchString();

	public void setSearchString(String searchString);

	public List<Object> getCategory();

	public void setCategory(List<Object> categories);

	public void setCategory(Object singleCat);

	public boolean isExactMatch();

	public void setExactMatch(boolean exactMatch);

	public boolean isSingleCat();

	public void setSingleCat(boolean singleCat);

	public Database getParentDatabase();

	public boolean isEndOfView();

	public void setEndOfView(boolean endOfView);

	public com.vaadin.navigator.View getParentVaadinView();

	public void setParentVaadinView(com.vaadin.navigator.View parentView);

	public void redrawContents();

	public int getAvailablePages();

	public void setAvailablePages(int pages);

	public void calculateAvailablePages(ViewNavigator nav);

	public void calculateAvailablePages(ViewEntryCollection nav);

	public int getCurrentPage();

	public void setCurrentPage(int page);

}
