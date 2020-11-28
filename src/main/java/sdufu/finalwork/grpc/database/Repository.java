package sdufu.finalwork.grpc.database;

import java.io.IOException;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;

public class Repository {
	private Database database;
	private DatabaseIO databaseIO;

	public Repository(Database database, DatabaseIO databaseIO) {
		this.database = database;
		this.databaseIO = databaseIO;
	}

	public Document put(BigInteger key, Document value) {
		Document response = this.database.put(key, value);

		try {
			this.databaseIO.saveDatabase(this.database);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("DB: " + this.database);

		return response;
	}

	public Document get(BigInteger key) {
		System.out.println("DB: " + this.database);
		return this.database.get(key);
	}

	public Document remove(BigInteger key) {
		Document response = this.database.remove(key);

		try {
			this.databaseIO.saveDatabase(this.database);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("DB: " + this.database);

		return response;
	}
}
