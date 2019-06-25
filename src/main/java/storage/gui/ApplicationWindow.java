package storage.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class ApplicationWindow extends JFrame {

	private JPanel contentPane;
	private JMenuBar menuBar;
	private JMenu addRemoveMenu, searchMenu, editMenu, statisticsMenu, userMenu;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ApplicationWindow frame = new ApplicationWindow();
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
	public ApplicationWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 20, 950, 700);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		menuBar = new JMenuBar();
		addRemoveMenu = new JMenu("Списання та завезення товару");
		addRemoveMenu.setForeground(new Color(0, 0, 0));
		addRemoveMenu.setBackground(Color.WHITE);
		addRemoveMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		searchMenu = new JMenu("Пошук");
		searchMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		editMenu = new JMenu("Редагування");
		editMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		statisticsMenu = new JMenu("Статистичні дані");
		statisticsMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		userMenu = new JMenu("Кабінет користувача");
		userMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		menuBar.add(addRemoveMenu);
		menuBar.add(searchMenu);
		menuBar.add(editMenu);
		menuBar.add(statisticsMenu);
		menuBar.add(userMenu);
		this.setJMenuBar(menuBar);
	}

}
