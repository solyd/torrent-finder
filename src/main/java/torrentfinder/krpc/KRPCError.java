package torrentfinder.krpc;

import torrentfinder.bencoding.BencodeDictionary;
import torrentfinder.bencoding.BencodeInteger;
import torrentfinder.bencoding.BencodeList;
import torrentfinder.bencoding.BencodeObject;
import torrentfinder.bencoding.BencodeString;

public class KRPCError extends KRPCMessage {
	protected static final String ErrorDescriptionKey = "e";
	
	private BencodeList m_error;
	private ErrorCode   m_code;
	private String  	m_description;
	
	public static enum ErrorCode {
		Generic			(201),
		Server			(202),
		Protocol		(203),
		MethodUnknown	(204);
		
		private int m_errcode;
		private ErrorCode(int errcode) {
			m_errcode = errcode;
		}
		
		public int code() {
			return m_errcode;
		}
		
		public static ErrorCode fromLong(long err) {
			if (err == 201) return Generic;
			if (err == 202) return Server;
			if (err == 203) return Protocol;
			if (err == 204) return MethodUnknown;
			return null;
		}
	}
	
	public KRPCError(BencodeDictionary dict) throws InvalidKRPCMessage {
		super(dict);
		
		m_error = dict.getListValue(ErrorDescriptionKey);
		if (m_error == null)
			throw new InvalidKRPCError("no error list");
		
		if (m_error.size() != 2)
			throw new InvalidKRPCError("error list does not contain error code and description");
		
		BencodeObject longcode = m_error.get(0);
		if (! (longcode instanceof BencodeInteger))
			throw new InvalidKRPCError("error number is not integer");
		
		long codeval = ((BencodeInteger) longcode).value();
		m_code = ErrorCode.fromLong(codeval);
		if (m_code == null)
			throw new InvalidKRPCError(String.format("invalid error number: %d", codeval));		
		
		BencodeObject descStr = m_error.get(1);
		if (! (descStr instanceof BencodeString))
			throw new InvalidKRPCError("error description is not string");
		
		m_description = ((BencodeString) descStr).string();
	}
	
	public KRPCError(String transactionId, ErrorCode code, String description) {
		super(KRPCMessageType.Error, transactionId);		
		m_error = new BencodeList().add(code.code()).add(description);
		addToMessage(ErrorDescriptionKey, m_error);
		m_code = code;
		m_description = description;
	}
	
	protected KRPCError(KRPCError other) throws InvalidKRPCMessage {
		super(other);
		m_code = other.m_code;
		m_description = other.m_description;
	}
	
	public String description() {
		return m_description;
	}
	
	public ErrorCode code() {
		return m_code;
	}
	
	@Override
	public String toString() {
		return String.format("%d: %s", m_code.code(), m_description);
	}
}
