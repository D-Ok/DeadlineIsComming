package storage.gui;

import javax.swing.JFrame;

/**
 * 
 * @author Masha. Class for displaying edit panels in another window
 *
 */
public class HelpingWindow extends JFrame {

	private static HelpingWindow window = new HelpingWindow();

	/**
	 * Create the frame.
	 */
	private HelpingWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
	}

	public static HelpingWindow getInstance() {
		return window;
	}

}
