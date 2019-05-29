package yonsei.app.hw.handlers;
import java.util.HashMap;
import java.util.Map;

import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import yonsei.app.hw.command.JsonFunc;

/*
 * Handler for commands we use
 * It needs a command map so we should bind a map after creating
 */

public class CommandHandler implements Handler<RoutingContext> {
	
	public static CommandHandler create() {
		CommandHandler handler = new CommandHandler();
		return handler;
	}
		
	private Map<String, JsonFunc> cmds = null;

	//chain function
	//and setting a command map
	public CommandHandler setMap(HashMap<String, JsonFunc> map) {
		this.cmds = map;
		return this;
	}
	
	@Override
	public void handle(RoutingContext req) {
		//return type for packet is json
		req.response().putHeader("content-type", "application/json");
		
		try {
			
			//1) parsing request json packet
			JsonObject packet = req.get("packet");
			JsonObject session = req.get("session");
			
			//2) get command for request
			String command = packet.getString("command");
			
			//3) find a register function for command
			JsonFunc func = cmds.get(command);
			if (func == null) {
				//if no command ? => return error
				JsonObject error = new JsonObject().put("ret", false).put("message", "wrong commands.");
				req.response().end(error.encode());
				return;
			}

			//4) processing...
			try {
				//user's json
				JsonObject json = new JsonObject().put("packet", packet);

				//user's session
				if (session != null)
					json.put("session", session);
				
				JsonObject ret = func.func(json);
				
				//return result;
				req.response().end(ret.encode());
				return;
			}
			catch(Exception e){ 
				//error in commands
				e.printStackTrace();
				//show the command occuring error;
				System.out.println("input params : " + packet);
			}
		}
		catch(Exception e) {
			//unknown error happens!!!
			e.printStackTrace();
		}
		JsonObject error = new JsonObject().put("ret", false).put("message", "system error");
		req.response().end(error.encode());
	}

}
