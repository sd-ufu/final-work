package sdufu.finalwork.grpc.database.io;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Arrays;

import sdufu.finalwork.grpc.database.Database;
import sdufu.finalwork.grpc.database.enums.DatabaseConstants;
import sdufu.finalwork.grpc.database.enums.DatabaseFilesConstants;
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

			this.readDocumentOnTmpFiles(db);

			return db;
		} catch (EOFException e) {
			Database db = new Database();

			try {
				this.readDocumentOnTmpFiles(db);
			} catch (ClassNotFoundException | IOException e1) {
				e1.printStackTrace();
			}

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
	public synchronized void saveDatabase(Database db) throws IOException {
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
	 * return file name
	 */
	public String saveDocument(BigInteger key, Document value, DatabaseFilesConstants fileType) throws IOException {
		String path = this.getStorageDocumentPath(key, fileType);

		File file = new File(path);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream fileInputStream = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(fileInputStream);
		outputStream.writeObject(value);

		fileInputStream.close();
		outputStream.close();

		String[] pathSplited = path.split("/");
		return pathSplited[pathSplited.length - 1];
	}

	/*
	 * Method to delete temporary files to save on table file
	 */
	public void deleteDocument(String fileName) throws FileNotFoundException {
		String path = DatabaseConstants.STORAGE_DIR.value + "/" + fileName;

		File file = new File(path);
		file.delete();
	}

	/*
	 * Method to read temporary files to storage and save on table
	 */
	public synchronized void readDocumentOnTmpFiles(Database db) throws IOException, ClassNotFoundException {
		File[] files = this.getTmpFiles();

		if (files == null) {
			return;
		}


		Arrays.sort(files, new FilesComparator());

		ObjectInputStream objectInputStream;
		FileInputStream fileInputStream;

		for (File file : files) {
			fileInputStream = new FileInputStream(file);
			objectInputStream = new ObjectInputStream(fileInputStream);

			String name = file.getName().replace(".storage", "");
			String[] splitedName = name.split("_");
			String keyValue = splitedName[1];
			String action = splitedName[2];

			Document doc = (Document) objectInputStream.readObject();
			BigInteger key = new BigInteger(keyValue);

			if (action.equals(DatabaseFilesConstants.ADD.value)) {
				Document doc2 = db.get(key);

				if (doc2 == null) {
					db.put(key, doc);
				} else if (doc2.getVersion() == doc.getVersion()) {
					db.put(key, doc);
				}
			}

			if (action.equals(DatabaseFilesConstants.DEL.value)) {
				db.remove(key);
			}

			fileInputStream.close();
			objectInputStream.close();
		}

		this.saveDatabase(db);

		for (File file : files) {
			file.delete();
		}
	}


	/*
	 * Method to return storage document path
	 */
	private String getStorageDocumentPath(BigInteger key, DatabaseFilesConstants fileType) {
		return DatabaseConstants.STORAGE_DIR.value + "/" + System.currentTimeMillis() + "_" + key.toString() + "_"
				+ fileType.value + ".storage";
	}

	/*
	 * Method to get temporary files
	 */
	private File[] getTmpFiles() {
		File directory = new File(DatabaseConstants.STORAGE_DIR.value);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				return lowercaseName.endsWith(".storage");
			}
		};

		return directory.listFiles(filter);
	}
}
