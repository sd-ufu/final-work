package sdufu.finalwork.grpc.database;

import java.io.IOException;

/*
 * Class to create new Database instance
 */
public class DatabaseFactory {
	public static final Database build() {
		try {
			DatabaseIO databaseIO = new DatabaseIO();
			Database db = databaseIO.readDatabaseFile();

			if (db != null)
				return db;
			
			databaseIO.createDatabaseFile();

			return new Database();
		} catch (IOException e) {
			return new Database();
		}
	}
}
