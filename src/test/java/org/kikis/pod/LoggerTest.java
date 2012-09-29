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
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

	@Before
	public void setUpStreams() {
		System.setOut(new PrintStream(outContent));
		System.setErr(new PrintStream(errContent));
	}

	@After
	public void cleanUpStreams() {
		System.setOut(null);
		System.setErr(null);
		outContent.reset();
		errContent.reset();
	}

	@Test
	public void basicTest() throws InterruptedException {
		Logger logger = new Logger(1, 150);

		logger.log("HOLA");

		logger.finish();

		Assert.assertEquals(1, outContent.toString().split("\\n").length);
	}

	@Test
	public void finishAndFlushTest() {
		Logger logger = new Logger(3, 2);

		logger.log("HOLA");
		logger.log("HOLA");

		logger.finish();

		Assert.assertEquals(2, outContent.toString().split("\\n").length);
	}

	@Test
	public void timeoutAndFlushTest() throws InterruptedException {
		Logger logger = new Logger(10, 2);

		logger.log("HOLA");
		logger.log("HOLA");
		logger.log("HOLA");
		logger.log("HOLA");

		Thread.sleep(3000);

		Assert.assertEquals(4, outContent.toString().split("\\n").length);
	}
}
