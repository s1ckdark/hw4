package yonsei.app.hw.db;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import redis.clients.jedis.Jedis;
import yonsei.app.hw.db.base.RedisBase;
import yonsei.app.hw.db.base.RedisPool;

/*
 * UserInfoTable for user's information
 *
 * type hash
 * one row for a user
 *
 * UserInfo:uidx - {}
 */
public class UserInfoTable extends RedisBase {

    private static UserInfoTable inst = new UserInfoTable();
    public static UserInfoTable inst() {return inst;}

    @Override
    protected String TABLENAME() {
        return "UserInfo";
    }

    public void put(String uidx, String field, String value) {
        try(Jedis jedis = getJedis()) {
            if (value != null)
                jedis.hset(key(uidx), field, value);
            else
                jedis.hdel(key(uidx), field);
        }
    }

    public void put(String uidx, JsonObject params) {
        String name = params.getString("name");
        String age = params.getString("age");

        try(Jedis jedis = getJedis()) {
            JsonObject val = new JsonObject().put("name", name).put("age", age);
            jedis.hset("UserInfo",  uidx, val.toString());
        }
    }


    public JsonObject get(String uidx) {
        try(Jedis jedis = getJedis()) {
            String val = jedis.hget("UserInfo",  uidx);
            if(val != null)
                return new JsonObject(val);
            else
                return new JsonObject();
        }
    }

    public String get(String uidx, String field) {
        try(Jedis jedis = getJedis()) {
            return jedis.hget(key(uidx), field);
        }
    }

    public void del(String uidx) {
        try(Jedis jedis = getJedis()) {
            jedis.del("UserInfo", uidx);
        }
    }

}