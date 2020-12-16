package sdufu.finalwork.ratis;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.ClientId;
import org.apache.ratis.protocol.Message;
import org.apache.ratis.protocol.RaftClientReply;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

public class Client {
	private List<Address> addresses;
	private RaftClient client;

	public Client(List<Address> addresses) {
		this.addresses = addresses;
	}

	public void start() {
		String raftGroupId = "raft_group____um";
		Map<String, InetSocketAddress> id2addr = new HashMap<>();
		for (Address address : addresses) {
			InetSocketAddress socketAddress = new InetSocketAddress(address.getHost(), address.getPort());
			id2addr.put(address.getName(), socketAddress);
		}

		List<RaftPeer> registeredAddresses = id2addr.entrySet().stream()
				.map(e -> new RaftPeer(RaftPeerId.valueOf(e.getKey()), e.getValue())).collect(Collectors.toList());

		RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)),
				registeredAddresses);

		RaftProperties raftProperties = new RaftProperties();

		this.client = RaftClient.newBuilder().setProperties(raftProperties).setRaftGroup(raftGroup)
				.setClientRpc(new GrpcFactory(new Parameters()).newRaftClientRpc(ClientId.randomId(), raftProperties))
				.build();
	}

	public Response send(Request data) throws IOException {
		ByteString request = ByteString.copyFrom(SerializationUtils.serialize(data));

		if (data.getOperation() == Operation.GET) {
			RaftClientReply getValue = client.sendReadOnly(Message.valueOf(request));
			byte[] b = getValue.getMessage().getContent().toByteArray();
			Response response = SerializationUtils.deserialize(b);

			return response;
		} else {
			RaftClientReply getValue = client.send(Message.valueOf(request));
			byte[] b = getValue.getMessage().getContent().toByteArray();
			Response response = SerializationUtils.deserialize(b);

			return response;
		}
	}
}
