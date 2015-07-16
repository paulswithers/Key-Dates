package org.openntf.osgiworlds;

import javax.servlet.ServletContext;

import lotus.domino.NotesException;

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

/**
 * The default configuration for a OsgiWorlds Application is to use as CURRENT
 * the following defaults:
 *
 * @author Paul Withers
 *
 */
public class DefaultApplicationConfig extends BaseApplicationConfigurator implements ApplicationConfiguration {

	private boolean _isDeveloperMode = false;
	private String _defaultDevelopmentUserName = null;
	private String _appSignerFullName = null;

	private static ThreadLocal<String> dominoFullName = new ThreadLocal<String>() {

		@Override
		protected String initialValue() {
			return "Anonymous";
		}

	};

	enum IdentityLocator {
		SIGNER, CURRENT
	}

	@SuppressWarnings("serial")
	private class XSPBasedNamedSessionFactory implements ISessionFactory, INamedSessionFactory {

		private boolean _isFullAccess = false;
		private IdentityLocator _identityLocator = null;

		public XSPBasedNamedSessionFactory(boolean fullAccess, IdentityLocator locator) {
			this._isFullAccess = fullAccess;
			this._identityLocator = locator;
		}

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

		@Override
		public Session createSession(String username) {
			System.out.println("Username: " + username);
			try {
				final long userHandle = NotesUtil.createUserNameList(username);
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

	@Override
	public String getAppSignerFullName() {
		return _appSignerFullName;
	}

	@Override
	public boolean isDeveloperMode() {
		return _isDeveloperMode;
	}

	@Override
	public void configure(ServletContext context) {

		// Save the current application context
		this.setAppContext(context);

		// Read the signer identity
		this._appSignerFullName = context.getInitParameter(CONTEXTPARAM_CWAPPSIGNER_IDENTITY);

		if ("true".equals(context.getInitParameter("osgiworlds.developermode"))) {
			this._isDeveloperMode = true;
			Factory.println("OSGIWORLDS::",
					"OsgiWorlds development mode is enabled through application property \"xworlds.developermode=true\"");

			// Read the development time identity
			this._defaultDevelopmentUserName = context.getInitParameter(CONTEXTPARAM_CWDEFAULTDEVELOPER_IDENTITY);
			if (_defaultDevelopmentUserName == null) {
				_defaultDevelopmentUserName = "Anonymous";
				try {
					setDominoFullName(ContextInfo.getUserSession().getEffectiveUserName());
				} catch (final NotesException e) {
					e.printStackTrace();
				}
			} else {
				setDominoFullName(_defaultDevelopmentUserName);
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

	public static String getDominoFullName() {
		return dominoFullName.get();
	}

	public static void setDominoFullName(String newDominoFullName) {
		dominoFullName.set(newDominoFullName);
	}

	@Override
	public String getDefaultDevelopmentUserName() {
		return _defaultDevelopmentUserName;
	}

}
