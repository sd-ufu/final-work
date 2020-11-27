package sdufu.finalwork.grpc.service;

import java.math.BigInteger;

import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.database.model.Document;
import sdufu.finalwork.grpc.server.exception.DocumentException;
import sdufu.finalwork.grpc.server.exception.DocumentExceptionTypes;;

public class DocumentService {
	private Repository repository;

	public DocumentService(Repository repository) {
		this.repository = repository;
	}

	public Document set(BigInteger key, byte[] data, long timestamp) throws DocumentException {
		Document docInDatabase = this.repository.get(key);

		if (docInDatabase != null) {
			throw new DocumentException(DocumentExceptionTypes.DOCUMENT_ALREADY_EXISTS);
		}

		Document doc = new Document();
		doc.setVersion(1);
		doc.setData(data);
		doc.setTimestamp(timestamp);

		return this.repository.put(key, doc);
	}

	public Document get(BigInteger key) {
		return this.repository.get(key);
	}
}
