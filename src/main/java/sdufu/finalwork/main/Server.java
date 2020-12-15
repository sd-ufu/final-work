package sdufu.finalwork.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sdufu.finalwork.grpc.server.GRPCServer;
import sdufu.finalwork.ratis.Address;

/*
 * Class to start server
 */
public class Server {
	/*
	 * Method to start server
	 */
	public static void main(String[] args) throws IOException, InterruptedException {
		int port = Integer.parseInt(args[0]);

		String addressPath = System.getProperty("user.dir") + "/files/address";
		File file = new File(addressPath);

		try (Scanner scanner = new Scanner(file)) {
			List<Address> addresses = new ArrayList<Address>();

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] lineSplited = line.split(" ");
				String name = lineSplited[0];
				String host = lineSplited[1];
				int addressPort = Integer.parseInt(lineSplited[2]);

				Address address = new Address(name, host, addressPort);
				addresses.add(address);
			}

			GRPCServer server = new GRPCServer();
			server.start(port, addresses);
		}
	}
}
