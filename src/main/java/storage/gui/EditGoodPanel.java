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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import storage.database.Good;

/**
 * 
 * @author Masha. Good editing panel.
 *
 */
public class EditGoodPanel extends JPanel {
	private JTextField nameField;
	private JTextField descriptionField;
	private JTextField producerField;

	/**
	 * Create the panel.
	 */
	public EditGoodPanel(Good good) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.5, 1.0, };
		gridBagLayout.rowWeights = new double[] { 0.5, 0.5, 0.5, 0.5, 0.5 };
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
		nameField.setText(good.getName());
		nameField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		add(nameField, gbc_textField);
		nameField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Опис:");
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		add(lblNewLabel, gbc_lblNewLabel);

		descriptionField = new JTextField();
		descriptionField.setText(good.getDescription());
		descriptionField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 1;
		gbc_textField_1.gridy = 1;
		add(descriptionField, gbc_textField_1);
		descriptionField.setColumns(10);

		JLabel label_1 = new JLabel("Виробник:");
		label_1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_1 = new GridBagConstraints();
		gbc_label_1.anchor = GridBagConstraints.EAST;
		gbc_label_1.insets = new Insets(0, 0, 5, 5);
		gbc_label_1.gridx = 0;
		gbc_label_1.gridy = 2;
		add(label_1, gbc_label_1);

		producerField = new JTextField();
		producerField.setText(good.getProducer());
		producerField.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_2.gridx = 1;
		gbc_textField_2.gridy = 2;
		add(producerField, gbc_textField_2);
		producerField.setColumns(10);

		JLabel label_2 = new JLabel("Ціна за одиницю:");
		label_2.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label_2 = new GridBagConstraints();
		gbc_label_2.anchor = GridBagConstraints.EAST;
		gbc_label_2.insets = new Insets(0, 0, 5, 5);
		gbc_label_2.gridx = 0;
		gbc_label_2.gridy = 3;
		add(label_2, gbc_label_2);

		JSpinner spinner = new JSpinner();
		spinner.setModel(new SpinnerNumberModel(good.getPrice(), new Double(0), null, new Double(0.5)));
		GridBagConstraints gbc_spinner = new GridBagConstraints();
		gbc_spinner.insets = new Insets(0, 0, 5, 0);
		gbc_spinner.gridx = 1;
		gbc_spinner.gridy = 3;
		gbc_spinner.anchor = GridBagConstraints.WEST;
		add(spinner, gbc_spinner);

		JButton saveChanges = new JButton("Зберегти");
		saveChanges.setForeground(new Color(255, 255, 255));
		saveChanges.setBackground(new Color(0, 204, 153));
		saveChanges.setFocusable(false);
		saveChanges.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_button = new GridBagConstraints();
		gbc_button.gridx = 0;
		gbc_button.gridy = 4;
		gbc_button.gridwidth = 2;
		add(saveChanges, gbc_button);
		/*
		 * Perform changes saving
		 */
		saveChanges.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				good.setName(nameField.getText());
				good.setProducer(producerField.getText());
				good.setDescription(descriptionField.getText());
				good.setPrice((double) spinner.getValue());
				// TODO call client method
			}
		});
	}

}
