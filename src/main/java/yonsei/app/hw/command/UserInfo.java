package yonsei.app.hw.command;

import io.vertx.core.json.JsonObject;
import yonsei.app.hw.db.UserInfoTable;

public class UserInfo {

    public static JsonObject userinfo_put(JsonObject json) {
        JsonObject session = json.getJsonObject("session");
        String uidx = session.getString("uidx");

        JsonObject packet = json.getJsonObject("packet");
        UserInfoTable.inst().put(uidx, packet);

        return JsonObject().put("ret", true);
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

//    public static JsonObject userinfo_put(JsonObject json){
//        return new JsonObject();
//    }
//    public static JsonObject userinfo_get(JsonObject json){
//        return new JsonObject();
//    }
//    public static JsonObject userinfo_del(JsonObject json){
//        return new JsonObject();
//    }
}
