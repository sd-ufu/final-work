package sdufu.finalwork.main;

import sdufu.finalwork.grpc.client.GRPCClient;
import sdufu.finalwork.grpc.client.GRPCClientWithFileBlocking;

/*
 * Class to start client server
 */
public class Client {
	/*
	 * Method to start client server
	 */
	public static void main(String[] args) {
		sdufu.finalwork.grpc.client.Client client = null;
		String scope = System.getenv("SCOPE");

		if (scope == null) {
			scope = "TERMINAL";
		}

		if (scope.equals("TERMINAL")) {
			client = new GRPCClient();
		} else {
			client = new GRPCClientWithFileBlocking();
		}

		client.start("localhost", 9089);
	}
}
