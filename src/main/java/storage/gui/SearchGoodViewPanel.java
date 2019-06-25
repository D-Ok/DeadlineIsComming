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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import storage.database.Good;

public class SearchGoodViewPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public SearchGoodViewPanel(Good good) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.2, 0.2, 0.2, 0.5, 0.5, 0.5 };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		JLabel nameLabel = new JLabel("Ім'я: " + good.getName());
		nameLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_nameLabel = new GridBagConstraints();
		gbc_nameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_nameLabel.gridx = 0;
		gbc_nameLabel.gridy = 0;
		add(nameLabel, gbc_nameLabel);

		JLabel lblNewLabel = new JLabel("Група: " + good.getGroupName());
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Виробник: " + good.getProducer());
		lblNewLabel_1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 2;
		gbc_lblNewLabel_1.gridy = 0;
		add(lblNewLabel_1, gbc_lblNewLabel_1);

		JButton info = new JButton("Інформація");
		info.setIcon(null);
		info.setForeground(new Color(255, 255, 255));
		info.setBackground(new Color(0, 204, 153));
		info.setFocusable(false);
		info.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_info = new GridBagConstraints();
		gbc_info.insets = new Insets(0, 10, 5, 5);
		gbc_info.gridx = 3;
		gbc_info.gridy = 0;
		add(info, gbc_info);

		JButton delete = new JButton("Видалити");
		GridBagConstraints gbc_delete = new GridBagConstraints();
		gbc_delete.insets = new Insets(0, 10, 5, 5);
		gbc_delete.gridx = 4;
		gbc_delete.gridy = 0;
		delete.setForeground(new Color(255, 255, 255));
		delete.setBackground(new Color(0, 204, 153));
		delete.setFocusable(false);
		delete.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(delete, gbc_delete);

		JButton edit = new JButton("Редагувати");
		edit.setForeground(new Color(255, 255, 255));
		edit.setBackground(new Color(0, 204, 153));
		edit.setFocusable(false);
		edit.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_edit = new GridBagConstraints();
		gbc_edit.insets = new Insets(0, 10, 5, 5);
		gbc_edit.gridx = 5;
		gbc_edit.gridy = 0;
		add(edit, gbc_edit);

		JLabel label = new JLabel("Опис: " + good.getDescription());
		label.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(0, 0, 0, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 1;
		add(label, gbc_label);

		edit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				HelpingWindow window = HelpingWindow.getInstance();
				window.add(new EditGoodPanel(good));
				window.setVisible(true);

			}
		});

		info.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int choice = JOptionPane.showConfirmDialog(null, "Ви впевнені, що хочете видалити цей товар?",
						"Видалення", JOptionPane.OK_CANCEL_OPTION);
				if (choice == JOptionPane.OK_OPTION) {
					// TODO delete
				}

			}
		});
	}

}
