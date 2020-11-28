package sdufu.finalwork.grpc.database;

/*
 * Enum with database files path
 */
public enum DatabaseConstants {
	PUT_STORAGE_DIR(System.getProperty("user.dir") + "/bd/sdufu/finalwork/tmp/storage"),
	DELETE_STORAGE_DIR(System.getProperty("user.dir") + "/bd/sdufu/finalwork/tmp/delete"),
	DB_FILE_PATH(System.getProperty("user.dir") + "/bd/sdufu/finalwork" + "/table.dat");

	public String value;

	DatabaseConstants(String value) {
		this.value = value;
	}
}