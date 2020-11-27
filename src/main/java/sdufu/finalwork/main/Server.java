package sdufu.finalwork.main;

import java.io.IOException;

import sdufu.finalwork.grpc.server.GRPCServer;

public class Server {
	public static void main(String[] args) throws IOException, InterruptedException {
		GRPCServer server = new GRPCServer();
		server.start(9089);
	}
}
