package torrentfinder.dht;

import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.InvalidKRPCResponse;
import torrentfinder.krpc.KRPCResponse;

public class DHTQueryResponse extends KRPCResponse {
	protected static final String RespondingNodeIdKey = "id";
	
	private DHTNodeId m_respondingNodeId;
	
	public DHTQueryResponse(KRPCResponse response) throws InvalidKRPCMessage, InvalidDHTId {
		super(response);
		m_respondingNodeId = new DHTNodeId(getBinaryReturnValue(RespondingNodeIdKey));
		
		if (m_respondingNodeId == null)
			throw new InvalidKRPCResponse("does not contain responding node id");
	}
	
	public DHTNodeId respondingNodeId() {
		return m_respondingNodeId;
	}
}
