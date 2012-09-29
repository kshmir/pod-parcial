package org.kikis.pod.parcial2011.ej1;

public class ClientRequest implements Runnable {

	private final Server server;

	public ClientRequest(final Server server) {
		this.server = server;
	}

	public void run() {
		server.processRequest(new Request());
	}

}
