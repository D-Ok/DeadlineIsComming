package storage.gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class ApplicationWindow extends JFrame {

	private ApplicationWindow frame;
	private JPanel panel;
	private JMenuBar menuBar;
	private JMenu addRemoveMenu, searchMenu, addMenu, statisticsMenu, userMenu;

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
		frame = this;
		panel = new JPanel();
		add(panel);
		menuBar = new JMenuBar();
		addRemoveMenu = new JMenu("Списання та завезення товару");
		addRemoveMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		addRemoveMenu.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// frame.changePanel(new ChangeGoodAmountPanel(good));
			}
		});
		searchMenu = new JMenu("Пошук");
		searchMenu.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				frame.changePanel(new SearchPanel());
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		searchMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		addMenu = new JMenu("Додати");
		JMenuItem addGood = new JMenuItem("Товар");
		JMenuItem addGroup = new JMenuItem("Групу");
		addMenu.add(addGood);
		addGood.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.changePanel(new AddGoodPanel(Client.getAllGroups()));
			}
		});
		addMenu.add(addGroup);
		addGroup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.changePanel(new AddGroupPanel());
			}
		});
		addMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		statisticsMenu = new JMenu("Статистичні дані");
		JMenuItem showAllGoods = new JMenuItem("Всі товари");
		JMenuItem showByGroups = new JMenuItem("Товари групи");
		statisticsMenu.add(showAllGoods);
		showAllGoods.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// frame.changePanel(new AllGoodsPanel());
			}
		});
		statisticsMenu.add(showByGroups);
		showByGroups.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				frame.changePanel(new ShowGoodsByGroupPanel(Client.getAllGroups()));
			}
		});
		statisticsMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		userMenu = new JMenu("Кабінет користувача");
		userMenu.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		menuBar.add(addRemoveMenu);
		menuBar.add(searchMenu);
		menuBar.add(addMenu);
		menuBar.add(statisticsMenu);
		menuBar.add(userMenu);
		this.setJMenuBar(menuBar);
	}

	private void changePanel(JPanel newPanel) {
		this.remove(panel);
		panel = newPanel;
		this.add(panel);
		this.setVisible(true);
	}

}
