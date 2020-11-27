package sdufu.finalwork.main;

import sdufu.finalwork.grpc.client.GRPCClient;

public class Client {
	public static void main(String[] args) {
		GRPCClient client = new GRPCClient();
		client.start("localhost", 9089);
	}
}
