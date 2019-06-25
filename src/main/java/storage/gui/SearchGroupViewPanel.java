package storage.gui;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import storage.database.Group;

/**
 * 
 * @author Masha. Class that displays single group view.
 *
 */
public class SearchGroupViewPanel extends JPanel {

	/**
	 * Create the panel.
	 */
	public SearchGroupViewPanel(Group group) {
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 5));

		JLabel lblNewLabel = new JLabel(group.getName());
		lblNewLabel.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Опис: " + group.getDescription());
		lblNewLabel_1.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(lblNewLabel_1);

		JButton edit = new JButton("Редагувати");

		edit.setForeground(new Color(255, 255, 255));
		edit.setBackground(new Color(0, 204, 153));
		edit.setFocusable(false);
		edit.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(edit);

		JButton delete = new JButton("Видалити");
		delete.setForeground(new Color(255, 255, 255));
		delete.setBackground(new Color(0, 204, 153));
		delete.setFocusable(false);
		delete.setFont(new Font("Bookman Old Style", Font.PLAIN, 14));
		add(delete);

	}

}
