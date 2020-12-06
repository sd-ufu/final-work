package sdufu.finalwork.grpc.client;

import java.io.File;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

public class GRPCClientWithFile extends Client {
	/*
	 * Method to start client server
	 */
	public void start(String host, int port) {
		String inputPath = System.getProperty("user.dir") + "/files/input";
		File directory = new File(inputPath);
		File[] files = directory.listFiles();

		for (File file : files) {
			ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
			DatabaseServiceBlockingStub stub = DatabaseServiceGrpc.newBlockingStub(channel);

			ClientFileThread clientFileThread = new ClientFileThread(file, this, stub);
			new Thread(clientFileThread).start();
		}
	}
}
