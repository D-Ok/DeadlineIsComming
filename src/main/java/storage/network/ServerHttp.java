package storage.network;

import com.google.gson.Gson; 
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.*;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;

import storage.database.Database;
import storage.database.Good;
import storage.database.Group;
import storage.exceptions.InvalidCharacteristicOfGoodsException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.binary.Base64;

public class ServerHttp {

	private static final SecretKey key = MacProvider.generateKey(SignatureAlgorithm.HS256);
	private static final Database db = new Database();
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private volatile static int unicNumber = 0;
	private static Cipher cipher ;
//	private static CopyOnWriteArraySet<String> tokens = new CopyOnWriteArraySet<String>();
	private static ConcurrentHashMap<String, Key> tokenAndKey = new ConcurrentHashMap<String, Key>();
	
	public ServerHttp() throws IOException {
		
		HttpServer server = HttpServer.create();
		server.bind(new InetSocketAddress(8765), 0);

		HttpContext context = server.createContext("/api/login", new LoginHandler());
		
		HttpContext registration = server.createContext("/api/registration", new RegistrationHandler());
		
		HttpContext context1 = server.createContext("/api/group", new GroupHandler());
		context1.setAuthenticator(new Auth());

		HttpContext context2 = server.createContext("/api/good", new GoodHandler());
		context2.setAuthenticator(new Auth());
		
		HttpContext context3 = server.createContext("/api/groups", new GroupListHandler());
		context1.setAuthenticator(new Auth());

		HttpContext context4 = server.createContext("/api/goods", new GoodListHandler());
		context2.setAuthenticator(new Auth());

		server.setExecutor(Executors.newFixedThreadPool(5));
		server.start();
	}

	public static void main(String[] args) throws Exception {

		db.createGroup("dairy", "products with milk");
		db.createGroup("groats", "description");
		db.createUser("login", getMD5EncryptedValue("password"));
		db.createUser("Kate", getMD5EncryptedValue("12345"));
		
		db.createGoods("milk", "dairy", "milk product", "Kyiv", 600, 23.9);
		db.createGoods("cheese", "dairy", "milk product", "Poltava", 780, 44);
		db.createGoods("butter", "dairy", "milk product", "Chernihiv", 450, 43.5);

		db.createGoods("buckwheat", "groats", "description", "Kyiv", 1000, 25.5);
		db.createGoods("fig", "groats", "description", "Chernihiv", 1500, 33);
		db.createGoods("bulgur", "groats", "description", "Kyiv", 800, 40);
		
		ServerHttp sh = new ServerHttp();

	}

	// Sample method to construct a JWT
	private static String createJWT(String id, String subject) {

		// The JWT signature algorithm we will be using to sign the token
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		// Let's set the JWT Claims
		long expMillis = nowMillis + 1800000;
		Date exp = new Date(expMillis);

		return Jwts.builder().setId(id).setIssuedAt(now).setSubject(subject).setIssuer("server").setExpiration(exp)
				.signWith(signatureAlgorithm, key).compact();
	}

	// Sample method to validate and read the JWT
	private static void parseJWT(String jwt) {

		// This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
		System.out.println("ID: " + claims.getId());
		System.out.println("Subject: " + claims.getSubject());
		System.out.println("Issuer: " + claims.getIssuer());
		System.out.println("Expiration: " + claims.getExpiration());
	}

	// Sample method to validate and read the JWT
	private static boolean isAlive(String jwt) {

		// This line will throw an exception if it is not a signed JWS (as expected)
		if(tokenAndKey.containsKey(jwt)) {
			try {
				Claims claims = Jwts.parser().setSigningKey(key).parseClaimsJws(jwt).getBody();
				long nowMillis = System.currentTimeMillis();
				Date now = new Date(nowMillis);
				if (claims.getExpiration().after(now))
					return true;
				else {
					System.out.println("here");
					tokenAndKey.remove(jwt);
					return false;
				}
			} catch (Exception e) {
				System.out.println("here1");
				return false;
			}
		} else {
			System.out.println("here2");
			return false;
		}
	}

	static class LoginHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {

