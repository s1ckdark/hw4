package yonsei.app.hw.db;

import redis.clients.jedis.Jedis;
import yonsei.app.hw.db.base.RedisBase;

public class CountTable extends RedisBase {

	private static CountTable inst = new CountTable();
	public static CountTable inst() {return inst;} 

	@Override
	protected String TABLENAME() {
		return "Count";
	}
	
	public Long next(String key) {
		try(Jedis jedis = getJedis()) {
			return jedis.incr(key(key));
		}
	}

}
