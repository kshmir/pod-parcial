package org.kikis.pod.parcial2011.ej1;

public abstract class Service {
	private boolean ready = false;

	public void initialize() {
		onInitialize();
		setIsReady(true);
	}

	public synchronized void setIsReady(final boolean ready) {
		this.ready = ready;
	}

	public synchronized boolean isReady() {
		return ready;
	}

	public abstract void onInitialize();
}