			if ("GET".equals(exchange.getRequestMethod())) {

				String p = exchange.getRequestURI().getRawQuery();
				System.out.println(p);
				Map<String, String> params = new HashMap<String, String>();

				for (String str : p.split("&")) {
					String[] pair = str.split("=");
					params.put(pair[0], pair[1]);
				}

				String login = "", password = "";
				if (params.containsKey("login"))
					login = params.get("login");
				if (params.containsKey("password"))
					password = params.get("password");
				
				Headers h = exchange.getRequestHeaders();
				byte[] publicKey = Base64.decodeBase64(h.getFirst("PublicKey"));
				
				System.out.println("GET: login = " + login + " password = " + password);
				if (db.existUser(login, password)) {
					String token = createJWT(" " + unicNumber, login);
					unicNumber++;
					byte[] key =  ServerHttp.generateKey(publicKey, token);
					
					JsonObject jo = new JsonObject();
					jo.addProperty("token", token);
					jo.addProperty("key", DatatypeConverter.printHexBinary(key));
					
					String b = GSON.toJson((JsonElement) jo);
					byte[] body = b.getBytes("UTF-8");

					exchange.sendResponseHeaders(200, body.length);

					OutputStream os = exchange.getResponseBody();
					os.write(body);
					os.close();

				} else {
					exchange.sendResponseHeaders(401, -1);
				}

			} else {
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();

		}
	}

	static class RegistrationHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {

			if ("GET".equals(exchange.getRequestMethod())) {

				String p = exchange.getRequestURI().getRawQuery();
				System.out.println(p);
				Map<String, String> params = new HashMap<String, String>();

				for (String str : p.split("&")) {
					String[] pair = str.split("=");
					params.put(pair[0], pair[1]);
				}

				String login = "", password = "";
				if (params.containsKey("login"))
					login = params.get("login");
				if (params.containsKey("password"))
					password = params.get("password");
				
				Headers h = exchange.getRequestHeaders();
				byte[] publicKey = Base64.decodeBase64(h.getFirst("PublicKey"));
				
				System.out.println("GET: login = " + login + " password = " + password);
				if (db.createUser(login, password)) {
					String token = createJWT(" " + unicNumber, login);
					unicNumber++;
					byte[] key =  ServerHttp.generateKey(publicKey, token);
					
					JsonObject jo = new JsonObject();
					jo.addProperty("token", token);
					jo.addProperty("key", DatatypeConverter.printHexBinary(key));
					
					String b = GSON.toJson((JsonElement) jo);
					byte[] body = b.getBytes("UTF-8");

					exchange.sendResponseHeaders(200, body.length);

					OutputStream os = exchange.getResponseBody();
					os.write(body);
					os.close();

				} else {
					exchange.sendResponseHeaders(409, -1);
				}

			} else {
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();

		}
	}
	
	static class GoodHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			StringBuilder builder = new StringBuilder();

