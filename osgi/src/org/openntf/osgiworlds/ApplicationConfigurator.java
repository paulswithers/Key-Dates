package org.openntf.osgiworlds;

import javax.servlet.ServletContext;

public interface ApplicationConfigurator {

	/**
	 * The notes full name for the identity to be used when a "SIGNER" session
	 * is required.
	 */
	public static final String CONTEXTPARAM_CWAPPSIGNER_IDENTITY = "org.openntf.osgiworlds.appsignername";
	/**
	 * The notes full name for the identity to be used when a "SIGNER" session
	 * is required.
	 */
	public static final String CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY = "org.openntf.osgiworlds.devtimename";

	/**
	 * @param context
	 */
	public void configure(ServletContext context);

}
