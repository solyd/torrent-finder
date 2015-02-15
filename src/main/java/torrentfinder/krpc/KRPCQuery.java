package torrentfinder.krpc;

import torrentfinder.bencoding.BencodeDictionary;
import torrentfinder.bencoding.BencodeObject;
import torrentfinder.bencoding.BencodeString;

public class KRPCQuery extends KRPCMessage {
	protected static final String QueryMethodNameKey = "q";
	protected static final String QueryArgumentsKey  = "a";
	
	private BencodeDictionary m_queryArgs;
	private BencodeString	  m_queryMethod;
	
	public KRPCQuery(BencodeDictionary msg) throws InvalidKRPCMessage {
		super(msg);
		m_queryMethod = msg.getStringValue(QueryMethodNameKey);
		if (m_queryMethod == null)
			throw new InvalidKRPCQuery("query method is null");
		
		BencodeObject queryArgsObj = msg.get(QueryArgumentsKey);
		if (queryArgsObj == null)
			return;
		
		if (! (queryArgsObj instanceof BencodeDictionary))
			throw new InvalidKRPCQuery("query args are not a dictionary");
		
		m_queryArgs = (BencodeDictionary) queryArgsObj;		
	}
	
	public KRPCQuery(String queryId, String queryMethod) {
		super(KRPCMessageType.Query, queryId);
		m_queryArgs = new BencodeDictionary();
		m_queryMethod = new BencodeString(queryMethod);
		addToMessage(QueryMethodNameKey, m_queryMethod);
		addToMessage(QueryArgumentsKey, m_queryArgs);
	}
	
	protected KRPCQuery(KRPCQuery other) throws InvalidKRPCMessage {
		super(other);
		m_queryArgs = other.m_queryArgs;
		m_queryMethod = other.m_queryMethod;
	}
	
	protected KRPCQuery addArgument(String key, String value) {
		m_queryArgs.put(key, value);
		return this;
	}
	
	protected KRPCQuery addArgument(String key, int value) {
		m_queryArgs.put(key, value);
		return this;
	}
	
	protected KRPCQuery addArgument(String key, byte[] value) {
		m_queryArgs.put(key, value);
		return this;
	}
	
	protected KRPCQuery addArgument(String key, BencodeObject value) {
		m_queryArgs.put(key, value);
		return this;
	}
}
