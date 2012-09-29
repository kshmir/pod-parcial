package org.kikis.pod.recu2011.ej1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LogGroup {
	private final BlockingQueue<String> logs = new LinkedBlockingQueue<String>();
	private final Thread loggerThread;

	public LogGroup(final long N, final long T) {
		loggerThread = new Thread(new Runnable() {
			public void run() {
				try {
					List<String> logs = new ArrayList<String>();
					boolean interrumpted = false;
					while (!interrumpted || hasLog()) {
						boolean flushLog = false;
						String toLog = null;
						try {
							toLog = takeLog(T);
							if (toLog == null) {
								flushLog = true;
							} else {
								logs.add(toLog);
							}

						} catch (InterruptedException e) {
							interrumpted = true;
						} finally {
							flushLog = flushLog || logs.size() == N
									|| interrumpted;

							if (flushLog && !logs.isEmpty()) {
								doFlush(logs);
								logs.clear();
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			private void doFlush(final List<String> logs) {
				for (String string : logs) {
					System.out.println(string);
				}
			}
		});
		loggerThread.start();
	}

	public boolean add(final String log) {
		return logs.add(log);
	}

	private boolean hasLog() {
		return logs.size() > 0;
	}

	private String takeLog(final long seconds) throws InterruptedException {
		return logs.poll(seconds, TimeUnit.SECONDS);
	}

	public void finish() {
		loggerThread.interrupt();
	}
}