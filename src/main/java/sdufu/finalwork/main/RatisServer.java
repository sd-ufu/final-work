package sdufu.finalwork.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import sdufu.finalwork.grpc.database.Database;
import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.ratis.Address;
import sdufu.finalwork.ratis.Server;

public class RatisServer {
	public static void main(String[] args) throws IOException, InterruptedException {
		String addressPath = System.getProperty("user.dir") + "/files/address";
		File file = new File(addressPath);

		List<Address> addresses = new ArrayList<Address>();

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				String[] lineSplited = line.split(" ");
				String name = lineSplited[0];
				String host = lineSplited[1];
				int addressPort = Integer.parseInt(lineSplited[2]);

				Address address = new Address(name, host, addressPort);
				addresses.add(address);
			}
		}

		String myName = args[0];

		Database database = new Database();
		Repository repository = new Repository(database);
		
		System.out.println("Starting state machine " + myName);

		Server server = new Server(myName, addresses, repository);
		server.start();
	}
}
