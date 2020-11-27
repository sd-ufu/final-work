package sdufu.finalwork.grpc.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sdufu.finalwork.grpc.controller.DocumentController;
import sdufu.finalwork.grpc.database.Database;
import sdufu.finalwork.grpc.database.DatabaseFactory;
import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.service.DocumentService;

public class GRPCServer {
	public void start(int port) throws IOException, InterruptedException {
		System.out.println("Starting grpc server");

		Database database = DatabaseFactory.build();
		Repository repository = new Repository(database);
		DocumentService documentService = new DocumentService(repository);
		DocumentController documentController = new DocumentController(documentService);

		Server server = ServerBuilder.forPort(port).addService(documentController).build();
		server.start();

		System.out.println("Server Started at " + server.getPort());
		server.awaitTermination();
	}
}
