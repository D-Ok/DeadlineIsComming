package storage;

import java.io.IOException;
import java.util.LinkedList; 

import org.junit.Assert;
import org.junit.Test;

import storage.database.Database;
import storage.database.Good;
import storage.database.Group;
import storage.exceptions.InvalidCharacteristicOfGoodsException;
import storage.network.ClientHttp;
import storage.network.ServerHttp;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
	
	public AppTest() {
		try {
			ServerHttp s = new ServerHttp();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test 
	public void testDatabaseCreating() {
		Database db = new Database();
		String groupName = "UnitTest";
		String goodName = "Unit";
		db.deleteGroup(groupName);
		db.createGroup(groupName, "description");
		try {
			db.createGoods(goodName, groupName, "description" , "smb", 600, 20.2);

			LinkedList<Group> l = db.getAllGroups();
			Assert.assertTrue(consistGroup(l, groupName));
			
			LinkedList<Good> list = db.getAllGoods();
			Assert.assertTrue(consistGood(list, goodName));
			
		} catch (InvalidCharacteristicOfGoodsException e) {
			e.printStackTrace();
		}
		db.deleteGroup(groupName);
	}
	
	
	@Test 
	public void testDatabaseDeleting() {
		Database db = new Database();
		String groupName = "UnitTest";
		String goodName = "Unit";
		db.deleteGroup(groupName);
		db.createGroup(groupName, "description");
		try {
			db.createGoods(goodName, groupName, "description" , "smb", 600, 20.2);
		} catch (InvalidCharacteristicOfGoodsException e) {
			e.printStackTrace();
		}
		
		db.deleteGroup(groupName);
		LinkedList<Group> l = db.getAllGroups();
		Assert.assertFalse(consistGroup(l, groupName));
		LinkedList<Good> list = db.getAllGoods();
		Assert.assertFalse(consistGood(list, goodName));

		
	}
	
	private boolean consistGroup(LinkedList<Group> l, String groupName) {
		for(Group g: l) {
			if(g.getName().equals(groupName)) return true;
		}
		return false;
	}
	
	private boolean consistGood(LinkedList<Good> l, String goodName) {
		for(Good g: l) {
			if(g.getName().equals(goodName)) return true;
		}
		
		return false;
	}
	
	@Test
	public void testDatabaseUpdating() {
		Database db = new Database();
		String groupName = "UnitTest";
		String goodName = "Unit";
		db.deleteGroup(groupName);
		db.createGroup(groupName, "description");
		try {
			db.createGoods(goodName, groupName, "description" , "smb", 600, 20.2);
			String description = "des";
			String producer = "prod";
			double price =33.3;
			db.updateGood(db.getGoodId(goodName), goodName, description, producer, price);
			
			Good g = db.getGood(goodName);
			
			Assert.assertEquals(description, g.getDescription());
			Assert.assertEquals(producer, g.getProducer());
			Assert.assertTrue(price == g.getPrice());
		} catch (InvalidCharacteristicOfGoodsException e) {
			e.printStackTrace();
		}
		
		db.deleteGroup(groupName);
	}
	
	@Test
	public void testDatabaseAdditionAndRemoving() {
		Database db = new Database();
		String groupName = "UnitTest";
		String goodName = "Unit";
		db.deleteGroup(groupName);
		db.createGroup(groupName, "description");
		try {
			db.createGoods(goodName, groupName, "description" , "smb", 0, 20.2);
			db.addGoods(goodName, 50);
			Assert.assertEquals(50, db.getQuontityOfGoods(goodName));
			db.removeGoods(goodName, 50);
			Assert.assertEquals(0, db.getQuontityOfGoods(goodName));
			db.removeGoods(goodName, 50);
			Assert.assertEquals(0, db.getQuontityOfGoods(goodName));

		} catch (InvalidCharacteristicOfGoodsException e) {
			e.printStackTrace();
		}	
		
		db.deleteGroup(groupName);
	}
	
	@Test
	public void testClientLogin() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			String token = cli.login();
			Assert.assertNotNull(token);
			
			cli = new ClientHttp("fghj", "fghj");
			token = cli.login();
			Assert.assertNull(token);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testClientCreateingAndDeletingGroup() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			Group gr = new Group("unitGroup19", "desc11");
			int id = cli.createGroup(gr);
			Assert.assertNotEquals(-1, id);
			
			Group g = cli.getGroup(id);
			Assert.assertEquals("unitGroup19", g.getName());
			
			boolean deleted = cli.deleteGroup(id);
			Assert.assertTrue(deleted);
			
			Group n = cli.getGroup(id);
			Assert.assertNull(n);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testClientCreateingGroup() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			Group gr = new Group("unitGroup00", "desc00");
			int id = cli.createGroup(gr);
			Assert.assertNotEquals(-1, id);
			
			int i = cli.createGroup(gr);
			Assert.assertEquals(-1, i);
			
			boolean deleted = cli.deleteGroup(id);
			Assert.assertTrue(deleted);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testClientDeletingGroup() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			Group gr = new Group("unitGroupds", "descds");
			int id = cli.createGroup(gr);
			Assert.assertNotEquals(-1, id);
			
			boolean deleted = cli.deleteGroup(id);
			Assert.assertTrue(deleted);
			
			deleted = cli.deleteGroup(id);
			Assert.assertFalse(deleted);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testClientUpdatingGroup() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			Group gr = new Group("unitGroupdn", "descdn");
			int id = cli.createGroup(gr);
			Assert.assertNotEquals(-1, id);
			
			Group refreshed = new Group(id, "refreshed", "refr");
			boolean r = cli.changeGroup(refreshed);
			Assert.assertTrue(r);
			
			boolean deleted = cli.deleteGroup(id);
			Assert.assertTrue(deleted);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	@Test
	public void testClientgetAllGroup() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			LinkedList<Group> res = cli.getAllGroups();
			Assert.assertNotNull(res);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testClientgetGroups() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			int id1 = cli.createGroup(new Group("uniGr1", "unitSesc"));
			int id2 = cli.createGroup(new Group("unitGr2", "unitSesc"));
			int id3 = cli.createGroup(new Group("unitGr3", "unitSesc"));
			int id4 = cli.createGroup(new Group("unitGr4", "unitSesc"));
			int id5 = cli.createGroup(new Group("unitGr5", "unitSesc"));
			
			LinkedList<Group> res = cli.getSearchGroupsByDescription("nitS");
		//	System.out.println(res.size());
			Assert.assertEquals(5, res.size());
			
			cli.deleteGroup(id5);
			cli.deleteGroup(id4);
			cli.deleteGroup(id3);
			cli.deleteGroup(id2);
			cli.deleteGroup(id1);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	@Test
	public void testClientCreateGood() {
		Database db = new Database();
		String login = "login";
		String password = "password";
		ClientHttp cli = new ClientHttp(login, password);
		try {
			
			String token = cli.login();
			int idGr = cli.createGroup(new Group("UnitGr", "descr"));
			int idG = cli.createGood(new Good("UnitGood", "descr", "pr", "UnitGr", 30.9, 500));
			
			Assert.assertNotEquals(-1, idG);
			
			int idSame = cli.createGood(new Good("UnitGood", "descr", "pr", "UnitGr", 30.9, 500));
			Assert.assertEquals(-1, idSame);
			
			cli.deleteGood(idG);
			cli.deleteGroup(idGr);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	
}
