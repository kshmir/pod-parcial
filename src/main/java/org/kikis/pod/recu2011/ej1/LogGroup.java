package org.kikis.pod.recu2011.ej1;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class LogGroup implements Cloneable {
	private final BlockingQueue<String> logs;
	private final Thread loggerThread;
	private final ExecutorService logService;
	private CountDownLatch latch;

	private static class LogTask implements Runnable {

		private final LogGroup group;
		private final Logger l;

		public LogTask(final LogGroup group, final Logger l) {
			super();
			this.group = group;
			this.l = l;
		}

		public void run() {
			l.doLog(group);
		}
	}

	LogGroup(final BlockingQueue<String> l) {
		logs = new LinkedBlockingQueue<String>(l);
		loggerThread = null;
		logService = null;
		latch = null;
	}

	public LogGroup(final long N, final long T, final Logger l) {
		logs = new LinkedBlockingQueue<String>();
		latch = new CountDownLatch((int) N);
		logService = Executors.newSingleThreadExecutor();
		loggerThread = new Thread(new Runnable() {
			public void run() {
				boolean interrumpted = false;
				while (!interrumpted || !logs.isEmpty()) {
					try {
						latch.await(T, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						interrumpted = true;
					}
					synchronized (LogGroup.this) {
						if (logs.size() > 0) {
							sendToLog(LogGroup.this, l);
						}
					}
				}
			}

			private void sendToLog(final LogGroup group, final Logger l) {
				LogTask task = new LogTask(group.clone(), l);
				logService.submit(task);
				latch = new CountDownLatch((int) N);
				group.logs.clear();
			}
		});

		loggerThread.start();
	}

	public boolean add(final String log) {
		try {
			logs.put(log);
			latch.countDown();
		} catch (InterruptedException e) {
		}
		return true;
	}

	public void finish() {
		loggerThread.interrupt();
		try {
			// Si este thread no se muere se peuden llegar a
			// Enviar más elementos al logService cuando
			// El mismo ya está apagado.
			loggerThread.join();
		} catch (InterruptedException e1) {
		}
		logService.shutdown();
		try {
			logService.awaitTermination(1, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
		}
	}

	public String[] logs() {
		return logs.toArray(new String[] {});
	}

	@Override
	public LogGroup clone() {
		return new LogGroup(logs);
	}
}