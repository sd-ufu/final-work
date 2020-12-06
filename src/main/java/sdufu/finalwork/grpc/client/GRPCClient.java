package sdufu.finalwork.grpc.client;

import java.math.BigInteger;
import java.util.Scanner;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

/*
 * Client server class
 */
public class GRPCClient extends Client {
	/*
	 * Method to start client server
	 */
	public void start(String host, int port) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		DatabaseServiceBlockingStub stub = DatabaseServiceGrpc.newBlockingStub(channel);

		this.showMenu();

		Scanner scanner = new Scanner(System.in);
		Integer opr = scanner.nextInt();

		while (opr != 0) {
			switch (opr) {
			case 1:
				this.set(scanner, stub);
				break;
			case 2:
				this.get(scanner, stub);
				break;
			case 3:
				this.deleteByKey(scanner, stub);
				break;
			case 4:
				this.deleteByKeyAndVersion(scanner, stub);
				break;
			case 5:
				this.testAndSet(scanner, stub);
				break;
			default:
				break;
			}

			System.out.println();
			this.showMenu();
			opr = scanner.nextInt();
		}
	}

	private void set(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();

		System.out.println("Enter the document DATA");
		String text = scanner.next();

		APIResponse response = super.startSetFlow(key.toString(), text, stub);
		System.out.println("SET RESPONSE = " + response);
	}

	private void get(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();

		APIResponse response = super.startGetFlow(key.toString(), stub);
		System.out.println("GET RESPONSE = " + response);
	}

	private void deleteByKey(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();

		APIResponse response = super.startDeleteByKeyFlow(key.toString(), stub);
		System.out.println("DELETE RESPONSE = " + response);
	}

	private void deleteByKeyAndVersion(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();

		System.out.println("Enter the document VERSION:");
		long version = scanner.nextLong();

		APIResponse response = super.startDeleteByKeyAndVersionFlow(key.toString(), Long.toString(version), stub);
		System.out.println("DELETE BY KEY AND VERSION RESPONSE = " + response);
	}

	private void testAndSet(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();

		System.out.println("Enter the document VERSION:");
		long version = scanner.nextLong();

		System.out.println("Enter the document DATA");
		String text = scanner.next();

		APIResponse response = super.startTestAndSetFlow(key.toString(), Long.toString(version), text, stub);
		System.out.println("TEST AND SET RESPONSE = " + response);
	}
}
