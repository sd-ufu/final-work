package sdufu.finalwork.grpc.service;

import java.math.BigInteger;

import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;
import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.database.model.Document;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.Database.APIResponseData;
import sdufu.finalwork.proto.database.Database.GetRequest;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceImplBase;;

public class DocumentService extends DatabaseServiceImplBase {
	private Repository repository;

	public DocumentService(Repository repository) {
		this.repository = repository;
	}

	@Override
	public void set(SetRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = ProtoBigIntegerService.read(request.getK());
		ByteString data = request.getD();
		long timestamp = request.getTs();

		Document doc = new Document();
		doc.setVersion(1);
		doc.setData(data.toByteArray());
		doc.setTimestamp(timestamp);

		this.repository.put(key, doc);

		try {
			APIResponse.Builder response = APIResponse.newBuilder();
			response.setE("SUCCESS");
			responseObserver.onNext(response.build());
		} catch (Exception e) {
			e.printStackTrace();

			APIResponse.Builder response = APIResponse.newBuilder();
			APIResponseData apiResponseData = APIResponseData.newBuilder().setTs(doc.getTimestamp())
					.setVer(doc.getVersion()).setData(data).build();

			response.setE("ERROR").setV(apiResponseData);
			responseObserver.onNext(response.build());
		} finally {
			responseObserver.onCompleted();
		}
	}

	@Override
	public void get(GetRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = ProtoBigIntegerService.read(request.getK());
		Document document = this.repository.get(key);

		APIResponse.Builder response = APIResponse.newBuilder();

		if (document != null) {
			ByteString data = ByteString.copyFrom(document.getData());
			System.out.println("D: " + document.getData().toString());
			APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(document.getVersion())
					.setTs(document.getTimestamp()).setData(data).build();
			response.setE("SUCCESS").setV(apiResponseData);
		} else {
			response.setE("ERROR");
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}
}
