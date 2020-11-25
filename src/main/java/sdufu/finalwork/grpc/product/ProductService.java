//$Id$
package sdufu.finalwork.grpc.product;

import sdufu.finalwork.grpc.document.Document;
import sdufu.finalwork.grpc.document.Table;
import sdufu.finalwork.grpc.document.TableFactory;
import sdufu.finalwork.proto.product.Product.APIResponse;
import sdufu.finalwork.proto.product.Product.ProdId;
import sdufu.finalwork.proto.product.Product.product;
import sdufu.finalwork.proto.product.ProductServiceGrpc.ProductServiceImplBase;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import io.grpc.stub.StreamObserver;

public class ProductService extends ProductServiceImplBase {

	static List<Products> products = new ArrayList<Products>();
	static Table table = TableFactory.getTable();

	@Override
	public void getProduct(ProdId request, StreamObserver<APIResponse> responseObserver) {
		try {
			int id = request.getId();
			Products product = null;

			System.out.println("GET: " + id);

			for (Products p : ProductService.products) {
				if (p.getId() == id)
					product = p;
			}

			Document doc = table.get(new BigInteger(Integer.toString(id)));
			System.out.println("DOCUMENT: " + doc.getTimestamp());
			System.out.println(doc);

			if (product != null)
				System.out.println("FIND: " + product.toString());
			else
				throw new Exception("ID NOT FOUND");

			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage(product.toString());
			responseObserver.onNext(response.build());
		} catch (Exception e) {
			e.printStackTrace();

			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("Sorry");
			responseObserver.onNext(response.build());
		} finally {
			responseObserver.onCompleted();
		}
	}

	@Override
	public void addProduct(product request, StreamObserver<APIResponse> responseObserver) {
		String name = request.getProdName();
		Integer price = request.getProdPrice();
		Integer stock = request.getProdStock();
		int id = (int) System.currentTimeMillis();
		Products product = new Products();
		product.setId(id);
		product.setName(name);
		product.setPrice(price);
		product.setStock(stock);
		try {
			ProductService.products.add(product);
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("values inserted: " + id);
			System.out.println("values inserted");
			System.out.println("ID: " + id);
			System.out.println("NAME: " + name);
			System.out.println("PRICE: " + price);
			System.out.println("STOCK: " + stock);
			Document doc = new Document();
			doc.setTimestamp(id);
			table.put(new BigInteger(Integer.toString(id)), doc);

			responseObserver.onNext(response.build());
		} catch (Exception e) {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("values not inserted: " + id);
			e.printStackTrace();

			responseObserver.onNext(response.build());
		} finally {
			responseObserver.onCompleted();
		}
	}

	@Override
	public void deleteProduct(ProdId request, StreamObserver<APIResponse> responseObserver) {
		int id = request.getId();
		int index = -1;

		System.out.println("DELETE: " + id);

		try {
			for (int i = 0; i < ProductService.products.size(); i++) {
				Products p = ProductService.products.get(i);

				if (p.getId() != id)
					continue;

				index = i;
				break;
			}

			if (index == -1)
				throw new Exception("ID NOT FOUND");

			ProductService.products.remove(index);

			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("deleted: " + id);

			responseObserver.onNext(response.build());
		} catch (Exception e) {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setMessageCode(0).setResponseMessage("failed: " + id);
			e.printStackTrace();

			responseObserver.onNext(response.build());
		} finally {
			responseObserver.onCompleted();
		}
	}
}
