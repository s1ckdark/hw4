package yonsei.app.hw.command;

import java.util.HashMap;

public class CommandMap extends HashMap<String, JsonFunc> {

	private static final long serialVersionUID = 5894250207559726225L;

	
	public CommandMap() {
		//==== commands for authorization
		put("signup",  Auth::signup);
		put("signin",  Auth::signin);
		put("signout", Auth::signout);
		put("dropout", Auth::dropout);

		put("userinfo_put", UserInfo::userinfo_put);
		put("userinfo_get", UserInfo::userinfo_get);
		put("userinfo_del", UserInfo::userinfo_del);
	}

}
