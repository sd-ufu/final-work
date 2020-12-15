package sdufu.finalwork.grpc.database;

import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;

/*
 * Class that communicates with the bank
 */
public class Repository {
	public Database database;

	public Repository(Database database) {
		this.database = database;
	}

	/*
	 * Method to save or update new document
	 */
	public Document put(BigInteger key, Document value) {
		return this.database.put(key, value);
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
		return this.database.remove(key);
	}
}
