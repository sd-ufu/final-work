package sdufu.finalwork.grpc.database.io;

import java.io.IOException;

import sdufu.finalwork.grpc.database.Database;

public class SynchronizeDatabase implements Runnable {
	private Database database;
	private DatabaseIO databaseIO;

	public SynchronizeDatabase(Database database, DatabaseIO databaseIO) {
		this.database = database;
		this.databaseIO = databaseIO;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(60000);
				this.databaseIO.readDocumentOnTmpFiles(this.database);
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
