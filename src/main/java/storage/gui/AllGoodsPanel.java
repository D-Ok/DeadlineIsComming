package storage.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import storage.database.Good;

public class AllGoodsPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public AllGoodsPanel(JPanel[] panels) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("Загальна вартість:");
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JScrollPane scrollPane = new JScrollPane();

		JPanel panelContainer = new JPanel();
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panelContainer.setLayout(gridBagLayout1);
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

		scrollPane.add(panelContainer);
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 1;
		add(scrollPane, gbc_scrollPane);
	}

}
