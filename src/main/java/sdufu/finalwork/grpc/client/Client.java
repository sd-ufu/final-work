package sdufu.finalwork.grpc.client;

import java.math.BigInteger;

import com.google.protobuf.ByteString;

import sdufu.finalwork.grpc.service.ProtoBigIntegerService;
import sdufu.finalwork.proto.database.Database.APIResponse;
import sdufu.finalwork.proto.database.Database.DelRequest;
import sdufu.finalwork.proto.database.Database.GetRequest;
import sdufu.finalwork.proto.database.Database.ProtoBigInteger;
import sdufu.finalwork.proto.database.Database.SetAndTestData;
import sdufu.finalwork.proto.database.Database.SetRequest;
import sdufu.finalwork.proto.database.Database.TestAndSetRequest;
import sdufu.finalwork.proto.database.DatabaseServiceGrpc.DatabaseServiceBlockingStub;

public abstract class Client {
	public abstract void start(String hots, int port);

	/*
	 * Method to start flow to SET new document
	 */
	public APIResponse startSetFlow(String keyString, String text, DatabaseServiceBlockingStub stub) {
		BigInteger key = new BigInteger(keyString);
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		ByteString data = ByteString.copyFrom(text.getBytes());

		SetRequest request = SetRequest.newBuilder().setTs(System.currentTimeMillis()).setK(transformedKey).setD(data)
				.build();

		return stub.set(request);
	}

	/*
	 * Method to start flow to GET new document
	 */
	public APIResponse startGetFlow(String keyString, DatabaseServiceBlockingStub stub) {
		BigInteger key = new BigInteger(keyString);
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		GetRequest request = GetRequest.newBuilder().setK(transformedKey).build();

		return stub.get(request);
	}

	/*
	 * Method to start flow to DELETE document by key
	 */
	public APIResponse startDeleteByKeyFlow(String keyString, DatabaseServiceBlockingStub stub) {
		BigInteger key = new BigInteger(keyString);
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		DelRequest request = DelRequest.newBuilder().setK(transformedKey).build();

		return stub.del(request);
	}

	/*
	 * Method to start flow to DELETE document by key and version
	 */
	public APIResponse startDeleteByKeyAndVersionFlow(String keyString, String versionString,
			DatabaseServiceBlockingStub stub) {
		BigInteger key = new BigInteger(keyString);
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		long version = Long.parseLong(versionString);

		DelRequest request = DelRequest.newBuilder().setK(transformedKey).setVers(version).build();

		return stub.del(request);
	}

	/*
	 * Method to start flow to TEST AND SET document by key and version
	 */
	public APIResponse startTestAndSetFlow(String keyString, String versionString, String text,
			DatabaseServiceBlockingStub stub) {
		BigInteger key = new BigInteger(keyString);
		ProtoBigInteger transformedKey = ProtoBigIntegerService.write(key);

		long version = Long.parseLong(versionString);

		ByteString data = ByteString.copyFrom(text.getBytes());

		SetAndTestData setAndTestData = SetAndTestData.newBuilder().setD(data).setTs(System.currentTimeMillis())
				.build();

		TestAndSetRequest request = TestAndSetRequest.newBuilder().setVers(version).setK(transformedKey)
				.setV(setAndTestData).build();

		return stub.testAndSet(request);
	}

	/*
	 * Method to show menu options
	 */
	protected void showMenu() {
		System.out.println("Enter the operation");
		System.out.println(this.getText("0", "to STOP CLIENT"));
		System.out.println(this.getText("1", "to SET DOCUMENT"));
		System.out.println(this.getText("2", "to GET DOCUMENT"));
		System.out.println(this.getText("3", "to DELETE DOCUMENT BY KEY"));
		System.out.println(this.getText("4", "to DELETE DOCUMENT BY KEY AND VERSION"));
		System.out.println(this.getText("5", "to TEST AND SET DOCUMENT"));
	}

	/*
	 * Method to styling or menu
	 */
	protected String getText(String opt, String text) {
		final String BLUE = "\u001B[34m";
		final String BOLD = "\033[0;1m";
		final String RESET_BOLD = "\033[0;0m";
		final String RESET_COLOR = "\u001B[0m";

		String str = "Press " + BOLD + BLUE;
		str = str.concat(opt);
		str = str.concat(RESET_COLOR + RESET_BOLD);
		str = str.concat(" " + text);

		return str;
	}
}
