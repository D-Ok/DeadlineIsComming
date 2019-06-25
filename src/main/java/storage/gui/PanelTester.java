package storage.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PanelTester extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					PanelTester frame = new PanelTester();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public PanelTester() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 700);
		add(new SearchPanel());
		/*
		 * // add(new SearchGoodViewPanel(new Good("name", "", "", "", 0, 0)));
		 * SearchPanel p = new SearchPanel(); // for (int i = 0; i < 50; i++)
		 * p.addResult(new SearchGoodViewPanel(new Good("name", "", "", "", 0, 0)));
		 * p.setVisible(true); add(p);
		 */
		// add(new SearchGoodViewPanel(new Good("nvnjgjf", "vnjfjvngjg",
		// "cnnvjjfjvdjvn", "vmnjvjvndvn", 0, 0)));
	}

}
