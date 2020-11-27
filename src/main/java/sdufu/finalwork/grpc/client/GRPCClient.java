package sdufu.finalwork.grpc.client;

import java.math.BigInteger;
import java.util.Scanner;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sdufu.finalwork.grpc.service.ProtoBigIntegerService;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.Database.GetRequest;
import sdufu.finalwork.proto.database.Database.ProtoBigInteger;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

public class GRPCClient {
	public void start(String host, int port) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();

		DatabaseServiceBlockingStub stub = DatabaseServiceGrpc.newBlockingStub(channel);

		this.showMenu();

		@SuppressWarnings("resource")
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
//			case 3:
//				System.out.println("Enter the product id");
//				Integer id = scanner.nextInt();
//				ProdId req = ProdId.newBuilder().setId(id).build();
//				APIResponse responses = prodStub.deleteProduct(req);
//				System.out.println("DELETE RESPONSE");
//				System.out.println(responses);
//				break;
			default:
				break;
			}

			System.out.println("");
			this.showMenu();
			opr = scanner.nextInt();
		}
	}

	private void startSetFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the document KEY");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		System.out.println("Enter the document DATA");
		String text = scanner.next();
		System.out.println("MY_TEXT: " + text);
		ByteString data = ByteString.copyFrom(text.getBytes());

		SetRequest request = SetRequest.newBuilder().setTs(System.currentTimeMillis()).setK(transformedKey).setD(data)
				.build();
		APIResponse response = stub.set(request);

		System.out.println("SET RESPONSE");
		System.out.println(response);
	}

	private void startGetFlow(Scanner scanner, DatabaseServiceBlockingStub stub) {
		System.out.println("Enter the product id");
		BigInteger key = scanner.nextBigInteger();
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		GetRequest request = GetRequest.newBuilder().setK(transformedKey).build();
		APIResponse response = stub.get(request);

		System.out.println("GET RESPONSE");
		System.out.println(response);
	}

	private void showMenu() {
		System.out.println("Enter the operation");
		System.out.println(this.getText("0", "to STOP CLIENT"));
		System.out.println(this.getText("1", "to SET DOCUMENT"));
		System.out.println(this.getText("2", "to GET DOCUMENT"));
	}

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
