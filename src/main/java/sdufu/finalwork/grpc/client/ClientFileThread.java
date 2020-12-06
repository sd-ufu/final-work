package sdufu.finalwork.grpc.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

public class ClientFileThread implements Runnable {
	private File file;
	private Client client;
	private DatabaseServiceBlockingStub stub;

	public ClientFileThread(File file, Client client, DatabaseServiceBlockingStub stub) {
		this.file = file;
		this.client = client;
		this.stub = stub;
	}

	@Override
	public void run() {
		Scanner myReader;

		try {
			myReader = new Scanner(this.file);
			String resp = "";

			while (myReader.hasNextLine()) {
				String line = myReader.nextLine();
				String[] lineSplited = line.split(" ");
				String action = lineSplited[0];
				String key = lineSplited[1];

				resp = resp + line + ":";

				APIResponse response = null;

				if (action.equals("1")) {
					resp = resp + " SET\n";
					response = this.client.startSetFlow(key, lineSplited[2], this.stub);
				}

				if (action.equals("2")) {
					resp = resp + " GET\n";
					response = this.client.startGetFlow(key, this.stub);
				}

				if (action.equals("3")) {
					resp = resp + " DELETE\n";
					response = this.client.startDeleteByKeyFlow(key, this.stub);
				}

				if (action.equals("4")) {
					resp = resp + " DELETE BY KEY AND VERSION\n";
					response = this.client.startDeleteByKeyAndVersionFlow(key, lineSplited[2], this.stub);
				}

				if (action.equals("5")) {
					resp = resp + " TEST AND SET\n";
					response = this.client.startTestAndSetFlow(key, lineSplited[2], lineSplited[3], this.stub);
				}

				resp = resp + response.toString() + "\n\n";
			}

			String fileRespPath = System.getProperty("user.dir") + "/files/output/" + this.file.getName() + "_RESP.txt";
			FileWriter fileWriter = new FileWriter(fileRespPath);
			fileWriter.write(resp);
			fileWriter.close();

			System.out.println(this.file.getName() + " FINISHED");

			myReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
