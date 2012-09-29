package org.kikis.pod.parcial2011.ej1;

public class DataBaseConnection extends Service {
	private final DataBase dataBase;

	public DataBaseConnection(final DataBase dataBase) {
		this.dataBase = dataBase;
	}

	@Override
	public void onInitialize() {
		if (!dataBase.isReady()) {
			throw new IllegalStateException("Data base not ready yet!");
		}
		System.out.println("Data base connection ready");
	}
}
