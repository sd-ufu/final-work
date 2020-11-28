package sdufu.finalwork.grpc.database;

import java.math.BigInteger;
import java.util.Hashtable;

import sdufu.finalwork.grpc.database.model.Document;

public class Database extends Hashtable<BigInteger, Document> {
	private static final long serialVersionUID = 1L;
	private static BigInteger KEY = new BigInteger("1");

	public static synchronized BigInteger generateKey() {
		BigInteger current = new BigInteger(Database.KEY.toString());

		Database.KEY.add(new BigInteger("1"));

		return current;
	}
}
