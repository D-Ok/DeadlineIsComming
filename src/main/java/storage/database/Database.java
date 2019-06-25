package storage.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.LinkedList;

import storage.exceptions.InvalidCharacteristicOfGoodsException;

public class Database {

 private Connection connection;
	
	public Database() {
		String user = "root";
    	String password = "root35";
    	String connectionURL = "jdbc:mysql://localhost:3306/storage?serverTimezone=Europe/Kiev&useSSL=false";
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
	    	connection = DriverManager.getConnection(connectionURL, user, password);
	    	creatingTables();
    	}catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void creatingTables() { 
		String goodsTable = "create table if not exists `goods`"
				+ " (`id` INT NOT NULL AUTO_INCREMENT, "
				+ "`name` VARCHAR(45) NOT NULL, "
				+ "`description` VARCHAR(1000) NULL, "
				+ "`producer` VARCHAR(45) NOT NULL, "
				+ "`price` DOUBLE NOT NULL, "
				+ "`quontity` INT NOT NULL, "
				+ "`groupName` VARCHAR(45) NOT NULL, "
				+ "PRIMARY KEY (`id`), "
				+ "UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE, "
				+ "UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE, "
				+ "FOREIGN KEY (`groupName`) REFERENCES `groups`(`name`) "
				+ "ON DELETE CASCADE "
				+ "ON UPDATE CASCADE "
				+ ");";
		
		String groupsTable = "create table if not exists `groups`"
				+ " (`id` INT NOT NULL AUTO_INCREMENT, "
				+ "`name` VARCHAR(45) NOT NULL, "
				+ "`description` VARCHAR(1000) NULL, "
				+ "PRIMARY KEY (`id`, `name`), "
				+ "UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE, "
				+ "UNIQUE INDEX `name_UNIQUE` (`name` ASC) VISIBLE);";
		
		String usersTable = "create table if not exists `users`"
				+ " (`id` INT NOT NULL AUTO_INCREMENT, "
				+ "`login` VARCHAR(45) NOT NULL, "
				+ "`password` VARCHAR(100) NOT NULL, "
				+ "PRIMARY KEY (`id`, `password`), "
				+ "UNIQUE INDEX `id_UNIQUE` (`id` ASC) VISIBLE, "
				+ "UNIQUE INDEX `password_UNIQUE` (`password` ASC) VISIBLE);";
		
		 PreparedStatement st;
		 
		try {
			st = connection.prepareStatement(groupsTable);
			st.executeUpdate();
			
			st = connection.prepareStatement(goodsTable);
	        st.executeUpdate();
	        
	        st = connection.prepareStatement(usersTable);
	        st.executeUpdate();
	        
	        st.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public void createGroup(String nameOfGroup, String description) {
		try {
	    	PreparedStatement ps = connection.prepareStatement("Insert into `groups` (`name`, `description`) values (?, ?)");
	    	ps.setString(1, nameOfGroup);
	    	ps.setString(2,  description);
	    	ps.executeUpdate();
	    	ps.close();
    	} catch(SQLIntegrityConstraintViolationException e) {
    		System.out.println("Group with this name exist.");
    	} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean createGoods(String nameOfGoods, String nameOfGroup, String description, String producer, int quontity, double price) throws InvalidCharacteristicOfGoodsException { 
		if(quontity<0 || price<=0 ) throw new InvalidCharacteristicOfGoodsException("Ucorrect parameters");
		try {
	    	PreparedStatement ps = connection.prepareStatement("Insert into `goods` (`name`, `description`, `price`, `quontity`, `producer`, `groupName`) values (?, ?, ?, ?, ?, ?)");
	    	ps.setString(1, nameOfGoods);
	    	ps.setString(2, description);
	    	ps.setDouble(3, price);
	    	ps.setInt(4, quontity);
	    	ps.setString(5, producer);
	    	ps.setString(6, nameOfGroup);
	    	ps.executeUpdate();
	    	ps.close();
	    	return true;
    	} catch(SQLIntegrityConstraintViolationException e) {
    		System.out.println("Goods with this name exist.");
    		return false;
    	} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	public boolean createGoods(Good g) throws InvalidCharacteristicOfGoodsException {
		return createGoods(g.getName(), g.getGroupName(),g.getDescription(), g.getProducer(), g.getQuontity() , g.getPrice());
	}
	
	public void createUser(String login, String password) {
		try {
	    	PreparedStatement ps = connection.prepareStatement("Insert into `users` (`login`, `password`) values (?, ?)");
	    	ps.setString(1, login);
	    	ps.setString(2, password);
	    	ps.executeUpdate();
	    	ps.close();
    	} catch(SQLIntegrityConstraintViolationException e) {
    		System.out.println("User with this password exist.");
    	} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void deleteGood(String nameOfGood) {
		String str = "delete from goods where name='"+nameOfGood+"'";
		update(str);
	}
	
	public boolean deleteGoodById(int id) {
		String str = "delete from goods where id='"+id+"'";
		return update(str);
	}
	
	public void deleteGroup(String nameOfGroup) {
		String str = "delete from `groups` where `name`='"+nameOfGroup+"'";
		update(str);
	}
	
	public boolean existUser(String login, String password) { 
		String str ="SELECT * FROM users WHERE users.login='"+login+"' AND users.password='"+password+"'";
		Good g = null;
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(str);
	    	if(rs.next()) {
		    	rs.close();
		    	statement.close();
	    		return true;
	    	}
	    	else {
		    	rs.close();
		    	statement.close();
	    		return false;
	    	}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	public Good getGood(String nameOfGood) { 
		String str ="SELECT * FROM goods WHERE goods.name='"+nameOfGood+"'";
		Good g = null;
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(str);
	    	if(rs.next()) {
	    		g = new Good(rs.getString("name"), rs.getString("description"), rs.getString("producer"), rs.getString("groupName"), rs.getDouble("price"), rs.getInt("quontity"));
	    		System.out.println("id = "+rs.getInt("id")+", name : "
	    	+rs.getString("name")+", description: "+rs.getString("description")+", producer: "+rs.getString("producer")
	    	+", price: "+rs.getDouble("price")+", quontity: "+rs.getInt("quontity")+", group: "+rs.getString("groupName"));
	    	}else System.out.println("Good with this name doesn`t exist. ");
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return g;
	}
	
	public Group getGroup(String nameOfGroup) { 
		String str ="SELECT * FROM groups WHERE goods.name='"+nameOfGroup+"'";
		Group g = null;
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(str);
	    	if(rs.next()) 
	    		g=new Group(rs.getString("name"), rs.getString("description"));
	    	else 
	    		System.out.println("Good with this name doesn`t exist. ");
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return g;
	}
	
	public int getGoodId(String nameOfGood) { 
		String str ="SELECT * FROM goods WHERE goods.name='"+nameOfGood+"'";
		Good g = null;
		int id=-1;
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(str);
	    	if(rs.next()) {
	    		id =rs.getInt("id");
	    	}else System.out.println("Good with this name doesn`t exist. ");
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return id;
	}
	
	public Good getGoodById(int id) { 
		String str ="SELECT * FROM goods WHERE goods.id='"+id+"'";
		Good g = null;
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(str);
	    	if(rs.next()) {
	    		g = new Good(rs.getString("name"), rs.getString("description"), rs.getString("producer"), rs.getString("groupName"), rs.getDouble("price"), rs.getInt("quontity"));
	    		g.setId(rs.getInt("id"));
	    		System.out.println("id = "+rs.getInt("id")+", name : "
	    	+rs.getString("name")+", description: "+rs.getString("description")+", producer: "+rs.getString("producer")
	    	+", price: "+rs.getDouble("price")+", quontity: "+rs.getInt("quontity")+", group: "+rs.getString("groupName"));
	    	}else System.out.println("Good with this name doesn`t exist. ");
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return g;
	}

	public int getQuontityOfGoods(String nameOfGood) {
		
		String str ="SELECT * FROM goods WHERE goods.name='"+nameOfGood+"'";
		
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(str);
	    	if(rs.next()) return rs.getInt("quontity");
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public LinkedList<Group> getAllGroups() { 
		String command = "SELECT * FROM storage.groups";
		return getGroups(command);
	}
	
	public LinkedList<Good> getAllGoodsOfTheGroup(String nameOfGroup) {
		String str ="SELECT * FROM goods WHERE goods.groupName='"+nameOfGroup+"'";
		return getGoods(str);
		
	}
	
	public LinkedList<Good> getAllGoods() {
		String str ="SELECT * FROM goods";
		return getGoods(str);
		
	}
	
	public LinkedList<Group> listByGroupNameBeginsWith(String groupName) {
		String command = "SELECT * FROM `groups` WHERE `groups`.`name` LIKE '"+groupName+"%'";
		return getGroups(command);
	}
	
	public LinkedList<Group> listByGroupNameEndsWith(String groupName) {
		String command = "SELECT * FROM `groups` WHERE `name` LIKE '%"+groupName+"'";
		return getGroups(command);
	}
	
	public LinkedList<Group> sortGroupsByNameIncrease() {
		String command = "SELECT * FROM `groups` ORDER BY `groups`.`name`";
		return getGroups(command);
	}
	
	public LinkedList<Group> sortGroupsByNameDecrease() {
		String command = "SELECT * FROM `groups` ORDER BY `groups`.`name` DESC";
		return getGroups(command);
	}
	
	public LinkedList<Good> listByGoodColumnBeginWith(String column, String goodName) {
		String command = "SELECT * FROM goods WHERE goods."+column+" LIKE '"+goodName+"%'";
		return getGoods(command);
	}
	
	public LinkedList<Good> listByGoodColumnEndWith(String column, String goodName) {
		String command = "SELECT * FROM goods WHERE goods."+column+" LIKE '%"+goodName+"'";
		return getGoods(command);
	}

	
	public LinkedList<Good> listByGoodColumnEquals(String column, String producer) {
		String command = "SELECT * FROM goods WHERE goods."+column+"='"+producer+"'";
		return getGoods(command);
	}
	

	/**
	 * Display all goods for which value in column = value
	 * 
	 * @param column
	 * @param value
	 */
	public LinkedList<Good> selectGoodsByCriteria(String column, String value) {
		String sql = "SELECT * FROM `goods` WHERE " + column + "=\"" + value + "\"";
		return getGoods(sql);
	}

	public LinkedList<Good> getSortedGoodsByValue(String column) {
		String sql = "SELECT * FROM goods ORDER BY goods." + column;
		return getGoods(sql);
	}

	public LinkedList<Good> getSortedGoodsByValueDecrease(String column) {
		String sql = "SELECT * FROM goods ORDER BY goods." + column+" DESC";
		return getGoods(sql);
	}
	
	
	

	
	public void addGoods(String nameOfGood, int quontity){
		String command = "SELECT * FROM goods WHERE goods.name='"+nameOfGood+"'";
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    	ResultSet rs = statement.executeQuery(command);
	    	if(rs.next()) {
	    		int old = rs.getInt("quontity");
		    	rs.updateInt("quontity", old+quontity);
		    	rs.updateRow();
	    	}
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public void removeGoods(String nameOfGood, int quontity) throws InvalidCharacteristicOfGoodsException{
		String command = "SELECT * FROM goods WHERE goods.name='"+nameOfGood+"'";
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    	ResultSet rs = statement.executeQuery(command);
	    	if(rs.next()) {
	    		int old = rs.getInt("quontity");
	    		if(old-quontity>=0) {
			    	rs.updateInt("quontity", old-quontity);
			    	rs.updateRow();
	    		} else {
	    			rs.close();
	    			throw new InvalidCharacteristicOfGoodsException("Can`t remove products.");
	    		}
	    	}
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void addGoodsById(int id, int quontity){
		String command = "SELECT * FROM goods WHERE goods.id='"+id+"'";
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    	ResultSet rs = statement.executeQuery(command);
	    	if(rs.next()) {
	    		int old = rs.getInt("quontity");
		    	rs.updateInt("quontity", old+quontity);
		    	rs.updateRow();
	    	}
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removeGoodsById(int id, int quontity) throws InvalidCharacteristicOfGoodsException{
		String command = "SELECT * FROM goods WHERE goods.id='"+id+"'";
		try {
			Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    	ResultSet rs = statement.executeQuery(command);
	    	if(rs.next()) {
	    		int old = rs.getInt("quontity");
	    		if(old-quontity>=0) {
			    	rs.updateInt("quontity", old-quontity);
			    	rs.updateRow();
	    		} else {
	    			rs.close();
	    			throw new InvalidCharacteristicOfGoodsException("Can`t remove products.");
	    		}
	    	}
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Update information about group in table
	 * 
	 * @param name
	 * @param description
	 */
	public boolean updateGroup(String name, String newName, String description) {
		String sql = "UPDATE `group` SET `name`='"+newName+"', `description`='"+description+"' WHERE `name`='"+name+"'";
		return update(sql);
	}
	

	/**
	 * Update information about good in table
	 * 
	 * @param name
	 * @param description
	 * @param producer
	 * @param amount
	 * @param price
	 * @throws InvalidCharacteristicOfGoodsException 
	 */
	public boolean updateGood(int id, String newName, String description, String producer, double price) throws InvalidCharacteristicOfGoodsException {
		
        if(price<=0) throw new InvalidCharacteristicOfGoodsException();
        
		String sql = "UPDATE `goods` SET `name`='"+newName+"', `description`='"+description+"', `producer`='"+producer+"',"
				+ " `price`='"+price+"' WHERE `id`='"+id+"'";
		return update(sql);
	}
	
	private boolean update(String command) { 
		try {
			Statement st = connection.createStatement();
			st.executeUpdate(command);
			st.close();
			return true;
		} catch (SQLException e) {
			System.out.println("Uncorrect data.");
			return false;
		}
	}
	
	private LinkedList<Good> getGoods(String command) { 
		LinkedList<Good> result = new LinkedList<Good>();
		
		try {
			Statement statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(command);
	    	while(rs.next()) {
	    		result.add(new Good(rs.getString("name"), rs.getString("description"), rs.getString("producer"), rs.getString("groupName"), rs.getDouble("price"), rs.getInt("quontity")));
	    		System.out.println(rs.getRow()+". "+"id = "+rs.getInt("id")+", name : "
	    	+rs.getString("name")+", description: "+rs.getString("description")+", producer: "+rs.getString("producer")
	    	+", price: "+rs.getDouble("price")+", quontity: "+rs.getInt("quontity")+", group: "+rs.getString("groupName"));
	    	}
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	private LinkedList<Group> getGroups(String command) {
		LinkedList<Group> result = new LinkedList<Group>();
		Statement statement;
		try {
			statement = connection.createStatement();
	    	ResultSet rs = statement.executeQuery(command);
	    	while(rs.next()) {
	    		result.add(new Group(rs.getString("name"), rs.getString("description")));
	    		System.out.println(rs.getRow()+". "+"id = "+rs.getInt("id")+", name : "+rs.getString("name")
	    		+", description: "+rs.getString("description"));
	    	}
	    	rs.close();
	    	statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}


}
