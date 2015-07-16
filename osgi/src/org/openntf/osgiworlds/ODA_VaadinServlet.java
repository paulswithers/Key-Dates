package org.openntf.osgiworlds;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.xsp.ODAPlatform;

import com.vaadin.server.VaadinServlet;

public class ODA_VaadinServlet extends VaadinServlet {
	private static final long serialVersionUID = 1L;
	private boolean stopODAPlatform = false;

	@Override
	public void init() throws ServletException {
		if (!ODAPlatform.isStarted()) {
			ODAPlatform.start();
			stopODAPlatform = true;
		}
		super.init();
	}

	@Override
	public void destroy() {
		super.destroy();
		if (stopODAPlatform) {
			ODAPlatform.stop();
		}
	}

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		startDominoThread();
		super.service(request, response);
		stopDominoThread();
	}

	public void startDominoThread() {
		try {
			Factory.initThread(Factory.STRICT_THREAD_CONFIG);
			final DefaultApplicationConfig config = new DefaultApplicationConfig();
			config.configure(getServletContext());
		} catch (final Exception e) {
			stopDominoThread();
		}
	}

	public void stopDominoThread() {
		try {
			Factory.termThread();
		} catch (final Exception e) {
			// TODO: handle exception
		}
	}
}