			if (exchange.getRequestMethod().equals("POST")) {
				System.out.println("POST");
				goodChange(exchange);
			} else if (exchange.getRequestMethod().equals("PUT")) {
				System.out.println("PUT");
				goodCreate(exchange);
			} else if (exchange.getRequestMethod().equals("DELETE")) {
				System.out.println("DELETE");
				goodDelete(exchange);
			} else if (exchange.getRequestMethod().equals("GET")) {
				System.out.println("GET");
				goodInfo(exchange);
			} else
				throw new UnsupportedOperationException();
			exchange.close();
		}

		private void goodInfo(HttpExchange exchange) throws IOException {

			String uri = exchange.getRequestURI().getRawPath();
			String[] uriParths = uri.split("/");
			String id = uriParths[uriParths.length - 1];
			Headers head = exchange.getRequestHeaders();
			String token = head.getFirst("Authorization");

			int idOfGood = Integer.valueOf(id);
			Good good = db.getGoodById(idOfGood);
			if (good != null) {
				System.out.println("send information to client about: "+good);
				String b = GSON.toJson(good);
				byte[] body = encryptData(token, b);
				System.out.println(new String(body));
				System.out.println("here");
				
				exchange.sendResponseHeaders(200, body.length);
				
				OutputStream os = exchange.getResponseBody();
				os.write(body);
				os.close();
			} else
				exchange.sendResponseHeaders(404, -1);

		}

		// delete
		private void goodDelete(HttpExchange exchange) throws IOException {
			String uri = exchange.getRequestURI().getRawPath();
			String[] uriParths = uri.split("/");
			String id = uriParths[uriParths.length - 1];

			int idOfGood = Integer.valueOf(id);
			
			Good g = db.getGoodById(idOfGood);
			
			if(g==null) exchange.sendResponseHeaders(404, -1);
			else 
			{
				if (db.deleteGoodById(idOfGood)) {
					System.out.println("deleted");
					exchange.sendResponseHeaders(204, -1);
				} else
					exchange.sendResponseHeaders(404, -1);
			}
		}

		// put
		private void goodCreate(HttpExchange exchange) throws IOException {

			InputStream is = exchange.getRequestBody();
			Headers head = exchange.getRequestHeaders();
			String token = head.getFirst("Authorization");

			try {
				
				Good goodToCreate = GSON.fromJson(decryptData(token, is.readAllBytes()), Good.class);
				if (db.createGoods(goodToCreate)) {
					System.out.println("Created good: "+goodToCreate);
					int id = db.getGoodId(goodToCreate.getName());
					JsonObject jo = new JsonObject();
					jo.addProperty("id", id);
					String b = GSON.toJson((JsonElement) jo);
					byte[] body = encryptData(token, b);

					exchange.sendResponseHeaders(201, body.length);

					OutputStream os = exchange.getResponseBody();
					os.write(body);
					os.close();
				} else
					exchange.sendResponseHeaders(409, -1);

			} catch (JsonSyntaxException | InvalidCharacteristicOfGoodsException e) {
				exchange.sendResponseHeaders(409, -1);
			}
		}

		// post
		private void goodChange(HttpExchange exchange) throws IOException {
			InputStream is = exchange.getRequestBody();
			Headers head = exchange.getRequestHeaders();
			String token = head.getFirst("Authorization");
			
			try {
				JsonObject jo = GSON.fromJson(decryptData(token, is.readAllBytes()), JsonObject.class);
				
				if(jo.has("good")) {
					
					Good goodToChange = GSON.fromJson(jo.get("good").getAsString(), Good.class);
					Good g = db.getGoodById(goodToChange.getId());
					
					if(g==null) exchange.sendResponseHeaders(404, -1);
					else {
						boolean isChanged = false;
						System.out.println("Good before changes: "+g);
						
						try {
							isChanged = db.updateGood(g.getId(), goodToChange.getName(), goodToChange.getDescription(), 
									goodToChange.getDescription(), goodToChange.getPrice());
						} catch (InvalidCharacteristicOfGoodsException e) {
							exchange.sendResponseHeaders(409, -1);
						}
						
						if(isChanged) {
							System.out.println("Chenged good: "+db.getGoodById(g.getId()));	
							exchange.sendResponseHeaders(204, -1);
						} else exchange.sendResponseHeaders(409, -1);
					}
				}
				else if(jo.has("id")) {
					int id = jo.get("id").getAsInt();
					Good g = db.getGoodById(id);
					
					if(g==null) 
						exchange.sendResponseHeaders(404, -1);
					else {
						System.out.println("Good before changes: "+g);
						
						if(jo.has("addGood"))  db.addGoodsById(id, jo.get("addGood").getAsInt());
						if(jo.has("removeGood"))
							try {
								db.removeGoodsById(id, jo.get("removeGood").getAsInt());
							} catch (InvalidCharacteristicOfGoodsException e) {
								exchange.sendResponseHeaders(409, -1);
							}
						System.out.println("Chenged good: "+db.getGoodById(id));	
						exchange.sendResponseHeaders(204, -1);
					}
				} else 
					exchange.sendResponseHeaders(404, -1);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				exchange.sendResponseHeaders(409, -1);
			}
		}
	}

	static class GroupHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {
			StringBuilder builder = new StringBuilder();

			if (exchange.getRequestMethod().equals("POST")) {
				System.out.println("POST");
				groupChange(exchange);
			} else if (exchange.getRequestMethod().equals("PUT")) {
				System.out.println("PUT");
				groupCreate(exchange);
			} else if (exchange.getRequestMethod().equals("DELETE")) {
				System.out.println("DELETE");
				groupDelete(exchange);
			} else if (exchange.getRequestMethod().equals("GET")) {
				System.out.println("GET");
				groupInfo(exchange);
			} else
				throw new UnsupportedOperationException();
			exchange.close();
		}


		private void groupInfo(HttpExchange exchange) throws IOException {

			String uri = exchange.getRequestURI().getRawPath();
			String[] uriParths = uri.split("/");
			String id = uriParths[uriParths.length - 1];
			Headers head = exchange.getRequestHeaders();
			String token = head.getFirst("Authorization");

			int idOfGroup = Integer.valueOf(id);
			Group group = db.getGroupById(idOfGroup);
			if (group != null) {
				System.out.println("send information to client about: "+group);
				String b = GSON.toJson(group);
				byte[] body = encryptData(token, b);
				System.out.println(new String(body));
				
				exchange.sendResponseHeaders(200, body.length);
				
				OutputStream os = exchange.getResponseBody();
				os.write(body);
				os.close();
			} else
				exchange.sendResponseHeaders(404, -1);

		}

		// delete
		private void groupDelete(HttpExchange exchange) throws IOException {
			String uri = exchange.getRequestURI().getRawPath();
			String[] uriParths = uri.split("/");
			String id = uriParths[uriParths.length - 1];

			int idOfGroup = Integer.valueOf(id);
			Group group = db.getGroupById(idOfGroup);
			if(group==null) exchange.sendResponseHeaders(404, -1);
			else 
			{
				if (db.deleteGroupById(idOfGroup)) {
					System.out.println("deleted");
					exchange.sendResponseHeaders(204, -1);
				} else
					exchange.sendResponseHeaders(404, -1);
			}
		}

		// put
		private void groupCreate(HttpExchange exchange) throws IOException {

			InputStream is = exchange.getRequestBody();
			Headers head = exchange.getRequestHeaders();
			String token = head.getFirst("Authorization");

			try {
				
				Group groupToCreate = GSON.fromJson(decryptData(token, is.readAllBytes()), Group.class);
				if (db.createGroup(groupToCreate)) {
					
					System.out.println("Created good: "+groupToCreate);
					JsonObject jo = new JsonObject();
					jo.addProperty("id", db.getGroupId(groupToCreate.getName()));
					String b = GSON.toJson((JsonElement) jo);
					
					byte[] body = encryptData(token, b);
					exchange.sendResponseHeaders(201, body.length);

					OutputStream os = exchange.getResponseBody();
					os.write(body);
					os.close();
				} else
					exchange.sendResponseHeaders(409, -1);

			} catch (JsonSyntaxException  e) {
				exchange.sendResponseHeaders(409, -1);
			}
		}

		// post
		private void groupChange(HttpExchange exchange) throws IOException {
			InputStream is = exchange.getRequestBody();
			Headers head = exchange.getRequestHeaders();
			String token = head.getFirst("Authorization");
			
			try {
				JsonObject jo = GSON.fromJson(decryptData(token, is.readAllBytes()), JsonObject.class);
				
				if(jo.has("group")){
					
					Group group = GSON.fromJson(jo.get("group").getAsString(), Group.class);
					Group groupToChange = db.getGroupById(group.getId()); 
					System.out.println("Group before changes: "+groupToChange);
					if(groupToChange==null) exchange.sendResponseHeaders(404, -1);
					else {
						boolean isChanged = false;
						
						
					    isChanged = db.updateGroup(group.getId(), group.getName(), group.getDescription());
						
						if(isChanged) {
							System.out.println("Group changed ");	
							exchange.sendResponseHeaders(204, -1);
						} else exchange.sendResponseHeaders(409, -1);
					}
				} else 
					exchange.sendResponseHeaders(404, -1);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				exchange.sendResponseHeaders(409, -1);
			}
		}
	}
	
	static class GoodListHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {

			if ("GET".equals(exchange.getRequestMethod())) {

				Headers head = exchange.getRequestHeaders();
				String token = head.getFirst("Authorization");
				
				String uri = exchange.getRequestURI().getRawPath();
				String[] uriParths = uri.split("/");
				
				String p = exchange.getRequestURI().getRawQuery();

				if (uriParths[uriParths.length - 2].equals("goods") && uriParths[uriParths.length - 1].equals("all")) {

					// get all goods

					Map<String, String> params = new HashMap<String, String>();

					if (p == null) {
						sendListOfGoods(exchange, db.getAllGoods(), token);
					} else {
						for (String str : p.split("&")) {
							String[] pair = str.split("=");
							params.put(pair[0], pair[1]);
						}
						String group = "";

						// get all goods from group
						if (params.containsKey("group")) {
							group = params.get("group");
							LinkedList<Good> goods = db.getAllGoodsOfTheGroup(group);
							sendListOfGoods(exchange, goods, token);
						} else {
							exchange.sendResponseHeaders(404, -1);
						}
					}

				} else {
					// get goods by criteria
					Map<String, String> params = new HashMap<String, String>();

					for (String str : p.split("&")) {
						String[] pair = str.split("=");
						params.put(pair[0], pair[1]);
					}

					String column = "", value = "";

					// get all goods from group
					if (params.containsKey("column"))
						column = params.get("column");
					if (params.containsKey("column"))
						value = params.get("value");
					LinkedList<Good> goods = db.listByGoodColumnContains(column, value);
					sendListOfGoods(exchange, goods, token);
				}
			} else {
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();

		}

		private void sendListOfGoods(HttpExchange exchange, LinkedList<Good> goods, String token) throws IOException {

			if(goods!=null && goods.size()>0) {
				JsonArray ja = new JsonArray();
				for (Good g : goods) {
					ja.add(GSON.toJson(g));
				}
				String b = GSON.toJson((JsonElement) ja);
				byte[] body = encryptData(token, b);

				exchange.sendResponseHeaders(200, body.length);

				OutputStream os = exchange.getResponseBody();
				os.write(body);
				os.close();
			} else exchange.sendResponseHeaders(404, -1);
			
		}
	}

	static class GroupListHandler implements HttpHandler {
		@Override
		public void handle(HttpExchange exchange) throws IOException {


			if ("GET".equals(exchange.getRequestMethod())) {

				Headers head = exchange.getRequestHeaders();
				String token = head.getFirst("Authorization");
				
				String uri = exchange.getRequestURI().getRawPath();
				String[] uriParths = uri.split("/");
				
				String p = exchange.getRequestURI().getRawQuery();

				if (p==null) sendListOfGroups(exchange, db.getAllGroups(), token);
				else {

					Map<String, String> params = new HashMap<String, String>();

					for (String str : p.split("&")) {
						String[] pair = str.split("=");
						params.put(pair[0], pair[1]);
					}

					String column = "", value = "";

					// get all goods from group
					if (params.containsKey("column"))
						column = params.get("column");
					if (params.containsKey("column"))
						value = params.get("value");
					sendListOfGroups(exchange, db.listByGroupColumnContains(column, value), token);
				}
			} else 
			{
				exchange.sendResponseHeaders(405, -1);// 405 Method Not Allowed
			}
			exchange.close();

		}
	
	private void sendListOfGroups(HttpExchange exchange, LinkedList<Group> groups, String token) throws IOException {
		if (groups != null && groups.size() > 0) {
			JsonArray ja = new JsonArray();
			for (Group g : groups) {
				ja.add(GSON.toJson(g));
			}
			String b = GSON.toJson((JsonElement) ja);
			byte[] body = encryptData(token, b);

			exchange.sendResponseHeaders(200, body.length);

			OutputStream os = exchange.getResponseBody();
			os.write(body);
			os.close();
		} else
			exchange.sendResponseHeaders(404, -1);
	
	}
	}

	
	static class Auth extends Authenticator {
		@Override
		public Result authenticate(HttpExchange httpExchange) {
			Headers head = httpExchange.getRequestHeaders();
			String token = head.getFirst("Authorization");
			if (token==null || token.length() <= 1) 
				return new Failure(403);
			if (isAlive(token))
				return new Success(new HttpPrincipal("c0nst", "realm"));
			else
				return new Failure(403);
		}
	}
	
	public static String getMD5EncryptedValue(String password) {
        final byte[] defaultBytes = password.getBytes();
        try {
            final MessageDigest md5MsgDigest = MessageDigest.getInstance("MD5");
            md5MsgDigest.reset();
            md5MsgDigest.update(defaultBytes);
            final byte messageDigest[] = md5MsgDigest.digest();
            final StringBuffer hexString = new StringBuffer();
            for (final byte element : messageDigest) {
                final String hex = Integer.toHexString(0xFF & element);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            password = hexString + "";
        } catch (final NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        return password;
    }
	
	public static byte[] encryptData(String token, String data) {
		try {
			cipher = Cipher.getInstance("AES");
			byte[] d = data.getBytes("UTF-8");
			cipher.init(Cipher.ENCRYPT_MODE, tokenAndKey.get(token));

			return Base64.encodeBase64(cipher.doFinal(d));
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decryptData(String token, byte[] data) {
		try {
			cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, tokenAndKey.get(token));
			byte[] decrypted = cipher.doFinal(data);
			String res = new String(decrypted);
			return res;
		} catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException | NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static byte[] generateKey(byte[] pubKey, String token) {
		KeyFactory kf;
		byte[] encryptedKeyBytes = null;
		try {
			kf = KeyFactory.getInstance("RSA");
			PublicKey publicKey = kf.generatePublic(new X509EncodedKeySpec(pubKey));
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			KeyGenerator gen = KeyGenerator.getInstance("AES");
			gen.init(128, new SecureRandom());

			Key key = gen.generateKey();
			tokenAndKey.put(token, key);
			byte[] keyBytes = key.getEncoded();
			encryptedKeyBytes = cipher.doFinal(keyBytes);
			
		} catch (NoSuchAlgorithmException | InvalidKeySpecException | InvalidKeyException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			e.printStackTrace();
		}

		return encryptedKeyBytes;
	}

}
