//$Id$
package sdufu.finalwork.grpc.server;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import sdufu.finalwork.grpc.product.ProductService;

public class GRPCServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println("Starting grpc server");
		Server server = ServerBuilder.forPort(9089).addService(new ProductService()).build(); // create a instance of
																								// server
		server.start();
		System.out.println("Server Started at " + server.getPort());
		server.awaitTermination();
	}

}
