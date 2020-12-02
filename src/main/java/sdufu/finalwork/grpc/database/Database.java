package sdufu.finalwork.grpc.database;

import java.math.BigInteger;
import java.util.Hashtable;

import sdufu.finalwork.grpc.database.model.Document;

/*
 * Class to implement Hashtable
 */
public class Database extends Hashtable<BigInteger, Document> {
	private static final long serialVersionUID = 1L;
}
