package sdufu.finalwork.grpc.client.stream;

import java.math.BigInteger;
import java.util.Date;

import io.grpc.stub.StreamObserver;
import sdufu.finalwork.proto.database.Database.APIResponse;

public class ResponseStream {
	public static StreamObserver<APIResponse> build(Date date, BigInteger key, String method) {
		return new StreamObserver<APIResponse>() {
			@Override
			public void onNext(APIResponse value) {
				System.out.println(date + " => " + method.toUpperCase() + " ON NEXT: " + key);
				System.out.println(value);
			}

			@Override
			public void onError(Throwable t) {
				System.out.println(date + " => " + method.toUpperCase() + " ON ERROR: " + key);
			}

			@Override
			public void onCompleted() {
				System.out.println(date + " => " + method.toUpperCase() + " COMPLETED: " + key);
			}
		};
	}

	private ResponseStream() {
	}
}
