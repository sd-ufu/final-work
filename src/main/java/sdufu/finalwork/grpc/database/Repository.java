package sdufu.finalwork.grpc.database;

import java.io.IOException;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.io.DatabaseIO;
import sdufu.finalwork.grpc.database.model.Document;

/*
 * Class that communicates with the bank
 */
public class Repository {
	private Database database;
	private DatabaseIO databaseIO;

	public Repository(Database database, DatabaseIO databaseIO) {
		this.database = database;
		this.databaseIO = databaseIO;
	}

	/*
	 * Method to save or update new document
	 */
	public Document put(BigInteger key, Document value) {
		Document response = this.database.put(key, value);

		try {
			this.databaseIO.saveDatabase(this.database);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	/*
	 * Method to get document
	 */
	public Document get(BigInteger key) {
		return this.database.get(key);
	}

	/*
	 * Method to remove document
	 */
	public Document remove(BigInteger key) {
		Document response = this.database.remove(key);

		try {
			this.databaseIO.saveDatabase(this.database);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
}
