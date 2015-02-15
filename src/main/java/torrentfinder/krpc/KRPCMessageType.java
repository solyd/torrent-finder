package torrentfinder.krpc;

public enum KRPCMessageType {
	Query		("q"),
	Response	("r"),
	Error		("e");
	
	String m_key;
	
	private KRPCMessageType(String key) {
		m_key = key;
	}
	
	public String key() {
		return m_key;
	}
	
	public static KRPCMessageType fromKey(String key) {
		if (key == null)
			return null;
		
		if 		(key.equals("q")) return Query;
		else if (key.equals("r")) return Response;
		else if (key.equals("e")) return Error;
		else return null;
	}
}
