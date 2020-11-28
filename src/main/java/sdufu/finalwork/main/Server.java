package sdufu.finalwork.main;

import java.io.IOException;

import sdufu.finalwork.grpc.server.GRPCServer;

/*
 * Class to start server
 */
public class Server {
	/*
	 * Method to start server
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		GRPCServer server = new GRPCServer();
		server.start(9089);
	}
}
