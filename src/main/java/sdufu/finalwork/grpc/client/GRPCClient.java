package sdufu.finalwork.grpc.client;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Scanner;

import com.google.protobuf.ByteString;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import sdufu.finalwork.proto.database.Database.APIResponse;
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
				System.out.println("Enter the document KEY");
				BigInteger key = scanner.nextBigInteger();

				System.out.println("Enter the document DATA");
				String text = scanner.next();
				System.out.println("MY_TEXT: " + text);
				ByteString data = ByteString.copyFrom(text.getBytes());

				SetRequest request = SetRequest.newBuilder().setTs(System.currentTimeMillis()).setK(1).setD(0, data).build();
				APIResponse response = stub.set(request);

				System.out.println("SET RESPONSE");
				System.out.println(response);
				break;
//			case 2:
//				System.out.println("Enter the product id");
//				Integer idToGet = scanner.nextInt();
//				ProdId reqToGet = ProdId.newBuilder().setId(idToGet).build();
//				APIResponse getResponse = prodStub.getProduct(reqToGet);
//				System.out.println("GET RESPONSE");
//				System.out.println(getResponse);
//				break;
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
	
	private void showMenu() {
		System.out.println("Press 0 to stop client");
		System.out.println("Press 1 to SET DOCUMENT");
//		System.out.println("Press 2 to get product");
//		System.out.println("Press 3 to delete product");
		System.out.println("Enter the operation");
	}
}
