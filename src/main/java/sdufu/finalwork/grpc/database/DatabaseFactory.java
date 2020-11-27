package sdufu.finalwork.grpc.database;

import java.io.IOException;

public class DatabaseFactory {
	public static final Database build() {
		try {
			Database db = DatabaseIO.readDatabaseFile();

			if (db != null)
				return db;
			
			DatabaseIO.createDatabaseFile();

			return new Database();
		} catch (IOException e) {
			return new Database();
		}
	}
}
