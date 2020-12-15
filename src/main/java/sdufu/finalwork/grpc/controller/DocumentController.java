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
import sdufu.finalwork.proto.database.Database.SetAndTestData;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.Database.TestAndSetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceImplBase;

/*
 * Class to implement GRPC interface
 */
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
		} catch (DocumentException e) {
			e.printStackTrace();

			try {
				Document doc = this.documentService.get(key);
				ByteString data = ByteString.copyFrom(doc.getData());

				APIResponseData apiResponseData = APIResponseData.newBuilder().setTs(doc.getTimestamp())
						.setVer(doc.getVersion()).setData(data).build();

				response.setE("ERROR").setV(apiResponseData);
			} catch (DocumentException e1) {
				e1.printStackTrace();
			}
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	@Override
	public void get(GetRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = ProtoBigIntegerService.read(request.getK());
		APIResponse.Builder response = APIResponse.newBuilder();

		try {
			Document document = this.documentService.get(key);
			
			if (document == null) {
				throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
			}

			ByteString data = ByteString.copyFrom(document.getData());
			APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(document.getVersion())
					.setTs(document.getTimestamp()).setData(data).build();

			response.setE("SUCCESS").setV(apiResponseData);
		} catch (DocumentException e) {
			e.printStackTrace();

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

			if (doc == null) {
				throw new DocumentException(DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST);
			}

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

				try {
					Document doc = this.documentService.get(key);

					ByteString data = ByteString.copyFrom(doc.getData());

					APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(doc.getVersion())
							.setTs(doc.getTimestamp()).setData(data).build();

					response.setV(apiResponseData);
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}

			response.setE(message);
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}

	@Override
	public void testAndSet(TestAndSetRequest request, StreamObserver<APIResponse> responseObserver) {
		BigInteger key = ProtoBigIntegerService.read(request.getK());
		APIResponse.Builder response = APIResponse.newBuilder();

		try {
			long version = request.getVers();

			SetAndTestData requestData = request.getV();
			ByteString data = requestData.getD();
			long timestamp = requestData.getTs();

			Document savedDoc = this.documentService.testAndSet(key, version, data.toByteArray(), timestamp);
			ByteString savedData = ByteString.copyFrom(savedDoc.getData());

			APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(savedDoc.getVersion())
					.setTs(savedDoc.getTimestamp()).setData(savedData).build();

			response.setV(apiResponseData).setE("SUCCESS");
		} catch (DocumentException e) {
			e.printStackTrace();

			String message = "ERROR";

			if (e.getType() == DocumentExceptionTypes.DOCUMENT_DOES_NOT_EXIST) {
				message = "ERROR_NE";
			}

			if (e.getType() == DocumentExceptionTypes.DIFFERENT_DOCUMENT_VERSION) {
				message = "ERROR_WV";

				try {
					Document doc = this.documentService.get(key);

					ByteString data = ByteString.copyFrom(doc.getData());

					APIResponseData apiResponseData = APIResponseData.newBuilder().setVer(doc.getVersion())
							.setTs(doc.getTimestamp()).setData(data).build();

					response.setV(apiResponseData);
				} catch (DocumentException e1) {
					e1.printStackTrace();
				}
			}

			response.setE(message);
		}

		responseObserver.onNext(response.build());
		responseObserver.onCompleted();
	}
}
