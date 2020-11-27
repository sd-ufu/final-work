package sdufu.finalwork.grpc.service;

import java.math.BigInteger;

import com.google.protobuf.ByteString;

import sdufu.finalwork.proto.database.Database.ProtoBigInteger;

public class ProtoBigIntegerService {
	public static ProtoBigInteger write(BigInteger val) {
		ProtoBigInteger.Builder builder = ProtoBigInteger.newBuilder();
		ByteString bytes = ByteString.copyFrom(val.toByteArray());
		builder.setValue(bytes);
		return builder.build();
	}

	public static BigInteger read(ProtoBigInteger message) {
		ByteString bytes = message.getValue();
		return new BigInteger(bytes.toByteArray());
	}
}
