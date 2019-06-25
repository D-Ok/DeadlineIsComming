package storage.gui;

import java.awt.event.WindowEvent;

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
		// authWindow.setVisible(false);
		authWindow.dispatchEvent(new WindowEvent(authWindow, WindowEvent.WINDOW_CLOSING));
		authWindow = null;
	}

	/**
	 * Show main application window
	 */
	public static void showApplicationWindow() {
		ApplicationWindow aw = new ApplicationWindow();
		aw.setVisible(true);
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