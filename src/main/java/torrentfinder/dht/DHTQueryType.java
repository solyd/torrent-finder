package torrentfinder.dht;

public enum DHTQueryType {
	Ping		 ("ping"),
	FindNode	 ("find_node"),
	GetPeers	 ("get_peers"),
	AnnouncePeer ("announce_peer");
	
	private String m_str;
	private DHTQueryType(String str) {
		m_str = str;
	}
	
	public String toString() {
		return m_str;
	}
	
	public static DHTQueryType fromString(String type) {
		if (type.equals(Ping.toString()))   	  return Ping;
		if (type.equals(FindNode.toString()))     return FindNode;
		if (type.equals(GetPeers.toString()))     return GetPeers;
		if (type.equals(AnnouncePeer.toString())) return AnnouncePeer;
		return null;
	}
	
	protected static String queryTypeToStringValue(DHTQueryType type) {
		switch (type) {
			case Ping:  	   return "ping";
			case FindNode:     return "find_node";
			case GetPeers:     return "get_peers";
			case AnnouncePeer: return "announce_peer";
		}
		
		return null;
	}
	
}
