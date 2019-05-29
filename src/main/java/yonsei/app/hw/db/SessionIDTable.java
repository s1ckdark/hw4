package yonsei.app.hw.db;

import io.vertx.core.json.JsonObject;
import redis.clients.jedis.Jedis;
import yonsei.app.hw.db.base.RedisBase;


/*
 * SessionIDTable
 * managing session ids
 * 
 * type :hset
 * 
 * field : SessionID
 * value : {uidx, etc informations (ex : expire, roles...) , ...}
 */

public class SessionIDTable extends RedisBase {
	
	private static SessionIDTable inst = new SessionIDTable();
	public static SessionIDTable inst() {return inst;} 
	
	//setting prefix key or table name
	@Override
	protected String TABLENAME() {
		return "SessionID";
	}

	//put session id - create or update
	public void put(String sessionID, String uidx) {
		put(sessionID, uidx, null);
	}
	
	//put session id - create or update 
	//store etc informations 
	public void put(String sessionID, String uidx, JsonObject etc) {
		try(Jedis jedis = getJedis()) {
			JsonObject value = new JsonObject();
			value.put("uidx", uidx);
			
			if (etc != null)
				value.mergeIn(etc);
			
			jedis.hset(key(), sessionID, value.encode());
		}
	}

	/*
	 * get (uidx and etc) for sessionID
	 * @return {uidx:[uidx], etc}
	 */
	public JsonObject get(String sessionID) { 
		try(Jedis jedis = getJedis()) {
			String value = jedis.hget(key(), sessionID);
			if (value == null)
				return null;
			JsonObject session = new JsonObject(value);

			//include other informations if needed
			session.put("sessionID", sessionID);
			
			return session;
		}
	}
	
	//delete when log out or drop out
	public void del(String sessionID) {
		try(Jedis jedis = getJedis()) {
			jedis.hdel(key(), sessionID);
		}
	}
}
