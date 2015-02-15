package torrentfinder.dht;

import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.KRPCResponse;

public class PingQuery extends DHTQuery {
	public PingQuery(String queryId, DHTNodeId srcId) {
		super(DHTQueryType.Ping, queryId, srcId);
	}
	
	public static class Response extends DHTQueryResponse {
		public Response(KRPCResponse response) throws InvalidKRPCMessage, InvalidDHTId {
			super(response);
		}
	}
}
