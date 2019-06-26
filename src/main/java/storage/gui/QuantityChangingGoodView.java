package storage.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import storage.database.Good;

public class QuantityChangingGoodView extends JPanel {

	/**
	 * Create the panel.
	 */
	public QuantityChangingGoodView(Good good) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

		JLabel lblNewLabel = new JLabel(good.getName());
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Кількість: " + good.getQuontity());
		lblNewLabel_1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(lblNewLabel_1);

		JButton change = new JButton("Списати/зарахувати");
		change.setForeground(Color.WHITE);
		change.setBackground(new Color(0, 204, 153));
		change.setFocusable(false);
		change.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(change);

		change.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

	}

}
