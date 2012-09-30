package org.kikis.pod;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kikis.pod.recu2011.ej1.Logger;

public class LoggerTest {

	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		outContent.reset();
	}

	@Test
	public void basicTest() throws InterruptedException {
		Logger logger = new Logger(1, 150);

		for (int i = 0; i < 100; i++) {
			logger.log("HOLA");
		}

		logger.finish();

		Assert.assertEquals(100, outContent.toString().split("\\n").length);
	}

	@Test
	public void finishAndFlushTest() {
		Logger logger = new Logger(3, 2);

		for (int i = 0; i < 100; i++) {
			logger.log("HOLA");
		}

		logger.finish();

		Assert.assertEquals(100, outContent.toString().split("\\n").length);
	}

	@Test
	public void timeoutAndFlushTest() throws InterruptedException {
		Logger logger = new Logger(10, 2);

		for (int i = 0; i < 200; i++) {
			logger.log("HOLA");
		}

		Thread.sleep(3000);

		Assert.assertEquals(200, outContent.toString().split("\\n").length);
	}
}
