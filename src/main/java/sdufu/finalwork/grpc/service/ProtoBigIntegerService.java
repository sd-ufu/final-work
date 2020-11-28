package sdufu.finalwork.grpc.service;

import java.math.BigInteger;

import com.google.protobuf.ByteString;

import sdufu.finalwork.proto.database.Database.ProtoBigInteger;

/*
 * Class to work with ProtoBigInteger
 */
public class ProtoBigIntegerService {
	/*
	 * Method to transform BigInteger on ProtoBigInteger
	 */
	public static ProtoBigInteger write(BigInteger val) {
		ProtoBigInteger.Builder builder = ProtoBigInteger.newBuilder();
		ByteString bytes = ByteString.copyFrom(val.toByteArray());
		builder.setValue(bytes);
		return builder.build();
	}

	/*
	 * Method to transform ProtoBigInteger on BigInteger
	 */
	public static BigInteger read(ProtoBigInteger message) {
		ByteString bytes = message.getValue();
		return new BigInteger(bytes.toByteArray());
	}
}
