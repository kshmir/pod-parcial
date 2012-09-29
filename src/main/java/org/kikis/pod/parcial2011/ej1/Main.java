package org.kikis.pod.parcial2011.ej1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

	public static void main(final String args[]) {
		long startTime = System.currentTimeMillis();
		System.out.println("Starting server");
		Server server = new Server();
		server.initialize();
		ExecutorService executorService = Executors.newCachedThreadPool();
		for (int i = 0; i < 15; i++) {
			executorService.execute(new ClientRequest(server));
		}
		executorService.shutdown();
		try {
			executorService.awaitTermination(15L, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
		}
		System.out.println("Server finished processing all the requests in "
				+ (System.currentTimeMillis() - startTime) / 1000D);
	}
}
