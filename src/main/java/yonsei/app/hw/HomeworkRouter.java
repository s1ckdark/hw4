package yonsei.app.hw;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import yonsei.app.hw.command.CommandMap;
import yonsei.app.hw.handlers.CommandHandler;
import yonsei.app.hw.handlers.SessionHandler;

public class HomeworkRouter  {

	public Router create(Vertx vertx, String id) {
		//setup router for web 
		Router router = Router.router(vertx);

		//use a default bodyhandler for Http body parsing
		router.route().handler(BodyHandler.create());
		
		//why send packet with get method?
		router.route().method(HttpMethod.GET).handler(req->{
			//maybe hacking???
			//register blacklist~
			// or some blah blah~~~
			
			//We send no response~
			req.response().end();
		});
		
		router.route().method(HttpMethod.POST).handler(req->{
			//actually, we should implement our handler here for security
			req.next();
		});
		
		//parsing http body data to a JsonoObject
		router.route().handler(req->{
			try { 
				JsonObject json = req.getBodyAsJson();
				System.out.println("[Request body]");
				System.out.println(req.getBodyAsString());
				req.put("packet", json);
				req.next();
			}
			catch(Exception e) {
				//json parsing error!
				req.response().end("JsonParsing Error!!");
			}
		});
	
		//handler for sessionID -> {uidx, etc}
		router.route().handler(SessionHandler.create());
		
		//handler for our request 
		router.route().handler(CommandHandler.create().setMap(new CommandMap()));
	

		return router;
	}
}
