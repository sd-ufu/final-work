package sdufu.finalwork.grpc.database.enums;

public enum DatabaseFilesConstants {
	DEL("DEL"),
	ADD("ADD");

	public String value;

	DatabaseFilesConstants(String value) {
		this.value = value;
	}
}
