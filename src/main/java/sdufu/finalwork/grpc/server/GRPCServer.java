package sdufu.finalwork.grpc.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sdufu.finalwork.grpc.controller.DocumentController;
import sdufu.finalwork.grpc.database.Database;
import sdufu.finalwork.grpc.database.DatabaseFactory;
import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.database.io.DatabaseIO;
import sdufu.finalwork.grpc.database.io.SynchronizeDatabase;
import sdufu.finalwork.grpc.service.DocumentService;

/*
 * Server class
 */
public class GRPCServer {
	/*
	 * Method to start server
	 */
	public void start(int port) throws IOException, InterruptedException {
		System.out.println("Starting grpc server");

		Database database = DatabaseFactory.build();
		DatabaseIO databaseIO = new DatabaseIO();
		Repository repository = new Repository(database, databaseIO);
		DocumentService documentService = new DocumentService(repository, databaseIO);
		DocumentController documentController = new DocumentController(documentService);
		
		System.out.println(database);

		Server server = ServerBuilder.forPort(port).addService(documentController).build();
		server.start();

		SynchronizeDatabase synchronizeDatabase = new SynchronizeDatabase(database, databaseIO);
		Thread syncDb = new Thread(synchronizeDatabase);

		syncDb.start();

		System.out.println("Server Started at " + server.getPort());
		server.awaitTermination();
	}
}
