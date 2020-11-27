package sdufu.finalwork.grpc.service;

import java.math.BigInteger;
import java.util.List;

import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;
import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.database.model.Document;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.Database.APIResponseData;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceImplBase;;

public class DocumentService extends DatabaseServiceImplBase {
	private Repository repository;

	public DocumentService(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void set(SetRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = new BigInteger(Long.toString(request.getK()));
		List<ByteString> data = request.getDList();
		long timestamp = request.getTs();

		Document doc = new Document();
		doc.setVersion(1);
		doc.setData((ByteString[]) data.toArray());
		doc.setTimestamp(timestamp);

		this.repository.put(key, doc);

		try {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setE("SUCCESS");
			responseObserver.onNext(response.build());
		} catch (Exception e) {
			e.printStackTrace();

			APIResponse.Builder response = APIResponse.newBuilder();
			APIResponseData.Builder builder = APIResponseData.newBuilder().setTs(doc.getTimestamp())
					.setVer(doc.getVersion());

			for (int i = 0; i < data.size(); i++) {
				builder.setData(i, data.get(i));
			}

			APIResponseData apiResponseData = builder.build();

			response.setE("ERROR").setV(apiResponseData);
			responseObserver.onNext(response.build());
		} finally {
			responseObserver.onCompleted();
		}
	}
}
