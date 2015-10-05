package org.openntf.osgiworlds;

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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Base abstract application configurator. Nothing in this class is
 *         Domino-specific.
 *
 */
public abstract class BaseApplicationConfigurator implements ApplicationConfiguration {

	private static final String ERROR_DONT_USE_DIRECTLY = "This base application configurator shouldn't be used directly, verify the configuration.";
	private ServletContext appContext = null;
	private boolean _isDeveloperMode = false;
	private String _defaultDevelopmentUserName = null;
	private String _appSignerFullName = null;

	/**
	 * Getter for appContext
	 *
	 * @return ServletContext Vaadin servlet context for the application
	 */
	public ServletContext getAppContext() {
		return appContext;
	}

	/**
	 * Setter for appContext
	 *
	 * @param appContext
	 *            ServletContext Vaadin servlet context for the application
	 */
	public void setAppContext(ServletContext appContext) {
		this.appContext = appContext;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.ApplicationConfiguration#isDeveloperMode()
	 */
	@Override
	public boolean isDeveloperMode() {
		return _isDeveloperMode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.ApplicationConfiguration#setDeveloperMode(boolean)
	 */
	@Override
	public void setDeveloperMode(boolean developerMode) {
		_isDeveloperMode = developerMode;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.ApplicationConfiguration#getAppSignerFullName()
	 */
	@Override
	public String getAppSignerFullName() {
		return _appSignerFullName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.ApplicationConfiguration#setAppSignerFullName(java
	 * .lang.String)
	 */
	@Override
	public void setAppSignerFullName(String signerFullName) {
		_appSignerFullName = signerFullName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.ApplicationConfiguration#
	 * getDefaultDevelopmentUserName()
	 */
	@Override
	public String getDefaultDevelopmentUserName() {
		return _defaultDevelopmentUserName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.ApplicationConfiguration#
	 * setDefaultDevelopmentUserName(java.lang.String)
	 */
	@Override
	public void setDefaultDevelopmentUserName(String developmentUserName) {
		_defaultDevelopmentUserName = developmentUserName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.openntf.osgiworlds.ApplicationConfigurator#configure(javax.servlet.
	 * ServletContext)
	 */
	@Override
	public void configure(ServletContext context, HttpServletRequest request) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);

	}

}
