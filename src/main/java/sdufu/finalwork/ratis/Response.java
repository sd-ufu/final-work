package sdufu.finalwork.ratis;

import java.io.Serializable;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;

public class Response implements Serializable {
	private static final long serialVersionUID = 1L;

	private Document document;
	private BigInteger key;

	public Response() {
	}

	public Response(Document document, BigInteger key) {
		this.document = document;
		this.key = key;
	}

	public Document getDocument() {
		return document;
	}

	public void setDocument(Document document) {
		this.document = document;
	}

	public BigInteger getKey() {
		return key;
	}

	public void setKey(BigInteger key) {
		this.key = key;
	}

	@Override
	public String toString() {
		if (this.document != null) {
			return this.key + " <> " + this.document.toString();
		}

		return this.key + " <> ";
	}
}
