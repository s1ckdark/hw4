package yonsei.app.hw.command;

import java.util.Date;

import io.vertx.core.json.JsonObject;
import yonsei.app.hw.db.AuthTable;
import yonsei.app.hw.db.CountTable;
import yonsei.app.hw.db.SessionIDTable;
import yonsei.app.hw.db.UserInfoTable;

public class Auth {

	public static JsonObject signup(JsonObject json) {
		JsonObject packet = json.getJsonObject("packet");

		String id = packet.getString("ID");
		String pwd = packet.getString("pwd");

		if (AuthTable.inst().get(id) == null) {
			String uidx = CountTable.inst().next("nextUserIndex").toString();
			if (AuthTable.inst().put(id, pwd, uidx)) {
				UserInfoTable.inst().put(uidx, "id",id);
//				UserInfoTable.inst().put(uidx, packet);
				return new JsonObject().put("ret", true);
			}
		}

		return new JsonObject().put("ret", false).put("message", "already taken");
	}


	public static JsonObject signin(JsonObject json) {
		JsonObject packet = json.getJsonObject("packet");

		String id = packet.getString("ID");
		String pwd = packet.getString("pwd");

		//1) check id exists
		JsonObject userAuthInfo = AuthTable.inst().get(id);
		if (userAuthInfo == null) {
			return new JsonObject().put("ret", false).put("message", "not found id");
		}

		//2) check a inputed password
		String userPwd = userAuthInfo.getString("pwd");
		if (pwd.equals(userPwd) == false) {
			return new JsonObject().put("ret", false).put("message", "wrong password");
		}

		String uidx = userAuthInfo.getString("uidx");

		//3)Unlink the old session id
		String oldSessionID = UserInfoTable.inst().get(uidx, "sessionID");
//		String oldSessionID = String.valueOf(UserInfoTable.inst().get(uidx));
		if (oldSessionID != null) {
			SessionIDTable.inst().del(oldSessionID);
		}

		//4) make new session id
		String sessionID = "sid:"+ new Date().getTime() + ":" + uidx;

		SessionIDTable.inst().put(sessionID, uidx);
		UserInfoTable.inst().put(uidx, "sessionID", sessionID);
//		UserInfoTable.inst().put(uidx, packet);

		return new JsonObject().put("ret", true).put("sessionID", sessionID);
	}

	public static JsonObject signout(JsonObject json) {
		JsonObject session = json.getJsonObject("session");
		String uidx = session.getString("uidx");

//		UserInfoTable.inst().put(uidx, session);
		UserInfoTable.inst().put(uidx, "sessionID", null);
		SessionIDTable.inst().del(uidx);

		return new JsonObject().put("ret", true);
	}

	public static JsonObject dropout(JsonObject json) {

		JsonObject session = json.getJsonObject("session");
		String uidx = session.getString("uidx");
		String id = UserInfoTable.inst().get(uidx, "id");
//		String id = String.valueOf(UserInfoTable.inst().get(uidx));

		UserInfoTable.inst().del(uidx);
		SessionIDTable.inst().del(uidx);
		AuthTable.inst().del(id);

		return new JsonObject().put("ret", true);
	}

	public static JsonObject userinfo_put(JsonObject json) {
		JsonObject session = json.getJsonObject("session");
		String uidx = session.getString("uidx");

		JsonObject packet = json.getJsonObject("packet");
		UserInfoTable.inst().put(uidx, packet);

		return new JsonObject().put("ret", true);
	}

	public static JsonObject userinfo_get(JsonObject json) {
		JsonObject session = json.getJsonObject("session");
		String uidx = session.getString("uidx");

		JsonObject userinfo = UserInfoTable.inst().get(uidx);
		return new JsonObject().put("ret", true).put("userinfo", userinfo);
	}

	public static JsonObject userinfo_del(JsonObject json) {
		JsonObject session = json.getJsonObject("session");
		String uidx = session.getString("uidx");

		JsonObject packet = json.getJsonObject("packet");
		UserInfoTable.inst().del(uidx);

		return new JsonObject().put("ret", true);
	}
}
