package sdufu.finalwork.grpc.database;

import java.io.IOException;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;

public class Repository {
	private Database database;

	public Repository(Database database) {
		this.database = database;
	}

	public Document put(BigInteger key, Document value) {
		Document response = this.database.put(key, value);

		try {
			DatabaseIO.saveDocument(key, value);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
}
