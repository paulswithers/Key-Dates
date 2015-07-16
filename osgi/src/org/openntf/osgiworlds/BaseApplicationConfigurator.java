package org.openntf.osgiworlds;

import javax.servlet.ServletContext;

public abstract class BaseApplicationConfigurator implements ApplicationConfigurator {

	private static final String ERROR_DONT_USE_DIRECTLY = "This base application configurator shouldn't be used directly, verify the configuration.";
	private ServletContext appContext = null;

	@Override
	public void configure(ServletContext context) {
		throw new IllegalStateException(ERROR_DONT_USE_DIRECTLY);

	}

	public ServletContext getAppContext() {
		return appContext;
	}

	public void setAppContext(ServletContext appContext) {
		this.appContext = appContext;
	}

}
