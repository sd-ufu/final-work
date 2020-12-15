package sdufu.finalwork.ratis;

import java.io.Serializable;
import java.math.BigInteger;

public class Request implements Serializable {
	private static final long serialVersionUID = 1L;

	private BigInteger key;
	private byte[] data;
	private long version;
	private long timestamp;
	private Operation operation;

	public Request(BigInteger key, byte[] data, long version, long timestamp, Operation operation) {
		this.key = key;
		this.data = data;
		this.version = version;
		this.timestamp = timestamp;
		this.operation = operation;
	}

	public Request(BigInteger key, long version, Operation operation) {
		this.key = key;
		this.version = version;
		this.operation = operation;
	}

	public Request(BigInteger key, long version, long timestamp, byte[] data, Operation operation) {
		this.key = key;
		this.version = version;
		this.timestamp = timestamp;
		this.data = data;
		this.operation = operation;
	}

	public Request(BigInteger key, Operation operation) {
		this.key = key;
		this.operation = operation;
	}

	public BigInteger getKey() {
		return key;
	}

	public void setKey(BigInteger key) {
		this.key = key;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

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

	public Operation getOperation() {
		return operation;
	}

	public void setOperation(Operation operation) {
		this.operation = operation;
	}
}
