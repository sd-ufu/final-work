package sdufu.finalwork.grpc.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

public class GRPCClientWithFileBlocking extends Client {
	/*
	 * Method to start client server
	 */
	public void start(String host, int port) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		DatabaseServiceBlockingStub stub = DatabaseServiceGrpc.newBlockingStub(channel);
		GRPCClientWithFileBlocking that = this;

		Thread ta = new Thread(new Runnable() {
			@Override
			public void run() {
				String basePath = "/Users/devatomic/Desktop/ivan/ufu/sd/";
				String fileName = System.getenv("FILE_NAME");

				File fileA = new File(basePath + fileName);
				Scanner myReader;

				try {
					myReader = new Scanner(fileA);

					while (myReader.hasNextLine()) {
						String line = myReader.nextLine();
						String[] lineSplited = line.split(" ");
						String action = lineSplited[0];
						String key = lineSplited[1];

						System.out.println(fileName + "_LINE: " + line);

						if (action.equals("1")) {
							that.startSetFlow(key, lineSplited[2], stub);
						}

						if (action.equals("2")) {
							that.startGetFlow(key, stub);
						}

						if (action.equals("3")) {
							that.startDeleteByKeyFlow(key, stub);
						}

						if (action.equals("4")) {
							that.startDeleteByKeyAndVersionFlow(key, lineSplited[2], stub);
						}

						if (action.equals("5")) {
							that.startTestAndSetFlow(key, lineSplited[2], lineSplited[3], stub);
						}
					}

					myReader.close();
					System.out.println(fileName + "_FINISHED");
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});

		ta.start();

		while (true) {
		}
	}
}
