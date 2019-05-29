package yonsei.app.hw.db.base;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;


/*
 * Redis pool manager
 * open redis and return one connection
 */

public class RedisPool {
	private static final RedisPool instance = new RedisPool();
	public static RedisPool inst(){return instance;}

	private JedisPool redis_pool = null;

	private JedisPool createPool(String host, int port, String password) {
		GenericObjectPoolConfig config = new GenericObjectPoolConfig(); 
		return new JedisPool(config, host, port, 10000, password);
	}
	
	public boolean redisOpen(String host, int port, String password) {
		try {
			redis_pool = createPool(host, port, password);
			if (checkRedis() == false) 
				throw new Exception("Error connect redis failed : " + host+":"+port+ "  pwd : "+password);				
			System.out.format("Connected to redis master - %s\n\n",host+":"+port);
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public Jedis getJedis() {
		return redis_pool.getResource();
	}
	
	//check redis connection
	//try to get a random value
	private boolean checkRedis() {
		try(Jedis jedis = getJedis()) {
			jedis.get("1");
			return true;
		}
		catch(Exception e){}
		return false;
	}
}

