package storage.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import storage.database.Group;

public class AddGroupPanel extends JPanel {
	private JTextField nameField;
	private JTextField descriptionField;

	/**
	 * Create the panel.
	 */
	public AddGroupPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.5, 1.0 };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.5, 1.0 };
		setLayout(gridBagLayout);

		JLabel label = new JLabel("Ім'я:");
		label.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);

		nameField = new JTextField();
		nameField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 10, 5, 10);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(nameField, gbc_textField);
		nameField.setColumns(10);

		JLabel label_1 = new JLabel("Опис:");
		label_1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 1;
		add(label_1, gbc_label_1);

		descriptionField = new JTextField();
		descriptionField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 10, 5, 10);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		add(descriptionField, gbc_textField_1);
		descriptionField.setColumns(10);

		JButton saveChanges = new JButton("Додати групу");
		saveChanges.setForeground(new Color(255, 255, 255));
		saveChanges.setBackground(new Color(0, 204, 153));
		saveChanges.setFocusable(false);
		saveChanges.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 0;
		gbc_button.gridy = 2;
		gbc_button.gridwidth = 2;
		add(saveChanges, gbc_button);

		saveChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Group group = new Group(nameField.getText(), descriptionField.getText());
				// TODO send client group
			}
		});

	}

}
