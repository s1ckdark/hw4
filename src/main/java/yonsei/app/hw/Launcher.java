package yonsei.app.hw;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Scanner;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import yonsei.app.hw.db.base.RedisPool;

public class Launcher {

	//change this value for Homework number^^;
	final private int hwNo = 4;

	//port = 8080 + hwNo!
	final private int port = 8080 + hwNo;

	private Vertx vertx = Vertx.vertx();

	public static void main(String[] args) {
		//notice usage
		if (args.length < 1) {
			System.out.println("java -jar hw2-1.0.0-fat.jar ID");
			args = new String[] {"12345"};
		} 
		new Launcher().launchServer(args[0]);
	}
	
	//server start with chool ID 
	public void launchServer(String id) {

		//start server with a web router
		HttpServer server = vertx.createHttpServer();
		
		//setup http request handler 
		server.requestHandler(new HomeworkRouter().create(vertx, id));

		//start listen
		server.listen(port, handler -> {
			//result after listen
			if (handler.succeeded()) {
				System.out.println("http://127.0.0.1:" + port + "/or http://ServerIP:" + port + "/");
				System.out.println("ID : " + id);
				
				//connect to db if server start successfully
				if (!connectRedis()) {
					vertx.close();
					System.out.println("Server is going to down");
					System.out.println("Bye bye");
				}
			} 
			else {
				System.err.println("Failed to listen on port 8080");
				killOthers();
			}
		});
	}

	private boolean connectRedis() {
		String host = "192.168.1.50";
		int port = 7468;
		String password = "qwer5678";
		return RedisPool.inst().redisOpen(host, port, password);
	}

	
	///==================== etc utils ==========================================================
	private void killOthers( ) {
		System.out.format("Unable to start server. port(%d) -> Kill others\n", port);
		
		Package pack = getClass().getPackage();
        String packageName = pack.getName();
        String []path = packageName.split("\\.");
        
		shellCmd(new String[] {"bash", "-c", "ps -ef | grep "+path[path.length - 2]+" | grep -v grep | awk {'print$2'} | xargs kill -9"});
	}

	private void shellCmd(String command[]) {
		try {
			Process process = Runtime.getRuntime().exec(command); 
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			Scanner scanner = new Scanner(br); scanner.useDelimiter(System.getProperty("line.separator"));
			while(scanner.hasNext()) System.out.println(scanner.next()); scanner.close(); br.close();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}