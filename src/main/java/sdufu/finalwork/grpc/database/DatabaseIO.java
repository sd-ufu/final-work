package sdufu.finalwork.grpc.database;

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
		} catch (EOFException e) {
			Database db = new Database();

			try {
				this.readDocumentTmpFiles(db);
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
	public String saveStorageDocument(BigInteger key, Document value) throws IOException {
		String path = this.getStorageDocumentPath(key);
		this.saveDocument(path, value);

		String[] pathSplited = path.split("/");
		return pathSplited[pathSplited.length - 1];
	}

	/*
	 * Method to save document on temporary storage file
	 */
	public String saveDeleteDocument(BigInteger key, Document value) throws IOException {
		String path = this.getDeleteDocumentPath(key);
		this.saveDocument(path, value);

		String[] pathSplited = path.split("/");
		return pathSplited[pathSplited.length - 1];
	}

	/*
	 * Method to delete temporary files to save on table file
	 */
	public void deleteStorageDocument(String fileName) throws FileNotFoundException {
		String path = DatabaseConstants.PUT_STORAGE_DIR.value + "/" + fileName;
		this.deleteFile(path);
	}

	/*
	 * Method to delete temporary files to delete on table file
	 */
	public void deleteDocument(String fileName) throws FileNotFoundException {
		String path = DatabaseConstants.DELETE_STORAGE_DIR.value + "/" + fileName;
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
		return DatabaseConstants.PUT_STORAGE_DIR.value + "/" + System.currentTimeMillis() + "_" + key.toString() + "_"
				+ DatabaseConstants.ADD_FILE_NAME.value + ".storage";
	}

	/*
	 * Method to return delete document path
	 */
	private String getDeleteDocumentPath(BigInteger key) {
		return DatabaseConstants.DELETE_STORAGE_DIR.value + "/" + System.currentTimeMillis() + "_" + key.toString()
				+ "_" + DatabaseConstants.DEL_FILE_NAME.value + ".storage";
	}

	/*
	 * Method to read temporary files to storage and save on table
	 */
	private void readDocumentTmpFiles(Database db) throws IOException, ClassNotFoundException {
		File[] putFilesList = this.getPutTmpFiles();
		File[] deleteFilesList = this.getDeletetmpFiles();
		File[] files = null;
		boolean hasPutFilesList = putFilesList != null;
		boolean hasDeleteFilesList = deleteFilesList != null;

		if (!hasPutFilesList && !hasDeleteFilesList) {
			return;
		}

		if (hasPutFilesList && hasDeleteFilesList) {
			files = new File[putFilesList.length + deleteFilesList.length];
			int count = 0;

			for (int i = 0; i < putFilesList.length; i++) {
				files[count] = putFilesList[i];
				count++;
			}

			for (int i = 0; i < deleteFilesList.length; i++) {
				files[count] = deleteFilesList[i];
				count++;
			}
		}

		if (hasPutFilesList && !hasDeleteFilesList) {
			files = putFilesList;
		}

		if (!hasPutFilesList && hasDeleteFilesList) {
			files = deleteFilesList;
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

			if (action.equals(DatabaseConstants.ADD_FILE_NAME.value)) {
				Document doc2 = db.get(key);

				if (doc2 == null) {
					db.put(key, doc);
				} else if (doc2.getVersion() == doc.getVersion()) {
					db.put(key, doc);
				}
			}

			if (action.equals(DatabaseConstants.DEL_FILE_NAME.value)) {
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

	private File[] getPutTmpFiles() {
		File directory = new File(DatabaseConstants.PUT_STORAGE_DIR.value);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				return lowercaseName.endsWith(".storage");
			}
		};

		return directory.listFiles(filter);
	}

	private File[] getDeletetmpFiles() {
		File directory = new File(DatabaseConstants.DELETE_STORAGE_DIR.value);
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String lowercaseName = name.toLowerCase();
				return lowercaseName.endsWith(".storage");
			}
		};

		return directory.listFiles(filter);
	}
}
