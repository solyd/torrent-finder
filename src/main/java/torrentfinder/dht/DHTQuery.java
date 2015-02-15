package torrentfinder.dht;

import torrentfinder.bencoding.BencodeDictionary;
import torrentfinder.krpc.InvalidKRPCMessage;
import torrentfinder.krpc.KRPCQuery;

public class DHTQuery extends KRPCQuery {
	public static final String IdKey = "id";
	
	public DHTQuery(BencodeDictionary query) throws InvalidKRPCMessage {
		super(query);
	}
	
	public DHTQuery(DHTQueryType type, String queryId, DHTNodeId srcId) {
		super(queryId, type.toString());
		addArgument(IdKey, srcId);
	}
	
	
}
