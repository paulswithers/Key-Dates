package org.openntf.osgiworlds;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Paul Withers<br/>
 *         <br/>
 *         Interface for application configuration, defining methods for getting
 *         application signer, development user and whether or not the
 *         application is in developmentMode.
 *
 */
public interface ApplicationConfiguration {

	/**
	 * The full name for the identity to be used when a "SIGNER" session is
	 * required.
	 */
	public static final String CONTEXTPARAM_CWAPPSIGNER_IDENTITY = "org.openntf.osgiworlds.appsignername";
	/**
	 * The full name for the identity to be used for the development user of the
	 * system
	 */
	public static final String CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY = "org.openntf.osgiworlds.devtimename";

	/**
	 * The session variable in which to store the identity to be used for the
	 * current session user
	 */
	public static final String CONTEXTPARAM_SESSION_IDENTITY = "org.openntf.osgiworlds.request.username";

	/**
	 * Gets the Full name for the user that will be treated as the signer for
	 * this application from web.xml. May return null to indicate no signer has
	 * been specified. In that case the server/client identity is used.
	 *
	 * @return String full name for "signer"
	 */
	public String getAppSignerFullName();

	/**
	 * Sets the full name for the user that will be treated as the signer for
	 * this application
	 *
	 * @param signerFullName
	 *            String full name for "signer"
	 */
	public void setAppSignerFullName(String signerFullName);

	/**
	 * OsgiWorlds can run in developer mode allowing identity switching to
	 * simplify development & testing on a client based setup.
	 *
	 * Developer mode is controlled by adding
	 * <b>osgiworlds.developermode=true</b> to properties
	 *
	 * @return boolean true if OsgiWorlds is running in developer mode.
	 */
	public boolean isDeveloperMode();

	/**
	 * OsgiWorlds can run in developer mode allowing identity switching to
	 * simplify development & testing on a client based setup.<br/>
	 * <br/>
	 * Developer mode is controlled by adding
	 * <b>osgiworlds.developermode=true</b> to properties
	 *
	 * @param developerMode
	 *            boolean whether to set the application in developerMode or not
	 */
	public void setDeveloperMode(boolean developerMode);

	/**
	 * In developer mode OsgiWorlds can run as a specific user. This avoids the
	 * need for specific authentication. Gets the full name for the user that
	 * will be treated as the development user for this application from
	 * web.xml.
	 *
	 * @return String the full name for the development user of the application
	 */
	public String getDefaultDevelopmentUserName();

	/**
	 * In developer mode OsgiWorlds can run as a specific user. This avoids the
	 * need for specific authentication. Sets the full name for the user that
	 * will be treated as the development user for this application.
	 *
	 * @param developmentUserName
	 *            String the full name for the development user of the
	 *            application
	 */
	public void setDefaultDevelopmentUserName(String developmentUserName);

	/**
	 * @param context
	 *            ServletContext Vaadin servlet context of the application
	 * @param request
	 *            HttpServletRequest current request to the server
	 */
	public void configure(ServletContext context, HttpServletRequest request);

}
