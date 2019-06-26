package storage.gui;

import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import storage.database.Good;
import storage.database.Group;
import storage.network.ClientHttp;

public class Client {
	private static ClientHttp httpClient;

	public static void main(String[] args) {
		Client client = new Client();
		ClientWindowManager.showLoginWindow();
	}

	public static void sendRegistrationRequest(String login, String password) {
		httpClient = new ClientHttp(login, password);
		String token = null;
		try {
			token = httpClient.regirnration();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Помилка передачі даних", "Помилка регістрації",
					JOptionPane.ERROR_MESSAGE);
		}

		if (token != null) {
			ClientWindowManager.closeLoginWindow();
			ClientWindowManager.showApplicationWindow();
		} else {
			JOptionPane.showMessageDialog(null,
					"Користувач з таким логіном вже існує або дані введені в некоректному форматі",
					"Помилка регістрації", JOptionPane.ERROR_MESSAGE);
		}

	}

	public static void sendLoginRequest(String login, String password) {
		httpClient = new ClientHttp(login, password);
		String token = null;
		try {
			token = httpClient.login();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Помилка передачі даних", "Помилка регістрації",
					JOptionPane.ERROR_MESSAGE);
		}

		if (token != null) {
			ClientWindowManager.closeLoginWindow();
			ClientWindowManager.showApplicationWindow();
		} else {
			JOptionPane.showMessageDialog(null,
					"Користувач з таким логіном вже існує або дані введені в некоректному форматі",
					"Помилка регістрації", JOptionPane.ERROR_MESSAGE);
		}
	}

	public static LinkedList<Group> getAllGroups() {

		LinkedList<Group> list = null;
		try {
			list = httpClient.getAllGroups();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Помилка передачі даних", "Помилка регістрації",
					JOptionPane.ERROR_MESSAGE);
		}
		if (list == null)
			JOptionPane.showMessageDialog(null, "Помилка обробки даних", "Помилка", JOptionPane.ERROR_MESSAGE);
		return list;
	}

	public static void sendAddGoodRequest(Good g) {
		int returned = -1;
		try {
			returned = httpClient.createGood(g);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Помилка передачі даних", "Помилка регістрації",
					JOptionPane.ERROR_MESSAGE);
		}
		if (returned == -1) {
			JOptionPane.showMessageDialog(null, "Некоректні дані для створення товару", "Помилка",
					JOptionPane.ERROR_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, "Товар успішно додано", "", JOptionPane.CLOSED_OPTION);
	}

	public static void sendAddGroupRequest(Group g) {
		int returned = -1;
		try {
			returned = httpClient.createGroup(g);
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Помилка передачі даних", "Помилка регістрації",
					JOptionPane.ERROR_MESSAGE);
		}
		if (returned == -1) {
			JOptionPane.showMessageDialog(null, "Некоректні дані для створення групи", "Помилка",
					JOptionPane.ERROR_MESSAGE);
		} else
			JOptionPane.showMessageDialog(null, "Групу успішно додано", "", JOptionPane.CLOSED_OPTION);
	}

	public static LinkedList<Good> sendGoodSearchRequest(String query, String criteria) {
		LinkedList<Good> results = new LinkedList<>();
		switch (criteria) {
		case "Ім'я":
			try {
				results = httpClient.getSearchGoodsByName(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "Опис":
			try {
				results = httpClient.getSearchGoodsByDescription(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "Група":
			try {
				results = httpClient.getSearchGoodsByGroup(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case "Виробник":
			try {
				results = httpClient.getSearchGoodsByProducer(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
		return results;
	}

	public static LinkedList<Group> sendGroupSearchRequest(String query, String criteria) {
		LinkedList<Group> results = new LinkedList<>();
		if (criteria.equals("Ім'я"))
			try {
				results = httpClient.getSearchGroupByName(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		else
			try {
				results = httpClient.getSearchGroupsByDescription(query);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return results;
	}

	public static void sendEditGroupRequest(Group g) {
		boolean changed = false;
		try {
			changed = httpClient.changeGroup(g);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (changed)
			JOptionPane.showMessageDialog(null, "Групу успішно змінено", "", JOptionPane.CLOSED_OPTION);
		else
			JOptionPane.showMessageDialog(null, "Некоректні дані для групи", "Помилка", JOptionPane.ERROR_MESSAGE);
	}

	public static void sendEditGoodRequest(Good g) {
		boolean changed = false;
		try {
			changed = httpClient.change(g);
		} catch (IOException e) {
			// TODO Auto-generated catch
			e.printStackTrace();
		}
		if (changed)
			JOptionPane.showMessageDialog(null, "Товар успішно змінено", "", JOptionPane.CLOSED_OPTION);
		else
			JOptionPane.showMessageDialog(null, "Некоректні дані для товару", "Помилка", JOptionPane.ERROR_MESSAGE);
	}

	public static void sendDeleteGoodRequest(int id) {
		boolean deleted = false;
		try {
			deleted = httpClient.deleteGood(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (deleted)
			JOptionPane.showMessageDialog(null, "Товар успішно видалено", "", JOptionPane.CLOSED_OPTION);
	}

	public static void sendDeleteGroupRequest(int id) {
		boolean deleted = false;
		try {
			deleted = httpClient.deleteGroup(id);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (deleted)
			JOptionPane.showMessageDialog(null, "Групу успішно видалено", "", JOptionPane.CLOSED_OPTION);
	}

}
