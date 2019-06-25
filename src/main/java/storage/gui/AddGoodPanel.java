package storage.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import storage.database.Good;
import storage.database.Group;

/**
 * 
 * @author Masha. Good adding panel.
 *
 */
public class AddGoodPanel extends JPanel {

	private JTextField nameField;
	private JTextField descriptionField;
	private JTextField producerField;

	/**
	 * Create the panel.
	 */
	public AddGoodPanel(Group[] groups) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0 };
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
		// gridBagLayout.columnWeights = new double[] { 0 };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0 };
		setLayout(gridBagLayout);

		JLabel label = new JLabel("Ім'я:");
		label.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.anchor = GridBagConstraints.EAST;
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		add(label, gbc_label);

		nameField = new JTextField();
		nameField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(nameField, gbc_textField);
		nameField.setColumns(10);

		JLabel label_3 = new JLabel("Група:");
		label_3.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_3 = new GridBagConstraints();
		gbc_label_3.anchor = GridBagConstraints.EAST;
		gbc_label_3.insets = new Insets(0, 0, 5, 5);
		gbc_label_3.gridx = 0;
		gbc_label_3.gridy = 1;
		add(label_3, gbc_label_3);

		JComboBox groupBox = new JComboBox(groups);
		groupBox.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_comboBox = new GridBagConstraints();
		gbc_comboBox.insets = new Insets(0, 0, 5, 0);
		gbc_comboBox.gridx = 1;
		gbc_comboBox.gridy = 1;
		add(groupBox, gbc_comboBox);

		JLabel lblNewLabel = new JLabel("Опис:");
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 2;
		add(lblNewLabel, gbc_lblNewLabel);

		descriptionField = new JTextField();
		descriptionField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 2;
		add(descriptionField, gbc_textField_1);
		descriptionField.setColumns(10);

		JLabel label_1 = new JLabel("Виробник:");
		label_1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 3;
		add(label_1, gbc_label_1);

		producerField = new JTextField();
		producerField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 3;
		add(producerField, gbc_textField_2);
		producerField.setColumns(10);

		JLabel label_2 = new JLabel("Ціна за одиницю:");
		label_2.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 4;
		add(label_2, gbc_label_2);

		JSpinner priceSpinner = new JSpinner();
		priceSpinner.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		priceSpinner.setModel(new SpinnerNumberModel(0, new Double(0), null, new Double(0.5)));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 4;
		gbc_spinner.anchor = GridBagConstraints.WEST;
		add(priceSpinner, gbc_spinner);

		JLabel label_4 = new JLabel("Кількість на складі:");
		label_4.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_4 = new GridBagConstraints();
		gbc_label_4.insets = new Insets(0, 0, 5, 5);
		gbc_label_4.gridx = 0;
		gbc_label_4.gridy = 5;
		add(label_4, gbc_label_4);

		JSpinner amountSpinner = new JSpinner();
		amountSpinner.setModel(new SpinnerNumberModel(new Integer(0), new Integer(0), null, new Integer(1)));
		amountSpinner.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_spinner_1 = new GridBagConstraints();
		gbc_spinner_1.insets = new Insets(0, 0, 5, 0);
		gbc_spinner_1.gridx = 1;
		gbc_spinner_1.gridy = 5;
		add(amountSpinner, gbc_spinner_1);

		JButton saveChanges = new JButton("Додати товар");
		saveChanges.setForeground(new Color(255, 255, 255));
		saveChanges.setBackground(new Color(0, 204, 153));
		saveChanges.setFocusable(false);
		saveChanges.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.insets = new Insets(0, 0, 0, 5);
		gbc_button.gridx = 0;
		gbc_button.gridy = 6;
		add(saveChanges, gbc_button);

		saveChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Good g = new Good(nameField.getText(), descriptionField.getText(), producerField.getText(),
						groupBox.getSelectedItem().toString(), (double) priceSpinner.getValue(),
						(int) amountSpinner.getValue());

				// TODO send client good
			}
		});

	}

}
