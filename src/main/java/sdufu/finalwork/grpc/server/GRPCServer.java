package sdufu.finalwork.grpc.server;

import java.io.IOException;
import java.util.List;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sdufu.finalwork.grpc.controller.DocumentController;
import sdufu.finalwork.grpc.database.Database;
import sdufu.finalwork.grpc.database.Repository;
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

		Database database = new Database();
		Client client = new Client(addresses);
		DocumentService documentService = new DocumentService(client);
		DocumentController documentController = new DocumentController(documentService);

		Repository repository = new Repository(database);
		Thread[] ts = new Thread[addresses.size()];

		for (int i = 0; i < addresses.size(); i++) {
			Address address = addresses.get(i);
			ts[i] = new Thread(new Runnable() {
				@Override
				public void run() {
					sdufu.finalwork.ratis.Server server = new sdufu.finalwork.ratis.Server(address.getName(), addresses,
							repository);
					try {
						System.out.println("START_THREAD: " + address.getName());
						server.start();
					} catch (IOException | InterruptedException e) {
						System.out.println("ERROR_THREAD: " + address.getName());
						e.printStackTrace();
					}
				}
			});

			ts[i].start();
		}

		client.start();

		Server server = ServerBuilder.forPort(port).addService(documentController).build();
		server.start();

		System.out.println("Server Started at " + server.getPort());
		server.awaitTermination();
	}
}
