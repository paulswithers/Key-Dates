package org.openntf.osgiworlds;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.openntf.domino.Session;
import org.openntf.domino.ext.Session.Fixes;
import org.openntf.domino.session.INamedSessionFactory;
import org.openntf.domino.session.ISessionFactory;
import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;

import com.ibm.domino.napi.NException;
import com.ibm.domino.napi.c.NotesUtil;
import com.ibm.domino.napi.c.xsp.XSPNative;
import com.ibm.domino.osgi.core.context.ContextInfo;

import lotus.domino.NotesException;

/**
 *
 * @author Paul Withers<br/>
 *         <br/>
 *         The default configuration for a OsgiWorlds Application is to use as
 *         CURRENT the following defaults:
 *
 */
@SuppressWarnings("restriction")
public class DefaultDominoApplicationConfig extends BaseApplicationConfigurator {

	/**
	 * ThreadLocal for holding the current user's Domino Full Name
	 */
	private static ThreadLocal<String> dominoFullName = new ThreadLocal<String>() {

		@Override
		protected String initialValue() {
			return "Anonymous";
		}

	};

	/**
	 * @author Paul Withers
	 *
	 *         enum for Identity types available within the system - SIGNER and
	 *         CURRENT
	 */
	enum IdentityLocator {
		SIGNER, CURRENT
	}

	/**
	 * @author Paul Withers
	 *
	 *         Session Factory for generating a Session for the specific user
	 */
	@SuppressWarnings("serial")
	private class XSPBasedNamedSessionFactory implements ISessionFactory, INamedSessionFactory {

		private boolean _isFullAccess = false;
		private IdentityLocator _identityLocator = null;

		/**
		 * Overloaded constructor, allowing setting of variables
		 *
		 * @param fullAccess
		 *            boolean whether or not the session has full access
		 * @param locator
		 *            IdentityLocation enum for session type (CURRENT / SIGNER)
		 */
		public XSPBasedNamedSessionFactory(boolean fullAccess, IdentityLocator locator) {
			this._isFullAccess = fullAccess;
			this._identityLocator = locator;
		}

		/*
		 * (non-Javadoc)
		 *
		 * @see org.openntf.domino.session.ISessionFactory#createSession()
		 */
		@Override
		public Session createSession() {

			String username = null;
			switch (_identityLocator) {
			case SIGNER:
				username = getAppSignerFullName();
				break;
			case CURRENT:
				username = getDominoFullName();
			}

			return createSession(username);

		}

		/*
		 * (non-Javadoc)
		 *
		 * @see
		 * org.openntf.domino.session.INamedSessionFactory#createSession(java.
		 * lang.String)
		 */
		@SuppressWarnings("deprecation")
		@Override
		public Session createSession(String username) {
			try {
				final long userHandle = NotesUtil.createUserNameList(username);
				System.out.println("Running as " + username);
				final lotus.domino.Session rawSession = XSPNative.createXPageSessionExt(username, userHandle, false, true, _isFullAccess);
				final Session sess = Factory.fromLotus(rawSession, Session.SCHEMA, null);
				sess.setFixEnable(Fixes.APPEND_ITEM_VALUE, true);
				sess.setFixEnable(Fixes.DOC_UNID_NULLS, true);
				sess.setFixEnable(Fixes.FORCE_JAVA_DATES, true);
				sess.setFixEnable(Fixes.MIME_CONVERT, true);
				sess.setFixEnable(Fixes.ODA_NAMES, true);
				sess.setFixEnable(Fixes.REMOVE_ITEM, true);
				sess.setFixEnable(Fixes.REPLACE_ITEM_NULL, true);
				sess.setFixEnable(Fixes.VIEW_UPDATE_OFF, true);
				sess.setFixEnable(Fixes.VIEWENTRY_RETURN_CONSTANT_VALUES, true);
				return sess;
			} catch (final NException e) {
				throw new RuntimeException(e);
			} catch (final NotesException e) {
				throw new RuntimeException(e);
			}
		}
	};

	/*
	 * (non-Javadoc)
	 *
	 * @see org.openntf.osgiworlds.BaseApplicationConfigurator#configure(javax.
	 * servlet.ServletContext)
	 */
	@Override
	public void configure(ServletContext context, HttpServletRequest request) {

		// Save the current application context
		this.setAppContext(context);

		// Read the signer identity
		setAppSignerFullName(context.getInitParameter(CONTEXTPARAM_CWAPPSIGNER_IDENTITY));

		// Get the current user's Domino Full Name
		// First try session attribute "org.openntf.osgiworlds.request.username"
		if (request.getSession(false) != null && request.getSession(false).getAttribute(CONTEXTPARAM_SESSION_IDENTITY) != null) {
			setDominoFullName((String) request.getSession(false).getAttribute(CONTEXTPARAM_SESSION_IDENTITY));
		} else {
			if ("true".equals(context.getInitParameter("osgiworlds.developermode"))) {
				setDeveloperMode(true);
				Factory.println("OSGIWORLDS::" + context.getServletContextName(),
						"OsgiWorlds development mode is enabled through application property \"xworlds.developermode=true\"");

				// Read the development time identity
				setDefaultDevelopmentUserName(context.getInitParameter(CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY));
				if (null != getDefaultDevelopmentUserName()) {
					setDominoFullName(getDefaultDevelopmentUserName());
				} else {
					setDefaultDevelopmentUserName("Anonymous");
					try {
						setDominoFullName(ContextInfo.getUserSession().getEffectiveUserName());
					} catch (final NotesException e) {
						e.printStackTrace();
					}
				}
				request.getSession().setAttribute(CONTEXTPARAM_SESSION_IDENTITY, getDominoFullName());
			} else {
				try {
					setDominoFullName(ContextInfo.getUserSession().getEffectiveUserName());
				} catch (final NotesException e) {
					e.printStackTrace();
				}
				request.getSession().setAttribute(CONTEXTPARAM_SESSION_IDENTITY, getDominoFullName());
			}
		}

		Factory.setSessionFactory(new XSPBasedNamedSessionFactory(false, IdentityLocator.CURRENT), SessionType.CURRENT);
		Factory.setSessionFactory(new XSPBasedNamedSessionFactory(true, IdentityLocator.CURRENT), SessionType.CURRENT_FULL_ACCESS);

		// The behaviour for asSigner session is the same with security enabled
		// or not.
		if (getAppSignerFullName() != null) {
			Factory.setSessionFactory(new XSPBasedNamedSessionFactory(false, IdentityLocator.SIGNER), SessionType.SIGNER);
			Factory.setSessionFactory(new XSPBasedNamedSessionFactory(true, IdentityLocator.SIGNER), SessionType.SIGNER_FULL_ACCESS);
		} else {
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER);
			Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.SIGNER_FULL_ACCESS);
		}

	}

	/**
	 * Gets Notes Name for current user from ThreadLocal map
	 *
	 * @return String Notes Name for current user
	 */
	public static String getDominoFullName() {
		return dominoFullName.get();
	}

	/**
	 * Sets Notes Name for current user from ThreadLocal map
	 *
	 * @param newDominoFullName
	 *            String Notes Name for current user
	 */
	public static void setDominoFullName(String newDominoFullName) {
		dominoFullName.set(newDominoFullName);
	}

}
