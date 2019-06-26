package storage.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class AuthenticationWindow extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField passwordField;
	private JButton btnSignIn;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AuthenticationWindow frame = new AuthenticationWindow();
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
	public AuthenticationWindow() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 20, 950, 700);
		this.setResizable(false);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(0, 128, 96));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[] { 370, 0 };
		gbl_contentPane.rowHeights = new int[] { 0, 0, 0 };
		gbl_contentPane.columnWeights = new double[] { 0.0, 1.0 };
		gbl_contentPane.rowWeights = new double[] { 1.0, 0.5, 1.0 };
		contentPane.setLayout(gbl_contentPane);

		JLabel lblLogin = new JLabel("Login:");
		lblLogin.setForeground(new Color(255, 255, 255));
		lblLogin.setFont(new Font("Bookman Old Style", Font.PLAIN, 20));
		GridBagConstraints gbc_lblLogin = new GridBagConstraints();
		gbc_lblLogin.insets = new Insets(0, 0, 10, 5);
		gbc_lblLogin.anchor = GridBagConstraints.SOUTHEAST;
		gbc_lblLogin.gridx = 0;
		gbc_lblLogin.gridy = 0;
		contentPane.add(lblLogin, gbc_lblLogin);

		textField = new JTextField();
		textField.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 10, 0);
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 0;
		gbc_textField.anchor = GridBagConstraints.SOUTHWEST;
		contentPane.add(textField, gbc_textField);
		textField.setColumns(20);

		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setForeground(new Color(255, 255, 255));
		lblPassword.setFont(new Font("Bookman Old Style", Font.PLAIN, 20));
		GridBagConstraints gbc_lblPassword = new GridBagConstraints();
		gbc_lblPassword.insets = new Insets(10, 0, 5, 5);
		gbc_lblPassword.gridx = 0;
		gbc_lblPassword.gridy = 1;
		gbc_lblPassword.anchor = GridBagConstraints.NORTHEAST;
		contentPane.add(lblPassword, gbc_lblPassword);

		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Bookman Old Style", Font.PLAIN, 16));
		GridBagConstraints gbc_passwordField = new GridBagConstraints();
		gbc_passwordField.insets = new Insets(10, 0, 5, 0);
		gbc_passwordField.gridx = 1;
		gbc_passwordField.gridy = 1;
		gbc_passwordField.anchor = GridBagConstraints.NORTHWEST;
		passwordField.setColumns(20);
		contentPane.add(passwordField, gbc_passwordField);

		btnSignIn = new JButton("Sign in");
		btnSignIn.setForeground(new Color(255, 255, 255));
		btnSignIn.setFont(new Font("Bookman Old Style", Font.PLAIN, 20));
		btnSignIn.setBackground(new Color(0, 204, 153));
		btnSignIn.setFocusable(false);
		GridBagConstraints gbc_btnSignIn = new GridBagConstraints();
		gbc_btnSignIn.insets = new Insets(0, 0, 0, 5);
		gbc_btnSignIn.gridx = 0;
		gbc_btnSignIn.gridy = 2;
		gbc_btnSignIn.anchor = GridBagConstraints.NORTHEAST;
		contentPane.add(btnSignIn, gbc_btnSignIn);

		btnNewButton = new JButton("Sign up");
		btnNewButton.setVerticalAlignment(SwingConstants.TOP);
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(0, 204, 153));
		btnNewButton.setFocusable(false);
		btnNewButton.setFont(new Font("Bookman Old Style", Font.PLAIN, 20));
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnNewButton.insets = new Insets(0, 200, 0, 0);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 2;
		contentPane.add(btnNewButton, gbc_btnNewButton);

		btnSignIn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Client.sendLoginRequest(textField.getText(), new String(passwordField.getPassword()));
			}
		});

		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Client.sendRegistrationRequest(textField.getText(), new String(passwordField.getPassword()));
			}
		});

	}
}
