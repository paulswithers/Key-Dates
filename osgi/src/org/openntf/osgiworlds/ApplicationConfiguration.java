package org.openntf.osgiworlds;

public interface ApplicationConfiguration {

	/**
	 * @return the Notes full name for the user that will be treated as the
	 *         signer for this application. May return null to indicate no
	 *         signer has been specified. In that case the server/client
	 *         identity is used.
	 */
	public String getAppSignerFullName();

	/**
	 * OsgiWorlds can run in developer mode allowing identity switching to
	 * simplify development & testing on a client based setup.
	 *
	 * Developer mode is controlled by adding
	 *
	 * osgiworlds.developermode=true to properties
	 *
	 * @return true if OsgiWorlds is running in developer mode.
	 */
	public boolean isDeveloperMode();

	public String getDefaultDevelopmentUserName();

}
