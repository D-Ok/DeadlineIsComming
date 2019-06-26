package storage.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;

import storage.database.Good;

public class ChangeGoodAmountPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public ChangeGoodAmountPanel(Good good) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 1.0, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.5, 1.0 };
		setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel(good.getName());
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		gbc_lblNewLabel.gridwidth = 2;
		add(lblNewLabel, gbc_lblNewLabel);

		JSpinner spinner = new JSpinner();
		spinner.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.insets = new Insets(0, 0, 5, 10);
		gbc_spinner.gridx = 0;
		gbc_spinner.gridy = 1;
		gbc_spinner.anchor = GridBagConstraints.EAST;
		add(spinner, gbc_spinner);

		String[] options = { "Списати", "Зарахувати" };
		JComboBox comboBox = new JComboBox(options);
		comboBox.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		gbc_comboBox.anchor = GridBagConstraints.WEST;
		add(comboBox, gbc_comboBox);

		JButton saveChanges = new JButton("Зберегти");
		saveChanges.setForeground(new Color(255, 255, 255));
		saveChanges.setBackground(new Color(0, 204, 153));
		saveChanges.setFocusable(false);
		saveChanges.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_saveChanges = new GridBagConstraints();
		gbc_saveChanges.insets = new Insets(0, 0, 0, 5);
		gbc_saveChanges.gridx = 0;
		gbc_saveChanges.gridy = 2;
		gbc_saveChanges.gridwidth = 2;
		add(saveChanges, gbc_saveChanges);

	}

}
