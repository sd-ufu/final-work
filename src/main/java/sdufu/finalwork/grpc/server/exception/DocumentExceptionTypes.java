package sdufu.finalwork.grpc.server.exception;

public enum DocumentExceptionTypes {
	DOCUMENT_ALREADY_EXISTS("DOCUMENT ALREADY EXISTS"),
	DOCUMENT_DOES_NOT_EXIST("DOCUMENT DOES NOT EXISTS"),
	DIFFERENT_DOCUMENT_VERSION("DIFFERENT DOCUMENT VERSION");
	
	public String value;

	DocumentExceptionTypes(String value) {
		this.value = value;
	}
}
