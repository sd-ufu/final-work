package sdufu.finalwork.grpc.document;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.Hashtable;

public class Table extends Hashtable<BigInteger, Document> {
	private static final long serialVersionUID = 1L;
	private static final String STORAGE_DIR = System.getProperty("user.dir") + "/bd/sdufu/finalwork";
	protected static final String TABLE_DIR = Table.STORAGE_DIR + "/table.dat";

	public Table() {
		super();
		this.createIfNotExistsFolder();
	}

	@Override
	public synchronized Document put(BigInteger key, Document value) {
		Document response = super.put(key, value);

		String path = this.getPath(key);
		System.out.println("PATH: " + path);

		try {
			this.saveDocument(path, value);
			this.saveTable();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	private void createIfNotExistsFolder() {
		File file = new File(Table.STORAGE_DIR);
		if (!file.exists())
			file.mkdirs();
	}

	private String getPath(BigInteger key) {
		int size = 3;

		String path = Table.STORAGE_DIR;
		String str = key.toString();

		for (int start = 0; start < str.length(); start += size) {
			String part = str.substring(start, Math.min(str.length(), start + size));
			String basePath = "/" + part;

			if (start == str.length() - 1)
				path = path.concat(basePath + ".dat");
			else
				path = path.concat(basePath);
		}

		return path;
	}

	private void saveDocument(String path, Document value) throws IOException {
		File file = new File(path);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream s = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(s);
		outputStream.writeObject(value);

		outputStream.close();
	}

	private void saveTable() throws IOException {
		File file = new File(Table.TABLE_DIR);
		file.getParentFile().mkdirs();
		file.createNewFile();

		FileOutputStream s = new FileOutputStream(file, false);
		ObjectOutputStream outputStream = new ObjectOutputStream(s);
		outputStream.writeObject(this);

		outputStream.close();
	}
}
