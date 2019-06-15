package yonsei.app.hw.command;

import io.vertx.core.json.JsonObject;
import netscape.javascript.JSObject;
import yonsei.app.hw.db.UserInfoTable;

public class UserInfo {

    public static JsonObject userinfo_put(JsonObject json) {
        System.out.println("userinfo_put init");
        System.out.println("json : " + json);

        //JsonObject session = json.getJsonObject("session");
        //String uidx = session.getString("uidx");

        JsonObject packet = json.getJsonObject("packet");
        String utk = packet.getString("utk");
        String uidx = utk.substring(utk.length()-1);

        UserInfoTable.inst().put(uidx, packet);
        return new JsonObject().put("ret", true);
    }

    public static JsonObject userinfo_get(JsonObject json) {
        System.out.println("userinfo_get init");
        System.out.println("json : " + json);

        //JsonObject session = json.getJsonObject("session");
        //String uidx = session.getString("uidx");

        JsonObject packet = json.getJsonObject("packet");
        String utk = packet.getString("utk");
        String uidx = utk.substring(utk.length()-1);

        JsonObject userinfo = UserInfoTable.inst().get(uidx);

        System.out.println("userinfo_get : " + userinfo.toString());
        return new JsonObject().put("ret", true).put("userinfo", userinfo);
    }

    public static JsonObject userinfo_del(JsonObject json) {
        System.out.println("userinfo_del init");
        System.out.println("json : " + json);

        //JsonObject session = json.getJsonObject("session");
        //String uidx = session.getString("uidx");

        JsonObject packet = json.getJsonObject("packet");
        String utk = packet.getString("utk");
        String uidx = utk.substring(utk.length()-1);
        UserInfoTable.inst().del(uidx);

        return new JsonObject().put("ret", true);
    }

}
