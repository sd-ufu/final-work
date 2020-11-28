package sdufu.finalwork.main;

import sdufu.finalwork.grpc.client.GRPCClient;

/*
 * Class to start client server
 */
public class Client {
	/*
	 * Method to start client server
	 */
	public static void main(String[] args) {
		GRPCClient client = new GRPCClient();
		client.start("localhost", 9089);
	}
}
