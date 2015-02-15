package torrentfinder.dht;

import torrentfinder.dht.DHTId.InvalidDHTId;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.KRPCResponse;

public class AnnouncePeerQuery extends DHTQuery {
	public static final String InfoHashKey     = "info_hash";
	public static final String PortKey  	   = "port";
	public static final String TokenKey 	   = "token";
	public static final String ImpliedPortKey = "implied_port";
	
	public AnnouncePeerQuery(String queryId, 
	                         DHTNodeId srcId, 
	                         Infohash infohash, 
	                         int port, 	
	                         byte[] token, 
	                         boolean impliedPort) 
	{
		super(DHTQueryType.AnnouncePeer, queryId, srcId);
		addArgument(InfoHashKey, infohash);
		addArgument(PortKey, port);
		addArgument(TokenKey, token);
		addArgument(ImpliedPortKey, impliedPort ? 1 : 0);
	}
	
	public static class Response extends DHTQueryResponse {
		public Response(KRPCResponse response) throws InvalidKRPCMessage, InvalidDHTId {
			super(response);
		}
	}
}
