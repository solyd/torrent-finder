package torrentfinder.dht;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.dht.DHTNodeInfo.InvalidDHTNodeInfo;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.KRPCResponse;

public class FindNodeQuery extends DHTQuery {
	public static final String TargetArgumentKey = "target";
	
	public FindNodeQuery(String queryId, DHTNodeId srcId, DHTNodeId targetId) {
		super(DHTQueryType.FindNode, queryId, srcId);
		addArgument(TargetArgumentKey, targetId);
	}
	
	public static class Response extends DHTQueryResponse {
		public static final String NodesReturnValueKey = "nodes";
		
		private List<DHTNodeInfo> m_nodes;
		
		public Response(KRPCResponse response) throws InvalidKRPCMessage, InvalidDHTId, UnknownHostException, InvalidDHTNodeInfo {
			super(response);
			m_nodes = new ArrayList<DHTNodeInfo>();
			ByteBuffer nodes = ByteBuffer.wrap(getBinaryReturnValue(NodesReturnValueKey));
			while (nodes.hasRemaining())
				m_nodes.add(new DHTNodeInfo(nodes));
		}		
		
		public List<DHTNodeInfo> nodes() {
			return m_nodes;
		}
	}
}
