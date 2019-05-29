package yonsei.app.hw.db;

import io.vertx.core.json.JsonObject;
import redis.clients.jedis.Jedis;
import yonsei.app.hw.db.base.RedisBase;

/*
 * AuthTable
 * store users ID for authorization 
 * 
 * key type : hash
 * 
 * field : ID
 * value : {pwd, uidx, etc...}
 * 
 * try to use this technique if you want to separate keys~ ^^
 * key(ID[0])
 * field : ID
 * value : ....
 * 
 * and should make LAU Script preventing multi threading..
 * ex) concur (deleting id and changing information) or (buying something blah blah..)  
 *    
 */
public class AuthTable extends RedisBase {
	private static AuthTable inst = new AuthTable();
	public static AuthTable inst() {return inst;} 
	
	//setting prefix key or table name
	@Override
	protected String TABLENAME() {
		return "Auth";
	}

	//put id, pwd and uidx to table
	//@return true if success or false  
	public boolean put(String id, String pwd, String uidx) {
		return put(id, pwd, uidx, null);
	}
	
	//put - create or update
	public boolean put(String id, String pwd, String uidx, JsonObject etc) {
		try(Jedis jedis = getJedis()) {
			JsonObject value = new JsonObject();
			value.put("pwd", pwd);
			value.put("uidx", uidx);
			
			if (etc != null)
				value.put("etc", etc);
			
			return jedis.hsetnx(key(), id, value.encode()) == 1;
		}
	}

	//when change password - Case 1 : put unconditionally~
	public void putPwd(String id, String pwd) {
		try(Jedis jedis = getJedis()) {

			//read a user data
			String json = jedis.hget(key(), id);
			if (json == null)
				return;
			
			//change password 
			JsonObject value = new JsonObject(json);
			value.put("pwd", pwd);
			
			//update to table
			jedis.hset(key(), id, value.encode());
		}
	}
	
	//when change password - Case 2 : put password after checking old password
	public boolean putPwd(String id, String newPwd, String oldPwd) {
		try(Jedis jedis = getJedis()) {

			//read a user data
			String json = jedis.hget(key(), id);
			if (json == null || oldPwd == null)
				return false;
			
			JsonObject value = new JsonObject(json);

			//compare both two passwords
			if (!oldPwd.equals(value.getString("pwd")))
				return false;
			
			//change password 
			value.put("pwd", newPwd);
			
			//update to table
			jedis.hset(key(), id, value.encode());
		}
		return true;
	}

	//get user pwd and uidx
	public JsonObject get(String id) { 
		try(Jedis jedis = getJedis()) {
			String value = jedis.hget(key(), id);
			if (value == null)
				return null;
			return new JsonObject(value);
		}
	}
	
	//delete
	public void del(String id) {
		try(Jedis jedis = getJedis()) {
			jedis.hdel(key(), id);
		}
	}
}
