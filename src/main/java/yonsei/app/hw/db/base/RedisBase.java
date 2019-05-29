package yonsei.app.hw.db.base;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.Jedis;

/*
 * A base class for redis db table
 */

public abstract class RedisBase {

	protected abstract String TABLENAME();
	static private Set<String> tablenames = new HashSet<String>();

	//check duplicated table name
	protected RedisBase() {
		if (tablenames.contains(TABLENAME())) 
			assert(false);
		tablenames.add(TABLENAME());
	}

	//key name
	protected String key() {
		return String.format("%s", TABLENAME());
	}
	
	//make key name with some arguments...
	//ex) key(id), key(arg1), key(arg1, arg2) ...
	//..
	protected String key(String key, Object ... args) {
		if (key == null)
			return key();
		return String.format("%s:%s", TABLENAME(), String.format(key, args));
	}

	protected Jedis getJedis() {
		return RedisPool.inst().getJedis();
	}
} 