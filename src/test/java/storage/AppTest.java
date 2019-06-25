package storage;

import java.util.LinkedList; 

import org.junit.Assert;
import org.junit.Test;

import storage.database.Database;
import storage.database.Good;
import storage.database.Group;
import storage.exceptions.InvalidCharacteristicOfGoodsException;


/**
 * Unit test for simple App.
 */
public class AppTest 
{
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
			db.createGoods(goodName, groupName, "description" , "smb", 600, 20.2);
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
	
	
}
