package torrentfinder.dht;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import torrentfinder.bencoding.BencodeList;
import torrentfinder.bencoding.BencodeObject;
import torrentfinder.bencoding.BencodeString;
import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.dht.DHTNodeInfo.InvalidDHTNodeInfo;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.InvalidKRPCResponse;
import torrentfinder.krpc.KRPCResponse;

public class GetPeersQuery extends DHTQuery {
	public static final String InfohashArgumentKey = "info_hash";
	
	public GetPeersQuery(String queryId, DHTNodeId srcId, Infohash infohash) {
		super(DHTQueryType.GetPeers, queryId, srcId);
		addArgument(InfohashArgumentKey, infohash);
	}
	
	public static class Response extends DHTQueryResponse {
		public static final String TokenKey  = "token";
		public static final String ValuesKey = "values";
		public static final String NodesKey  = "nodes";
		
		private BencodeString m_token;
		private List<DHTNodeInfo> m_closestPeers;
		private List<DHTNodeInfo> m_peers;

		public Response(KRPCResponse response) throws InvalidKRPCMessage, InvalidDHTId, UnknownHostException, InvalidDHTNodeInfo {
			super(response);			
			BencodeObject tokenObj = getReturnValue(TokenKey);
			if (tokenObj != null)  {
				if (! (tokenObj instanceof BencodeString))
					throw new InvalidKRPCResponse("token value is not a bencoded string");

				m_token = (BencodeString) tokenObj;
			}
			
			BencodeObject valuesObj = getReturnValue(ValuesKey);
			if (valuesObj != null) {
				if (! (valuesObj instanceof BencodeList))
					throw new InvalidKRPCResponse("values are not a list");
				
				BencodeList values = (BencodeList) valuesObj;
				return;
			}
			
			ByteBuffer nodes = ByteBuffer.wrap(getBinaryReturnValue(NodesKey));
			if (nodes == null)
				throw new InvalidKRPCResponse("both nodes and values are missing");
			
			m_closestPeers = new ArrayList<DHTNodeInfo>();
			while (nodes.hasRemaining())
				m_closestPeers.add(new DHTNodeInfo(nodes));			
		}
		
		public BencodeString token() {
			return m_token;
		}
		
		public boolean hasPeers() {
			return m_peers != null;
		}
		
		public List<DHTNodeInfo> closestPeers() {
			return m_closestPeers;
		}
				
		public List<DHTNodeInfo> peers() {
			return m_peers;
		}
	}
}
