package org.kikis.pod.recu2011.ej1;

public class Logger {

	private final LogGroup logGroup;

	public Logger(final long N, final long T) {
		logGroup = new LogGroup(N, T, this);
	}

	public void log(final String log) {
		synchronized (logGroup) {
			logGroup.add(log);
		}
	}

	public void doLog(final LogGroup logGroup) {
		for (String str : logGroup.logs()) {
			System.out.println(str);
		}
	}

	public void finish() {
		logGroup.finish();
	}
}
