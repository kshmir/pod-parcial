package org.kikis.pod.recu2011.ej1;

public class Logger {

	private final LogGroup logGroup;

	public Logger(final long N, final long T) {
		logGroup = new LogGroup(N, T);
	}

	public void log(final String log) {
		logGroup.add(log);
	}

	public void finish() {
		logGroup.finish();
	}
}