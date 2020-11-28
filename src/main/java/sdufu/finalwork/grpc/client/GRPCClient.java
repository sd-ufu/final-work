package sdufu.finalwork.grpc.client;

import java.math.BigInteger;
import java.util.Scanner;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sdufu.finalwork.grpc.service.ProtoBigIntegerService;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.Database.DelRequest;
import sdufu.finalwork.proto.database.Database.GetRequest;
import sdufu.finalwork.proto.database.Database.ProtoBigInteger;
import sdufu.finalwork.proto.database.Database.SetAndTestData;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.Database.TestAndSetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

/*
 * Client server class
 */
public class GRPCClient {
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
				this.startSetFlow(scanner, stub);
				break;
			case 2:
				this.startGetFlow(scanner, stub);
				break;
			case 3:
				this.startDeleteByKeyFlow(scanner, stub);
				break;
			case 4:
				this.startDeleteByKeyAndVersionFlow(scanner, stub);
				break;
			case 5:
				this.startTestAndSetFlow(scanner, stub);
				break;
			default:
				break;
			}

			System.out.println("");
			this.showMenu();
			opr = scanner.nextInt();
		}
	}

	/*
	 * Method to start flow to SET new document
	 */
	private void startSetFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		System.out.println("Enter the document DATA");
		String text = scanner.next();

		ByteString data = ByteString.copyFrom(text.getBytes());

		SetRequest request = SetRequest.newBuilder().setTs(System.currentTimeMillis()).setK(transformedKey).setD(data)
				.build();
		APIResponse response = stub.set(request);

		System.out.println("SET RESPONSE");
		System.out.println(response);
	}

	/*
	 * Method to start flow to GET new document
	 */
	private void startGetFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY:");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		GetRequest request = GetRequest.newBuilder().setK(transformedKey).build();
		APIResponse response = stub.get(request);

		System.out.println("GET RESPONSE");
		System.out.println(response);
	}

	/*
	 * Method to start flow to DELETE document by key
	 */
	private void startDeleteByKeyFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY:");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		DelRequest request = DelRequest.newBuilder().setK(transformedKey).build();
		APIResponse response = stub.del(request);

		System.out.println("DELETE BY KEY RESPONSE");
		System.out.println(response);
	}

	/*
	 * Method to start flow to DELETE document by key and version
	 */
	private void startDeleteByKeyAndVersionFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY:");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		System.out.println("Enter the document VERSION:");
		long version = scanner.nextLong();

		DelRequest request = DelRequest.newBuilder().setK(transformedKey).setVers(version).build();
		APIResponse response = stub.del(request);

		System.out.println("DELETE BY KEY AND VERSION RESPONSE");
		System.out.println(response);
	}

	private void startTestAndSetFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY:");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		System.out.println("Enter the document VERSION:");
		long version = scanner.nextLong();

		System.out.println("Enter the document DATA");
		String text = scanner.next();
		ByteString data = ByteString.copyFrom(text.getBytes());

		SetAndTestData setAndTestData = SetAndTestData.newBuilder().setD(data).setTs(System.currentTimeMillis())
				.build();

		TestAndSetRequest request = TestAndSetRequest.newBuilder().setVers(version).setK(transformedKey)
				.setV(setAndTestData).build();
		APIResponse response = stub.testAndSet(request);

		System.out.println("TEST AND SET RESPONSE");
		System.out.println(response);
	}

	/*
	 * Method to show menu options
	 */
	private void showMenu() {
		System.out.println("Enter the operation");
		System.out.println(this.getText("0", "to STOP CLIENT"));
		System.out.println(this.getText("1", "to SET DOCUMENT"));
		System.out.println(this.getText("2", "to GET DOCUMENT"));
		System.out.println(this.getText("3", "to DELETE DOCUMENT BY KEY"));
		System.out.println(this.getText("4", "to DELETE DOCUMENT BY KEY AND VERSION"));
		System.out.println(this.getText("5", "to TEST AND SET DOCUMENT"));
	}

	/*
	 * Method to styling or menu
	 */
	private String getText(String opt, String text) {
		final String BLUE = "\u001B[34m";
		final String BOLD = "\033[0;1m";
		final String RESET_BOLD = "\033[0;0m";
		final String RESET_COLOR = "\u001B[0m";

		String str = "Press " + BOLD + BLUE;
		str = str.concat(opt);
		str = str.concat(RESET_COLOR + RESET_BOLD);
		str = str.concat(" " + text);

		return str;
	}
}
