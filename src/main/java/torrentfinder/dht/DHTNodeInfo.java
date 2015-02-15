package torrentfinder.dht;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.utils.TFUtils;

public class DHTNodeInfo {
	public static final int CompactNodeInfoLengthBytes = 26;
	
	private DHTNodeId m_nodeId;
	private InetSocketAddress m_nodeAddr;
	
	public static class InvalidDHTNodeInfo extends Exception {
		public InvalidDHTNodeInfo(String err) {
			super(err);
		}
	}
	
	public DHTNodeInfo(ByteBuffer compactNodeInfo) throws InvalidDHTNodeInfo, InvalidDHTId, UnknownHostException {
		if (compactNodeInfo.remaining() < CompactNodeInfoLengthBytes)
			throw new InvalidDHTNodeInfo(String.format("compact node info length is %d, expected %d", 
			                                           compactNodeInfo.remaining(), CompactNodeInfoLengthBytes));
		
		m_nodeId = new DHTNodeId(TFUtils.readBytesFrom(compactNodeInfo, DHTNodeId.IdByteLength));
		
		InetAddress addr = InetAddress.getByAddress(TFUtils.readBytesFrom(compactNodeInfo, 4));
		int port = TFUtils.readTwoBytesAsInt(compactNodeInfo);
		m_nodeAddr = new InetSocketAddress(addr, port);
	}
	
	public DHTNodeId nodeId() {
		return m_nodeId;
	}
	
	public InetSocketAddress nodeAddr() {
		return m_nodeAddr;
	}
	
	@Override
	public String toString() {
		return String.format("id=%s, addr=%s", m_nodeId.toString(), m_nodeAddr.toString());
	}
}
