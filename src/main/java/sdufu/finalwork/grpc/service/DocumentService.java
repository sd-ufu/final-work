package sdufu.finalwork.grpc.service;

import java.io.IOException;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;
import sdufu.finalwork.grpc.server.exception.DocumentException;
import sdufu.finalwork.grpc.server.exception.DocumentExceptionTypes;
import sdufu.finalwork.ratis.Client;
import sdufu.finalwork.ratis.Operation;
import sdufu.finalwork.ratis.Request;
import sdufu.finalwork.ratis.Response;;

/*
 * Class that contains all business logics
 */
public class DocumentService {
	private Client client;

	public DocumentService(Client client) {
		this.client = client;
	}

	/*
	 * Method to save document on database
	 */
	public synchronized Document set(BigInteger key, byte[] data, long timestamp) throws DocumentException {
		try {
			Document document = this.get(key);

			if (document != null) {
				throw new DocumentException(DocumentExceptionTypes.DOCUMENT_ALREADY_EXISTS);
			}

			Document doc = new Document();
			doc.setVersion(1);
			doc.setData(data);
			doc.setTimestamp(timestamp);

			return this.save(key, doc, Operation.SET);
		} catch (IOException e) {
			e.printStackTrace();

			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
		}
	}

	/*
	 * Method to get document on database by key
	 */
	public Document get(BigInteger key) {
		try {
			Request req = new Request(key, Operation.GET);
			Document doc = this.client.send(req).getDocument();

			return doc;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Method to delete document on database by key
	 */
	public synchronized Document del(BigInteger key) throws DocumentException {
		try {
			Document document = this.get(key);

			if (document == null) {
				throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
			}

			return this.delete(key, document, Operation.DEL);
		} catch (IOException e) {
			e.printStackTrace();

			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
		}
	}

	/*
	 * Method to delete document on database by key and version
	 */
	public synchronized Document del(BigInteger key, long version) throws DocumentException {
		try {
			Document document = this.get(key);

			if (document == null) {
				throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
			}

			if (document.getVersion() != version) {
				throw new DocumentException(DocumentExceptionTypes.DIFFERENT_DOCUMENT_VERSION);
			}

			return this.delete(key, document, Operation.DEL_BY_VERSION);
		} catch (IOException e) {
			e.printStackTrace();

			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
		}
	}

	/*
	 * Method to test update document
	 */
	public synchronized Document testAndSet(BigInteger key, long version, byte[] data, long timestamp)
			throws DocumentException {

		try {
			Document docInDataBase = this.get(key);

			if (docInDataBase == null) {
				throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
			}

			if (docInDataBase.getVersion() != version) {
				throw new DocumentException(DocumentExceptionTypes.DIFFERENT_DOCUMENT_VERSION);
			}

			Document newDoc = new Document();
			newDoc.setVersion(version);
			newDoc.setData(data);
			newDoc.setTimestamp(timestamp);

			return this.save(key, newDoc, Operation.TEST_AND_SET);
		} catch (IOException e) {
			e.printStackTrace();

			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
		}
	}

	/*
	 * Method to delete document
	 */
	private Document delete(BigInteger key, Document document, Operation operation) throws IOException {
		Request req = new Request(key, document.getVersion(), operation);
		Response resp = this.client.send(req);

		return resp.getDocument();
	}

	/*
	 * Method to save document
	 */
	private Document save(BigInteger key, Document document, Operation operation)
			throws IOException, DocumentException {
		Request req = new Request(key, operation);
		req.setData(document.getData());
		req.setTimestamp(document.getTimestamp());
		req.setVersion(document.getVersion());

		Response r = this.client.send(req);

		return this.get(key);
	}
}
