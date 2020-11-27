package sdufu.finalwork.grpc.server.exception;

public class DocumentException extends Exception {
	private static final long serialVersionUID = 1L;
	
	DocumentExceptionTypes message;

	public DocumentException(DocumentExceptionTypes message) {
		super();
		this.message = message;
	}
}
