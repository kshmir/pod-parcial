package org.kikis.pod;

public class Logger {

	private final LogGroup logGroup;

	public Logger(final long N, final long T) {
		logGroup = new LogGroup(N, T);
	}

	public void log(final String log) {
		logGroup.add(log);
		doLog(logGroup);
	}

	protected void doLog(final LogGroup logGroup) {
		System.out.print(logGroup.doLog());
	}

	protected void finish() {
		logGroup.finish();
	}
}
