package storage.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import storage.database.Good;
import storage.database.Group;

public class ClientHttp implements Runnable {

	private final OkHttpClient client;
	public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
	private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private final Random random = new Random();
	private final String login, password;
	private static String[] groups = { "groats", "dairy" };
	private final PrivateKey privateKey;
	private Key key;
	private final String publicKey;
	private String token;

	private void receiveServerKey(byte[] received) {

		Cipher cipher;

		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decrypted = cipher.doFinal(received);
			key = new SecretKeySpec(decrypted, "AES");
			System.out.println("Received key from client");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException
				| BadPaddingException e) {
			e.printStackTrace();
		}

	}

	public ClientHttp(String login, String password) {
		client = new OkHttpClient.Builder().retryOnConnectionFailure(true).build();
		this.login = login;
		this.password = password;

		KeyPairGenerator gen = null;
		try {
			gen = KeyPairGenerator.getInstance("RSA");
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Key generator exception");
		}
		gen.initialize(512, new SecureRandom());
		KeyPair pair = gen.generateKeyPair();
		PublicKey publKey = pair.getPublic();

		publicKey = Base64.encodeBase64URLSafeString(publKey.getEncoded());
		privateKey = pair.getPrivate();
	}

	public void startWork() {
		String token;
		try {
			token = login();
			if (token != null && token.length() > 0) {
				while (true) {
					int type = random.nextInt(6);
					switch (type) {
					case 0:
						getGood(random.nextInt(501));
						break;
					case 1:
						deleteGood(random.nextInt(501));
						break;
					case 2:
						Good g = new Good("name" + random.nextInt(100), "description", "tester",
								groups[random.nextInt(2)], random.nextDouble() * 100, random.nextInt(1000));
						createGood(g);
						break;
					case 3:
						// Good goodToChange =
						// change(token, random.nextInt(500), random.nextDouble()*100, null, null, null,
						// null);
						break;
					case 4:
						addGood(random.nextInt(500), random.nextInt(1000));
						break;
					case 5:
						removeGood(random.nextInt(500), random.nextInt(1000));
						break;
					default:
						break;
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private byte[] encryptData(String data) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			byte[] d = data.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			return cipher.doFinal(d);
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | UnsupportedEncodingException
				| NoSuchAlgorithmException | NoSuchPaddingException e) {
			return null;
		}
	}

	private String decryptData(String data) {
		try {
			System.out.println(data);
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, key);
			byte[] decrypted = cipher.doFinal(Base64.decodeBase64(data));
			String res = new String(decrypted);
			return res;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException
				| NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String login() throws IOException {

		System.out.println("\n" + Thread.currentThread() + "try to login login = " + login);
		String MD5Password = ServerHttp.getMD5EncryptedValue(password);
		Request request = new Request.Builder().addHeader("PublicKey", publicKey)
				.url("http://localhost:8765/api/login?login=" + login + "&password=" + MD5Password).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on login: " + responseCode + " " + response.message());

		if (responseCode == 200) {
			JsonObject jo = (JsonObject) GSON.fromJson(response.body().string(), JsonElement.class);
			if (jo.has("token")) {
				token = jo.get("token").getAsString();
				System.out.println(Thread.currentThread() + " token = " + token);
			}
			if (jo.has("key"))
				System.out.println(jo.get("key").getAsString());
			receiveServerKey(DatatypeConverter.parseHexBinary(jo.get("key").getAsString()));

		}
		return token;
	}

	public String regirnration() throws IOException {

		System.out.println("\n" + Thread.currentThread() + "try to login registration = " + login);
		String MD5Password = ServerHttp.getMD5EncryptedValue(password);
		Request request = new Request.Builder().addHeader("PublicKey", publicKey)
				.url("http://localhost:8765/api/registration?login=" + login + "&password=" + MD5Password).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(
				Thread.currentThread() + " answer on registration: " + responseCode + " " + response.message());

		if (responseCode == 200) {
			JsonObject jo = (JsonObject) GSON.fromJson(response.body().string(), JsonElement.class);
			if (jo.has("token")) {
				token = jo.get("token").getAsString();
				System.out.println(Thread.currentThread() + " token = " + token);
			}
			if (jo.has("key"))
				System.out.println(jo.get("key").getAsString());
			receiveServerKey(DatatypeConverter.parseHexBinary(jo.get("key").getAsString()));

		}
		return token;
	}

	public Group getGroup(int id) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to get group with id = " + id);

		Request request = new Request.Builder().get().addHeader("Authorization", token)
				.url("http://localhost:8765/api/group/" + id).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on get group with id " + id + " : " + responseCode + " "
				+ response.message());

		if (responseCode == 200) {
			Group g = GSON.fromJson(decryptData(response.body().string()), Group.class);
			System.out.println(Thread.currentThread() + " good with id " + id + " : " + g.toString());
			return g;
		} else
			return null;
	}

	public LinkedList<Good> getAllGoods() throws IOException {

		Request request = new Request.Builder().get().addHeader("Authorization", token)
				.url("http://localhost:8765/api/goods/all").build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(
				Thread.currentThread() + " answer on getAllGoods : " + responseCode + " " + response.message());

		if (responseCode == 200) {
			JsonArray g = GSON.fromJson(decryptData(response.body().string()), JsonArray.class);
			LinkedList<Good> res = new LinkedList<Good>();
			for (int i = 0; i < g.size(); i++) {
				res.add(GSON.fromJson(g.get(i).getAsString(), Good.class));
			}
			return res;
		} else
			return null;

	}

	public Good getGood(int id) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to get good with id = " + id);

		Request request = new Request.Builder().get().addHeader("Authorization", token)
				.url("http://localhost:8765/api/good/" + id).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on get good with id " + id + " : " + responseCode + " "
				+ response.message());

		if (responseCode == 200) {
			Good good = GSON.fromJson(decryptData(response.body().string()), Good.class);
			System.out.println(Thread.currentThread() + " good with id " + id + " : " + good.toString());
			return good;
		} else
			return null;

	}

	public boolean deleteGroup(int id) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to delete group with id = " + id);

		Request request = new Request.Builder()
				// .addHeader("Connection","close")
				.addHeader("Authorization", token).delete().url("http://localhost:8765/api/group/" + id).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on deleting group with id " + id + " : " + responseCode
				+ " " + response.message());

		if (responseCode == 204) {
			System.out.println("deleted");
			return true;
		} else
			return false;

	}

	public boolean deleteGood(int id) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to delete good with id = " + id);

		Request request = new Request.Builder()
				// .addHeader("Connection","close")
				.addHeader("Authorization", token).delete().url("http://localhost:8765/api/good/" + id).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on deleting good with id " + id + " : " + responseCode
				+ " " + response.message());

		if (responseCode == 204) {
			System.out.println("deleted");
			return true;
		} else
			return false;

	}

	public int createGroup(Group g) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to create group");

		Request request = new Request.Builder().addHeader("Authorization", token).url("http://localhost:8765/api/group")
				.put(RequestBody.create(JSON, encryptData(GSON.toJson(g)))).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(
				Thread.currentThread() + " answer on creating good : " + responseCode + " " + response.message());

		if (responseCode == 201) {
			JsonObject jo = (JsonObject) GSON.fromJson(decryptData(response.body().string()), JsonElement.class);
			int id = -1;
			if (jo.has("id"))
				id = jo.get("id").getAsInt();
			System.out.println(Thread.currentThread() + " created id = " + id);

			return id;
		} else
			return -1;

	}

	public int createGood(Good g) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to create good ");

		Request request = new Request.Builder().addHeader("Authorization", token).url("http://localhost:8765/api/good")
				.put(RequestBody.create(JSON, encryptData(GSON.toJson(g)))).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(
				Thread.currentThread() + " answer on creating good : " + responseCode + " " + response.message());

		if (responseCode == 201) {
			JsonObject jo = (JsonObject) GSON.fromJson(decryptData(response.body().string()), JsonElement.class);
			int id = -1;
			if (jo.has("id"))
				id = jo.get("id").getAsInt();
			System.out.println(Thread.currentThread() + " created id = " + id);

			return id;
		} else
			return -1;

	}

	public boolean changeGroup(Group group) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to change group with id = " + group.getId());

		JsonObject jo = new JsonObject();
		String jsonGood = GSON.toJson(group);
		jo.addProperty("group", jsonGood);

		RequestBody body = RequestBody.create(JSON, encryptData(GSON.toJson(jo)));
		Request request = new Request.Builder().addHeader("Authorization", token).url("http://localhost:8765/api/group")
				.post(body).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on changing group with id " + group.getId() + " : "
				+ responseCode + " " + response.message());

		if (responseCode == 204)
			return true;
		else
			return false;
	}

	public boolean change(Good good) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to change good with id = " + good.getId());

		JsonObject jo = new JsonObject();
		String jsonGood = GSON.toJson(good);
		jo.addProperty("good", jsonGood);

		RequestBody body = RequestBody.create(JSON, encryptData(GSON.toJson(jo)));
		return sendPostRequest(body, good.getId());
	}

	public boolean addGood(int id, int quantity) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to add " + quantity + " goods with id = " + id);

		JsonObject jo = new JsonObject();
		jo.addProperty("id", id);
		jo.addProperty("addGood", quantity);

		RequestBody body = RequestBody.create(JSON, encryptData(GSON.toJson(jo)));
		return sendPostRequest(body, id);
	}

	public boolean removeGood(int id, int quantity) throws IOException {
		System.out.println("\n" + Thread.currentThread() + "try to remove " + quantity + " goods with id = " + id);

		JsonObject jo = new JsonObject();
		jo.addProperty("id", id);
		jo.addProperty("removeGood", quantity);

		RequestBody body = RequestBody.create(JSON, encryptData(GSON.toJson(jo)));
		return sendPostRequest(body, id);
	}

	private boolean sendPostRequest(RequestBody body, int id) throws IOException {
		Request request = new Request.Builder().addHeader("Authorization", token).url("http://localhost:8765/api/good")
				.post(body).build();

		Response response = client.newCall(request).execute();
		int responseCode = response.code();
		System.out.println(Thread.currentThread() + " answer on changing good with id " + id + " : " + responseCode
				+ " " + response.message());

		if (responseCode == 204)
			return true;
		else
			return false;
	}

	static public void main(String args[]) throws Exception {
		HashMap<String, String> users = new HashMap<String, String>();
		users.put("login1", "password1");
		users.put("Kate", "12345");

//    for(int i=0; i<10; i++) {
//      Thread cli;
//      
//      if(i%3==0) 
//        cli = new Thread(new ClientHttp("login", "wrong"));
//      else if(i%3 == 1) 
//        cli = new Thread(new ClientHttp("login", users.get("login")));
//      else 
//        cli = new Thread(new ClientHttp("Kate", users.get("Kate")));
//      
//      cli.start();
//    }

		ClientHttp cli = new ClientHttp("login2", "password2");
		// String token = cli.login();
		cli.login();
//        System.out.println(token);
//        Good goodToChange = new Good(1, "milkk", "milk product", "Kyiv", 25.9);
//        cli.change(goodToChange, token);
//        
//        cli.getGood(1, token);
//        
//        Good goodToCreate = new Good("name", "description", "producer", "dairy", 30, 100);
//        cli.createGood(goodToCreate, token);
//        
//        cli.deleteGood(1, token);
//        
//        cli.getGood(1, token);

//        int id = cli.createGroup(new Group("name", "description"));
//      //  id=113;
//        cli.getGroup(id);
//        cli.changeGroup(new Group(id, "name1", "desc1"));
//        cli.getGroup(id);
//        cli.deleteGroup(id);

		LinkedList<Good> l = cli.getAllGoods();
		for (Good g : l) {
			System.out.println(g);
		}

	}

	@Override
	public void run() {

	}
}
