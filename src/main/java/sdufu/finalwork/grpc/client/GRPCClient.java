package sdufu.finalwork.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import sdufu.finalwork.proto.product.Product.APIResponse;
import sdufu.finalwork.proto.product.Product.ProdId;
import sdufu.finalwork.proto.product.Product.product;

import sdufu.finalwork.proto.product.ProductServiceGrpc;
import sdufu.finalwork.proto.product.ProductServiceGrpc.ProductServiceBlockingStub;

import java.util.Scanner;

public class GRPCClient {
	public static void main(String[] args) {
		ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 9089).usePlaintext().build();

		ProductServiceBlockingStub prodStub = ProductServiceGrpc.newBlockingStub(channel);

		GRPCClient.showMenu();

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		Integer opr = scanner.nextInt();

		while (opr != 0) {
			switch (opr) {
			case 1:
				System.out.println("Enter the product Name");
				String name = scanner.next();
				System.out.println("Enter the product price");
				Integer price = scanner.nextInt();
				System.out.println("Enter the product Stock");
				Integer stock = scanner.nextInt();
				product request = product.newBuilder().setProdName(name).setProdPrice(price).setProdStock(stock).build();
				APIResponse response = prodStub.addProduct(request);
				System.out.println("ADD RESPONSE");
				System.out.println(response);
				break;
			case 2:
				System.out.println("Enter the product id");
				Integer idToGet = scanner.nextInt();
				ProdId reqToGet = ProdId.newBuilder().setId(idToGet).build();
				APIResponse getResponse = prodStub.getProduct(reqToGet);
				System.out.println("GET RESPONSE");
				System.out.println(getResponse);
				break;
			case 3:
				System.out.println("Enter the product id");
				Integer id = scanner.nextInt();
				ProdId req = ProdId.newBuilder().setId(id).build();
				APIResponse responses = prodStub.deleteProduct(req);
				System.out.println("DELETE RESPONSE");
				System.out.println(responses);
				break;
			default:
				break;
			}

			System.out.println("");
			GRPCClient.showMenu();
			opr = scanner.nextInt();
		}

	}
	
	public static void showMenu() {
		System.out.println("Press 0 to stop client");
		System.out.println("Press 1 to add new product");
		System.out.println("Press 2 to get product");
		System.out.println("Press 3 to delete product");
		System.out.println("Enter the operation");
	}
}
