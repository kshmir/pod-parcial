package org.kikis.pod.parcial2011.ej1;

public class Server extends Service {

	protected DataBase dataBase;
	protected DataBaseConnection dataBaseConnection;
	protected FTPServer ftpServer;
	protected WebServer webServer;

	public Server() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onInitialize() {
		dataBase = new DataBase();
		dataBase.initialize();
		dataBaseConnection = new DataBaseConnection(dataBase);
		dataBaseConnection.initialize();
		ftpServer = new FTPServer();
		ftpServer.initialize();
		webServer = new WebServer();
		webServer.initialize();
	}

	public void processRequest(final Request request) {
		if (!(dataBase.isReady() && dataBaseConnection.isReady()
				&& ftpServer.isReady() && webServer.isReady())) {
			throw new IllegalStateException(
					"Cannot process request if server is not ready");
		}
		request.process();
	}

}
