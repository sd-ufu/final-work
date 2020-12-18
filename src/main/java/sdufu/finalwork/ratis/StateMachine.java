package sdufu.finalwork.ratis;

import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.ratis.proto.RaftProtos;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.statemachine.TransactionContext;
import org.apache.ratis.statemachine.impl.BaseStateMachine;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import sdufu.finalwork.grpc.database.Repository;
import sdufu.finalwork.grpc.database.model.Document;

public class StateMachine extends BaseStateMachine {
	private Repository repository;

	public StateMachine(Repository repository) {
		this.repository = repository;
	}

	@Override
	public CompletableFuture<Message> query(Message request) {
		Request req = SerializationUtils.deserialize(request.getContent().toByteArray());
		BigInteger key = req.getKey();

		Response response = new Response();
		Document doc = this.repository.get(req.getKey());

		response.setDocument(doc);
		response.setKey(key);

		ByteString result = ByteString.copyFrom(SerializationUtils.serialize(response));

		LOG.debug("GET: {} = {}", key, doc);

		System.out.println("DATABASE: " + this.repository.database.toString());

		return CompletableFuture.completedFuture(Message.valueOf(result));
	}

	@Override
	public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
		RaftProtos.LogEntryProto entry = trx.getLogEntry();
		ByteString data = entry.getStateMachineLogEntry().getLogData();
		Request req = SerializationUtils.deserialize(data.toByteArray());

		Response response = new Response();

		if (req.getOperation() == Operation.SET || req.getOperation() == Operation.TEST_AND_SET) {
			Document doc = new Document();
			doc.setData(req.getData());
			doc.setTimestamp(req.getTimestamp());
			doc.setVersion(req.getVersion());

			this.repository.put(req.getKey(), doc);

			response.setDocument(doc);
			response.setKey(req.getKey());
		}

		if (req.getOperation() == Operation.DEL || req.getOperation() == Operation.DEL_BY_VERSION) {
			Document doc = this.repository.remove(req.getKey());

			response.setDocument(doc);
			response.setKey(req.getKey());
		}

		ByteString resp = ByteString.copyFrom(SerializationUtils.serialize(response));
		CompletableFuture<Message> future = CompletableFuture.completedFuture(Message.valueOf(resp));

		RaftProtos.RaftPeerRole role = trx.getServerRole();
		LOG.info("{}:{} {} {}", req.getOperation(), role, getId(), req.getKey());

		return future;
	}
}
