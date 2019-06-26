package storage.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import storage.database.Good;

/**
 * 
 * @author Masha. Panel for searching
 *
 */
public class SearchPanel extends JPanel {
	private JTextField queryField;
	private JScrollPane resultPanel;
	private JPanel panelContainer;
	private String[] searchCriteriasForGroups = { "Ім'я", "Опис" };
	private String[] searchCriteriasForGoods = { "Ім'я", "Опис", "Група", "Виробник" };

	/**
	 * Create the panel.
	 */
	public SearchPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		queryField = new JTextField();
		queryField.setHorizontalAlignment(SwingConstants.CENTER);
		queryField.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.gridwidth = 3;
		gbc_textField.insets = new Insets(5, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 0;

		add(queryField, gbc_textField);
		queryField.setColumns(10);

		JButton searchButton = new JButton("Пошук");
		searchButton.setForeground(new Color(255, 255, 255));
		searchButton.setBackground(new Color(0, 204, 153));
		searchButton.setFocusable(false);
		searchButton.setToolTipText("Натисніть, щоб почати пошук");
		searchButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_searchButton = new GridBagConstraints();
		gbc_searchButton.insets = new Insets(5, 0, 5, 0);
		gbc_searchButton.gridx = 3;
		gbc_searchButton.gridy = 0;
		add(searchButton, gbc_searchButton);

		JRadioButton goods = new JRadioButton("Товари");
		goods.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_radioButton = new GridBagConstraints();
		gbc_radioButton.insets = new Insets(0, 0, 5, 5);
		gbc_radioButton.gridx = 0;
		gbc_radioButton.gridy = 1;
		goods.setSelected(true);
		add(goods, gbc_radioButton);

		JComboBox goodsBox = new JComboBox(searchCriteriasForGoods);
		goodsBox.setForeground(Color.WHITE);
		goodsBox.setBackground(new Color(0, 204, 153));
		goodsBox.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_goodsBox = new GridBagConstraints();
		gbc_goodsBox.insets = new Insets(0, 0, 5, 5);
		gbc_goodsBox.fill = GridBagConstraints.HORIZONTAL;
		gbc_goodsBox.gridx = 1;
		gbc_goodsBox.gridy = 1;
		add(goodsBox, gbc_goodsBox);

		JRadioButton groups = new JRadioButton("Групи");
		groups.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_radioButton_1 = new GridBagConstraints();
		gbc_radioButton_1.insets = new Insets(0, 0, 5, 5);
		gbc_radioButton_1.gridx = 2;
		gbc_radioButton_1.gridy = 1;
		add(groups, gbc_radioButton_1);

		JComboBox groupsBox = new JComboBox(searchCriteriasForGroups);
		groupsBox.setForeground(Color.WHITE);
		groupsBox.setBackground(new Color(0, 204, 153));
		groupsBox.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_groupsBox = new GridBagConstraints();
		gbc_groupsBox.insets = new Insets(0, 0, 5, 0);
		gbc_groupsBox.gridx = 3;
		gbc_groupsBox.gridy = 1;
		add(groupsBox, gbc_groupsBox);

		groups.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (groups.isSelected()) {
					goods.setSelected(false);

				} else {
					goods.setSelected(true);

				}
			}
		});

		panelContainer = new JPanel();

		goods.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (goods.isSelected()) {
					groups.setSelected(false);

				} else {
					groups.setSelected(true);
				}
			}
		});

		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String query = queryField.getText();
				if (goods.isSelected()) {
					String criteria = goodsBox.getSelectedItem().toString();
					System.out.println(criteria);
					Client.sendGoodSearchRequest(query, criteria);
				} else {
					String criteria = groupsBox.getSelectedItem().toString();
					System.out.println(criteria);
					Client.sendGroupSearchRequest(query, criteria);
				}

			}
		});
	}

	/**
	 * Add results to panel
	 * 
	 * @param results
	 */
	public void addResult(JPanel[] panels) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelContainer.setLayout(gridBagLayout);
		int row = 0;
		for (JPanel jPanel : panels) {
			jPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			GridBagConstraints gbc_radioButton = new GridBagConstraints();
			gbc_radioButton.insets = new Insets(0, 0, 5, 5);
			gbc_radioButton.gridx = 0;
			gbc_radioButton.gridy = row;
			panelContainer.add(new SearchGoodViewPanel(new Good("name", "", "", "", 0, 0)), gbc_radioButton);
			row++;
		}
		resultPanel = new JScrollPane(panelContainer);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 2;
		gbc_scrollPane.gridwidth = 3;
		add(resultPanel, gbc_scrollPane);
	}

}
