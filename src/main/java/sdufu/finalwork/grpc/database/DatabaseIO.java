package sdufu.finalwork.grpc.database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;

import sdufu.finalwork.grpc.database.model.Document;

/*
 * Class to write, read or delete database files
 */
public class DatabaseIO {
	/*
	 * Method to read table file and return Database instance
	 */
	@SuppressWarnings("resource")
	public Database readDatabaseFile() {
		File file = new File(DatabaseConstants.DB_FILE_PATH.value);

		try {
			file.getParentFile().mkdirs();
			file.createNewFile();

			FileInputStream fileInputStream = new FileInputStream(file);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

			Database db = (Database) objectInputStream.readObject();

			this.readDocumentTmpFiles(db);

			return db;
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	/*
	 * Method to create table file
	 */
	public void createDatabaseFile() throws IOException {
		File file = new File(DatabaseConstants.DB_FILE_PATH.value);
		file.getParentFile().mkdirs();
		file.createNewFile();
		file.mkdirs();
	}

	/*
	 * Method to save database on table file
	 */
	public void saveDatabase(Database db) throws IOException {
		File file = new File(DatabaseConstants.DB_FILE_PATH.value);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream s = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(s);
		outputStream.writeObject(db);

		outputStream.close();
	}

	/*
	 * Method to save document on temporary storage file
	 */
	public void saveStorageDocument(BigInteger key, Document value) throws IOException {
		String path = this.getStorageDocumentPath(key);
		this.saveDocument(path, value);
	}

	/*
	 * Method to save document on temporary storage file
	 */
	public void saveDeleteDocument(BigInteger key, Document value) throws IOException {
		String path = this.getDeleteDocumentPath(key);
		this.saveDocument(path, value);
	}

	/*
	 * Method to delete temporary files to save on table file
	 */
	public void deleteStorageDocument(BigInteger key) throws FileNotFoundException {
		String path = this.getStorageDocumentPath(key);
		this.deleteFile(path);
	}

	/*
	 * Method to delete temporary files to delete on table file
	 */
	public void deleteDocument(BigInteger key) throws FileNotFoundException {
		String path = this.getDeleteDocumentPath(key);
		this.deleteFile(path);
	}

	/*
	 * Method to delete any document on temporary folder
	 */
	private void deleteFile(String path) {
		File file = new File(path);
		file.delete();
	}

	/*
	 * Method to save any document on temporary folder
	 */
	private void saveDocument(String path, Document value) throws IOException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream fileInputStream = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(fileInputStream);
		outputStream.writeObject(value);

		fileInputStream.close();
		outputStream.close();
	}

	/*
	 * Method to return storage document path
	 */
	private String getStorageDocumentPath(BigInteger key) {
		return DatabaseConstants.PUT_STORAGE_DIR.value + "/" + key.toString() + ".storage";
	}

	/*
	 * Method to return delete document path
	 */
	private String getDeleteDocumentPath(BigInteger key) {
		return DatabaseConstants.DELETE_STORAGE_DIR.value + "/" + key.toString() + ".storage";
	}

	/*
	 * Method to read temporary files to storage and save on table
	 */
	private void readDocumentTmpFiles(Database db) throws IOException, ClassNotFoundException {
		String extension = ".storage";
		File directory = new File(DatabaseConstants.PUT_STORAGE_DIR.value);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				return lowercaseName.endsWith(extension);
			}
		};

		File[] filesList = directory.listFiles(filter);
		
		if (filesList == null) {
			return;
		}

		ObjectInputStream objectInputStream;
		FileInputStream fileInputStream;

		for (File file : filesList) {
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);

			Document doc = (Document) objectInputStream.readObject();
			BigInteger key = new BigInteger(file.getName().replace(extension, ""));
			db.put(key, doc);

			fileInputStream.close();
			objectInputStream.close();
		}

		if (filesList.length != 0) {
			this.saveDatabase(db);
		}

		for (File file : filesList) {
			file.delete();
		}
	}
}
