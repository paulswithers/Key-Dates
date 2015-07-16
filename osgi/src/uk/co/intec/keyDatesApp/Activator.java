package uk.co.intec.keyDatesApp;

import lotus.domino.NotesThread;

import org.openntf.domino.utils.Factory;
import org.openntf.domino.utils.Factory.SessionType;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		// startDominoThread();
		Activator.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		// stopDominoThread();
		Activator.context = null;
	}

	public static void startDominoThread() {

		if (!Factory.isStarted()) { // Wait for ODA Factory to beready
			int timeout = 30; // Maximum wait time for Factory startup;
			while (!Factory.isStarted() && timeout > 0) {
				try {
					System.out.print(".");
					timeout--;
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		System.out.println("Starting factory thread...");
		Factory.initThread(Factory.STRICT_THREAD_CONFIG);

		// Override the default session factory.
		System.out.println("Overriding session factory");
		Factory.setSessionFactory(Factory.getSessionFactory(SessionType.NATIVE), SessionType.CURRENT);

		System.out.println("Initialising Notes Thread");
		NotesThread.sinitThread();

	}

	public static void stopDominoThread() {

		NotesThread.stermThread();
		Factory.termThread();

	}

}
