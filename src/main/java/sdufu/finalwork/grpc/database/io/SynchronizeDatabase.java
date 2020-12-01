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
				System.out.println("VOU SINCRONIZAR AGORA");
				Thread.sleep(2000);
				this.databaseIO.readDocumentOnTmpFiles(this.database);
				System.out.println("SINCRONIZEI");
				Thread.sleep(60000);
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
