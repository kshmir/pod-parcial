package org.kikis.pod.parcial2011.ej1;

public class DataBase extends Service {

	@Override
	public void onInitialize() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Database server ready");
	}

}
