package sdufu.finalwork.grpc.server.exception;

/*
 * Class that represents errors when accessing the database
 */
public class DocumentException extends Exception {
	private static final long serialVersionUID = 1L;
	
	private DocumentExceptionTypes type;

	public DocumentException(DocumentExceptionTypes type) {
		super(type.value);
		this.type = type;
	}
	
	public DocumentExceptionTypes getType() {
		return this.type;
	}
}
