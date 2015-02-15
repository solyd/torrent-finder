package torrentfinder.dht;


public class DHTNodeId extends DHTId {
	public DHTNodeId(String nodeId) throws InvalidDHTId {
		super(nodeId);
	}
	
	public DHTNodeId(byte[] nodeId) throws InvalidDHTId {
		super(nodeId);
	}
}
