//$Id$
package com.other.shopcl.client;

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
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter the operation");
		Integer opr = scanner.nextInt();
		switch (opr) {
		case 1:
			System.out.println("Enter the product Name");
			String name = scanner.next();
			System.out.println("Enter the product price");
			Integer price = scanner.nextInt();
			System.out.println("Enter the product Name");
			Integer stock = scanner.nextInt();
			product request = product.newBuilder().setProdName(name).setProdPrice(price).setProdStock(stock).build();
			APIResponse response = prodStub.addProduct(request);
			System.out.println(response);
			break;
		case 2:
			System.out.println("Enter the product id");
			Integer id = scanner.nextInt();
			ProdId req = ProdId.newBuilder().setId(id).build();
			APIResponse responses = prodStub.getProduct(req);
			System.out.println(responses);
			break;
		default:
			break;
		}

	}
}
