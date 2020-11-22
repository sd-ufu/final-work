//$Id$
package com.deepak.grpc.products;

import sdufu.finalwork.proto.product.Product.APIResponse;
import sdufu.finalwork.proto.product.Product.ProdId;
import sdufu.finalwork.proto.product.Product.product;
import sdufu.finalwork.proto.product.ProductServiceGrpc.ProductServiceImplBase;

import io.grpc.stub.StreamObserver;

public class ProductService extends ProductServiceImplBase {

	@Override
	public void getProduct(ProdId request, StreamObserver<APIResponse> responseObserver) {
		try {
			Products product = new Products();
			product.setId(request.getId());
			product.setName("PRODUCT_NAME");
			product.setPrice(10);
			product.setStock(1);
			System.out.println("rset");
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage(product.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		APIResponse.Builder response = APIResponse.newBuilder();
		response.setMessageCode(0).setResponseMessage("Sorry");
	}

	@Override
	public void addProduct(product request, StreamObserver<APIResponse> responseObserver) {
		String name = request.getProdName();
		Integer price = request.getProdPrice();
		Integer stock = request.getProdStock();
		Products product = new Products();
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		try {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("values inserted");
			System.out.println("values inserted");
		} catch (Exception e) {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("values not inserted");
			e.printStackTrace();
		}

	}

	@Override
	public void deleteProduct(ProdId request, StreamObserver<APIResponse> responseObserver) {
		try {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("deleted: " + request.getId());
		} catch (Exception e) {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("failed");
			e.printStackTrace();
		}
	}
}
