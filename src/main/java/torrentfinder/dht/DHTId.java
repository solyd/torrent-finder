package torrentfinder.dht;

import java.nio.ByteBuffer;

import torrentfinder.bencoding.BencodeString;
import torrentfinder.utils.TFUtils;

// 20 byte string
public class DHTId extends BencodeString {
	public static final int IdByteLength = 20;
	
	public static class InvalidDHTId extends Exception {
		public InvalidDHTId(String err) {
			super(err);
		}
	}

	protected DHTId(String id) throws InvalidDHTId {
		super(id);
		if (m_value.length != IdByteLength)
			throw new InvalidDHTId(String.format("id length must be %d, provided id length is %d",
			                                     IdByteLength, m_value.length));
	}
	
	protected DHTId(byte[] id) throws InvalidDHTId {
		super(id);
		if (m_value.length != IdByteLength)
			throw new InvalidDHTId(String.format("id length must be %d, provided id length is %d",
			                                     IdByteLength, m_value.length));
	}
}
