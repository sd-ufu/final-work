package sdufu.finalwork.grpc.server;

import java.io.IOException;
import java.util.List;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sdufu.finalwork.grpc.controller.DocumentController;
import sdufu.finalwork.grpc.service.DocumentService;
import sdufu.finalwork.ratis.Address;
import sdufu.finalwork.ratis.Client;

/*
 * Server class
 */
public class GRPCServer {
	/*
	 * Method to start server
	 */
	public void start(int port, List<Address> addresses) throws IOException, InterruptedException {
		System.out.println("Starting grpc server");

		Client client = new Client(addresses);
		DocumentService documentService = new DocumentService(client);
		DocumentController documentController = new DocumentController(documentService);

		client.start();

		Server server = ServerBuilder.forPort(port).addService(documentController).build();
		server.start();

		System.out.println("Server Started at " + server.getPort());
		server.awaitTermination();
	}
}
