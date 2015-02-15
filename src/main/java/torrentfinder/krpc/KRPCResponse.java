package torrentfinder.krpc;

import torrentfinder.bencoding.BencodeDictionary;
import torrentfinder.bencoding.BencodeList;
import torrentfinder.bencoding.BencodeObject;
import torrentfinder.bencoding.BencodeString;

public class KRPCResponse extends KRPCMessage {
	protected static final String ReturnValuesKey = "r";
	
	private BencodeDictionary m_returnValues;
	
	public KRPCResponse(BencodeDictionary resp) throws InvalidKRPCMessage {
		super(resp);
		BencodeObject returnValuesObj = resp.get(ReturnValuesKey);
		if (returnValuesObj == null)
			throw new InvalidKRPCResponse("return values are null");
		
		if (! (returnValuesObj instanceof BencodeDictionary))
			throw new InvalidKRPCResponse("return values are not a dictionary");
		
		m_returnValues = (BencodeDictionary) returnValuesObj;
	}
	
	public KRPCResponse(String queryId) {
		super(KRPCMessageType.Response, queryId);
		m_returnValues = new BencodeDictionary();
		addToMessage(ReturnValuesKey, m_returnValues);
	}
	
	protected KRPCResponse(KRPCResponse other) throws InvalidKRPCMessage {
		super(other);
		m_returnValues = other.m_returnValues;
	}
	
	protected KRPCResponse addReturnValue(String key, String value) {
		m_returnValues.put(key, value);
		return this;
	}
	
	protected BencodeObject getReturnValue(String key) {
		return m_returnValues.get(key);
	}
	
	protected BencodeList getListReturnValue(String key) {
		return m_returnValues.getListValue(key);
	}
	
	protected BencodeString getStringReturnValue(String key) {
		return m_returnValues.getStringValue(key);
	}
	
	protected byte[] getBinaryReturnValue(String key) {
		return m_returnValues.getBinaryReturnValue(key);
	}
	
	@Override
	public String toString() {
		return String.format("Response to query id \"%s\", return values: %s", 
		                     transactionId(), m_returnValues.toString());
	}
}
