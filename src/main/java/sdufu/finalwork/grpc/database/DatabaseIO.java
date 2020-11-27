package sdufu.finalwork.grpc.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;

public class DatabaseIO {
	@SuppressWarnings("resource")
	public static Database readDatabaseFile() {
		File file = new File(DatabaseConstants.DB_FILE_PATH.value);

		try {
			file.getParentFile().mkdirs();
			file.createNewFile();

			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			return (Database) objectInputStream.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}
	
	public static void createDatabaseFile() throws IOException {
		File file = new File(DatabaseConstants.DB_FILE_PATH.value);
		file.getParentFile().mkdirs();
		file.createNewFile();
		file.mkdirs();
	}

	public static void saveDatabase(Database db) throws IOException {
		File file = new File(DatabaseConstants.DB_FILE_PATH.value);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream s = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(s);
		outputStream.writeObject(db);

		outputStream.close();
	}

	public static void saveDocument(BigInteger key, Document value) throws IOException {
		String path = DatabaseIO.getDocumentPath(key);

		File file = new File(path);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream s = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(s);
		outputStream.writeObject(value);

		outputStream.close();
	}
	
	private static String getDocumentPath(BigInteger key) {
		return DatabaseConstants.STORAGE_DIR.value + "/" + key.toString() + ".storage";
	}
}
