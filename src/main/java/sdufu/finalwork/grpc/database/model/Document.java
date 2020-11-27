package sdufu.finalwork.grpc.database.model;

import java.io.Serializable;

public class Document implements Serializable {
	private static final long serialVersionUID = 1L;

	private long version;
	private long timestamp;
	private byte[] data;

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
