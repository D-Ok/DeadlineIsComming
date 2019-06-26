package storage.gui;

/**
 * 
 * @author Masha. Manages client views.
 *
 */
public class ClientWindowManager {
	private static AuthenticationWindow authWindow;
	private static ApplicationWindow appWindow;

	/**
	 * Open login window
	 */
	public static void showLoginWindow() {
		authWindow = new AuthenticationWindow();
		authWindow.setVisible(true);
	}

	/**
	 * Close login window
	 */
	public static void closeLoginWindow() {
		authWindow.setVisible(false);
		// authWindow.dispatchEvent(new WindowEvent(authWindow,
		// WindowEvent.WINDOW_CLOSING));
		authWindow = null;
	}

	/**
	 * Show main application window
	 */
	public static void showApplicationWindow() {
		appWindow = new ApplicationWindow();
		appWindow.setVisible(true);
	}

	/**
	 * Close main application window
	 */
	public static void closeApplicationWindow() {
		// appWindow.dispatchEvent(new WindowEvent(authWindow,
		// WindowEvent.WINDOW_CLOSING));
		appWindow.setVisible(false);
		appWindow = null;
	}

	public static void main(String[] args) {
		ClientWindowManager.showLoginWindow();
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClientWindowManager.closeLoginWindow();
	}

}