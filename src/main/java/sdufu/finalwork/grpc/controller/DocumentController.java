package sdufu.finalwork.grpc.controller;

import java.math.BigInteger;

import com.google.protobuf.ByteString;

import io.grpc.stub.StreamObserver;
import sdufu.finalwork.grpc.database.model.Document;
import sdufu.finalwork.grpc.server.exception.DocumentException;
import sdufu.finalwork.grpc.server.exception.DocumentExceptionTypes;
import sdufu.finalwork.grpc.service.DocumentService;
import sdufu.finalwork.grpc.service.ProtoBigIntegerService;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.Database.APIResponseData;
import sdufu.finalwork.proto.database.Database.DelRequest;
import sdufu.finalwork.proto.database.Database.GetRequest;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceImplBase;

public class DocumentController extends DatabaseServiceImplBase {
	DocumentService documentService;

	public DocumentController(DocumentService documentService) {
		this.documentService = documentService;
	}

	@Override
	public void set(SetRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = ProtoBigIntegerService.read(request.getK());
		APIResponse.Builder response = APIResponse.newBuilder();

		try {
			ByteString data = request.getD();
			long timestamp = request.getTs();

			this.documentService.set(key, data.toByteArray(), timestamp);

			response.setE("SUCCESS");
			responseObserver.onNext(response.build());
		} catch (DocumentException e) {
			e.printStackTrace();

			Document doc = this.documentService.get(key);
			ByteString data = ByteString.copyFrom(doc.getData());

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
		Document document = this.documentService.get(key);

		APIResponse.Builder response = APIResponse.newBuilder();

		if (document != null) {
			ByteString data = ByteString.copyFrom(document.getData());
			APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(document.getVersion())
					.setTs(document.getTimestamp()).setData(data).build();

			response.setE("SUCCESS").setV(apiResponseData);
		} else {
			response.setE("ERROR");
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	@Override
	public void del(DelRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = ProtoBigIntegerService.read(request.getK());
		APIResponse.Builder response = APIResponse.newBuilder();

		try {
			long version = request.getVers();
			Document doc = version != 0 ? this.documentService.del(key, version) : this.documentService.del(key);

			ByteString data = ByteString.copyFrom(doc.getData());
			APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(doc.getVersion())
					.setTs(doc.getTimestamp()).setData(data).build();
			response.setE("SUCCESS").setV(apiResponseData);
		} catch (DocumentException e) {
			e.printStackTrace();

			String message = "ERROR";

			if (e.getType() == DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST) {
				message = "ERROR_NE";
			}

			if (e.getType() == DocumentExceptionTypes.DIFFERENT_DOCUMENT_VERSION) {
				message = "ERROR_WV";

				Document doc = this.documentService.get(key);
				ByteString data = ByteString.copyFrom(doc.getData());

				APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(doc.getVersion())
						.setTs(doc.getTimestamp()).setData(data).build();

				response.setV(apiResponseData);
			}

			response.setE(message);
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}
}
