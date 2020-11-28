package sdufu.finalwork.grpc.service;

import java.io.IOException;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.DatabaseIO;
import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.database.model.Document;
import sdufu.finalwork.grpc.server.exception.DocumentException;
import sdufu.finalwork.grpc.server.exception.DocumentExceptionTypes;;

/*
 * Class that contains all business logics
 */
public class DocumentService {
	private Repository repository;
	private DatabaseIO databaseIO;

	public DocumentService(Repository repository, DatabaseIO databaseIO) {
		this.repository = repository;
		this.databaseIO = databaseIO;
	}

	/*
	 * Method to save document on database
	 */
	public Document set(BigInteger key, byte[] data, long timestamp) throws DocumentException {
		Document docInDatabase = this.repository.get(key);

		if (docInDatabase != null) {
			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_ALREADY_EXISTS);
		}

		Document doc = new Document();
		doc.setVersion(1);
		doc.setData(data);
		doc.setTimestamp(timestamp);

		try {
			this.databaseIO.saveStorageDocument(key, doc);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Document response = this.repository.put(key, doc);

		try {
			this.databaseIO.deleteStorageDocument(key);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	/*
	 * Method to get document on database by key
	 */
	public Document get(BigInteger key) {
		return this.repository.get(key);
	}

	/*
	 * Method to delete document on database by key
	 */
	public Document del(BigInteger key) throws DocumentException {
		Document document = this.get(key);

		if (document == null) {
			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
		}

		return this.delete(key, document);
	}

	/*
	 * Method to delete document on database by key and version
	 */
	public Document del(BigInteger key, long version) throws DocumentException {
		Document document = this.get(key);

		if (document == null) {
			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
		}

		if (document.getVersion() != version) {
			throw new DocumentException(DocumentExceptionTypes.DIFFERENT_DOCUMENT_VERSION);
		}

		return this.delete(key, document);
	}

	/*
	 * Method to delete document
	 */
	private Document delete(BigInteger key, Document document) {
		try {
			this.databaseIO.saveDeleteDocument(key, document);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Document response = this.repository.remove(key);

		try {
			this.databaseIO.deleteStorageDocument(key);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}
}
