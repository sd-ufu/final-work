package sdufu.finalwork.ratis;

import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.LifeCycle;

import sdufu.finalwork.grpc.database.Repository;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Server {
	private List<Address> addresses;
	private Repository repository;
	private String id;

	public Server(String id, List<Address> addresses, Repository repository) {
		this.addresses = addresses;
		this.repository = repository;
		this.id = id;
	}

	public void start() throws IOException, InterruptedException {
		String raftGroupId = "raft_group____um";

		Map<String, InetSocketAddress> id2addr = new HashMap<>();
		for (Address address : this.addresses) {
			InetSocketAddress socketAddress = new InetSocketAddress(address.getHost(), address.getPort());
			id2addr.put(address.getName(), socketAddress);
		}

		List<RaftPeer> registeredAddresses = id2addr.entrySet().stream()
				.map(e -> new RaftPeer(RaftPeerId.valueOf(e.getKey()), e.getValue())).collect(Collectors.toList());

		RaftPeerId myId = RaftPeerId.valueOf(this.id);

		if (registeredAddresses.stream().noneMatch(p -> p.getId().equals(myId))) {
			System.out.println("Identificador " + this.id + " é inválido.");
			System.exit(1);
		}

		RaftProperties properties = new RaftProperties();
		properties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
		GrpcConfigKeys.Server.setPort(properties, id2addr.get(this.id).getPort());

		File storage = new File(Storage.DIR.value + "/" + myId);
		RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(storage));

		RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)),
				registeredAddresses);

		StateMachine stateMachine = new StateMachine(this.repository);
		RaftServer raftServer = RaftServer.newBuilder().setServerId(myId).setStateMachine(stateMachine)
				.setProperties(properties).setGroup(raftGroup).build();
		raftServer.start();

		while (raftServer.getLifeCycleState() != LifeCycle.State.CLOSED) {
			TimeUnit.SECONDS.sleep(1);
		}
	}
}
