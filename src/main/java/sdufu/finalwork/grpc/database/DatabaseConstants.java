package sdufu.finalwork.grpc.database;

public enum DatabaseConstants {
	STORAGE_DIR(System.getProperty("user.dir") + "/bd/sdufu/finalwork"),
	DB_FILE_PATH(System.getProperty("user.dir") + "/bd/sdufu/finalwork" + "/table.dat");

	public String value;

	DatabaseConstants(String value) {
		this.value = value;
	}
}
