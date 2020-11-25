package sdufu.finalwork.grpc.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class TableFactory {
	public static final Table getTable() {
		try {
			File file = new File(Table.TABLE_DIR);
			file.getParentFile().mkdirs();
			file.createNewFile();

			FileInputStream s = new FileInputStream(file);

			try (ObjectInputStream inputStream = new ObjectInputStream(s)) {
				Table table = (Table) inputStream.readObject();
				System.out.println("TABLE: " + table.toString());
				return table;
			} catch (Exception e) {
				return new Table();
			}
		} catch (Exception e2) {
			return new Table();
		}
	}

}
