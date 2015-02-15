package torrentfinder.bencoding;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import torrentfinder.utils.ByteBufferBuilder;
import torrentfinder.utils.Hexdump;
import torrentfinder.utils.TFUtils;

public class BencodeString implements BencodeObject {
	protected static final char 	LengthValueSeperator 		= ':';
	protected static final byte[] 	LengthValueSeperatorBytes 	= new byte[] { LengthValueSeperator };
	
	protected String m_stringValue;
	protected byte[] m_value;
	
	protected BencodeString() {		
	}
	
	public BencodeString(String value) {
		m_value = value.getBytes(StandardCharsets.UTF_8);
		m_stringValue = value;
	}
	
	public BencodeString(byte[] value) {
		m_value = value;
		m_stringValue = new String(m_value, StandardCharsets.UTF_8);
	}
	
	public byte[] encode() {
		return new ByteBufferBuilder()
			.append(asciiLengthBytes())
			.append(LengthValueSeperatorBytes)
			.append(m_value)
			.buildArray();
	}

	public void decode(ByteBuffer data) throws InvalidBencodeObject {
		// TODO validate that data is a valid bencoded string
		int stringLength = (int) TFUtils.readASCIINumber(data);
		data.get(); // skip seperator
		m_value = new byte[stringLength];
		data.get(m_value);
		m_stringValue = new String(m_value, StandardCharsets.UTF_8);
	}

	public byte[] bytes() {
		return m_value;
	}
	
	public String string() {
		return m_stringValue;
	}
	
	public String hex() {
		return Hexdump.toHexString(m_value);
	}

	@Override
	public String toString() {
		return String.format("\"%s\" (%s)", m_stringValue, hex());
	}
	
	@Override
	public int hashCode() {
		return m_stringValue.hashCode();
	}
	
	@Override 
	public boolean equals(Object obj) {
		if (! (obj instanceof BencodeString))
			return false;
		return Arrays.equals(m_value, ((BencodeString) obj).bytes());		
	}
		
	protected byte[] asciiLengthBytes() {
		return Integer.toString(m_value.length).getBytes(StandardCharsets.US_ASCII);
	}
}
