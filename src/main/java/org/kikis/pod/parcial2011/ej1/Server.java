package org.kikis.pod.parcial2011.ej1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Server extends Service {

	protected DataBase dataBase;
	protected DataBaseConnection dataBaseConnection;
	protected FTPServer ftpServer;
	protected WebServer webServer;
	protected Thread worker;
	protected final BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<Request>(
			1);

	@Override
	public void onInitialize() {

		final ExecutorService service = Executors.newFixedThreadPool(3);

		service.submit(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				dataBase = new DataBase();
				dataBase.initialize();
				dataBaseConnection = new DataBaseConnection(dataBase);
				dataBaseConnection.initialize();
				return true;
			}
		});

		service.submit(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				ftpServer = new FTPServer();
				ftpServer.initialize();
				return true;
			}
		});

		service.submit(new Callable<Boolean>() {
			public Boolean call() throws Exception {
				webServer = new WebServer();
				webServer.initialize();
				return true;
			}
		});

		service.shutdown();

		worker = new Thread(new Runnable() {
			public void run() {
				try {
					service.awaitTermination(15, TimeUnit.MINUTES);

					if (!(dataBase.isReady() && dataBaseConnection.isReady()
							&& ftpServer.isReady() && webServer.isReady())) {
						throw new IllegalStateException(
								"Cannot process request if server is not ready");
					}

					boolean interrumpted = false;
					while (!interrumpted || !requestQueue.isEmpty()) {
						try {
							requestQueue.take().process();
						} catch (InterruptedException e) {
							interrumpted = true;
						}
					}

				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		worker.start();
	}

	public void processRequest(final Request request) {
		try {
			requestQueue.put(request);
		} catch (InterruptedException e) {
		}
	}
}
