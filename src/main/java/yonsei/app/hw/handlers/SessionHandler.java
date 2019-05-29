package yonsei.app.hw.handlers;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import yonsei.app.hw.command.Auth;
import yonsei.app.hw.db.SessionIDTable;

/*
 * Get uidx and other information from SessionIDTable for sessionID
 */

public class SessionHandler implements Handler<RoutingContext> {
	
	public static SessionHandler create() {
		SessionHandler handler = new SessionHandler();
		return handler;
	}
	
	private SessionHandler() {}

	private JsonObject session(RoutingContext req) {
		
		JsonObject packet = req.get("packet");
		if (packet == null)
			return null;
		
		String sessionID = packet.getString("sessionID");
		if (sessionID == null) 
			return null;
		
		return SessionIDTable.inst().get(sessionID);
	}
	
	@Override
	public void handle(RoutingContext req) {

		JsonObject session = session(req);
		if (session != null)
			req.put("session", session);
		
		req.next();
	}

}
